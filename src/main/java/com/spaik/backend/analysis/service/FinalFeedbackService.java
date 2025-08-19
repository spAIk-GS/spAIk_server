package com.spaik.backend.analysis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.*;
import com.spaik.backend.analysis.gemini.GeminiClient;
import com.spaik.backend.analysis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinalFeedbackService {

    private final ReportRepository reportRepo;
    private final AudioFeedbackRepository audioRepo;
    private final VideoFeedbackRepository videoRepo;
    private final FinalFeedbackRepository finalFeedbackRepo;
    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    @Transactional
    public FinalFeedbackResponseDto createFinalFeedback(FinalFeedbackRequestDto requestDto) {
        // 1️⃣ Report 조회
        String presentationId = requestDto.getPresentationId();
        Report report = reportRepo.findByPresentationPresentationId(presentationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Report not found for presentationId: " + presentationId));

        // 2️⃣ AudioFeedback, VideoFeedback 조회
        AudioFeedback audioFeedback = audioRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException(
                        "AudioFeedback not found for reportId: " + report.getId()));

        VideoFeedback videoFeedback = videoRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException(
                        "VideoFeedback not found for reportId: " + report.getId()));

        // 3️⃣ DTO 변환 (AnalysisCallbackDto 기준)
        AnalysisCallbackDto.AudioAnalysis audioDto = new AnalysisCallbackDto.AudioAnalysis();
        audioDto.setAnalysisId(audioFeedback.getAnalysisIdAudio());
        audioDto.setStatus(audioFeedback.getStatus().name());

        var audioResults = new java.util.HashMap<String, AnalysisCallbackDto.AnalysisResult>();
        mapAudioResult("speed", audioFeedback.getSpeedEmotion(), audioFeedback.getSpeedSegmentsJson(), audioResults);
        mapAudioResult("pitch", audioFeedback.getPitchEmotion(), audioFeedback.getPitchSegmentsJson(), audioResults);
        mapAudioResult("volume", audioFeedback.getVolumeEmotion(), audioFeedback.getVolumeSegmentsJson(), audioResults);
        mapAudioResult("stutter", audioFeedback.getStutterEmotion(), audioFeedback.getStutterSegmentsJson(), audioResults);
        audioDto.setResults(audioResults);

        AnalysisCallbackDto.VideoAnalysis videoDto = new AnalysisCallbackDto.VideoAnalysis();
        videoDto.setAnalysisId(videoFeedback.getAnalysisIdVideo());
        videoDto.setStatus(videoFeedback.getStatus().name());

        var videoResults = new java.util.HashMap<String, AnalysisCallbackDto.AnalysisResult>();
        mapVideoResult("movement", videoFeedback.getMovementEmotion(), videoFeedback.getMovementSegmentsJson(), videoResults);
        mapVideoResult("gaze", videoFeedback.getGazeEmotion(), videoFeedback.getGazeSegmentsJson(), videoResults);
        videoDto.setResults(videoResults);

        AnalysisCallbackDto analysisCallbackDto = new AnalysisCallbackDto();
        analysisCallbackDto.setPresentationId(presentationId);
        analysisCallbackDto.setAudio(audioDto);
        analysisCallbackDto.setVideo(videoDto);

        // 4️⃣ Gemini API 호출
        String geminiResponseJson;
        try {
            geminiResponseJson = geminiClient.requestFinalFeedback(
                    objectMapper.writeValueAsString(analysisCallbackDto),
                    objectMapper.writeValueAsString(analysisCallbackDto) // video JSON도 함께 전달
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize analysis DTO for Gemini", e);
        }

        // 5️⃣ Gemini 응답 파싱
        String finalFeedbackText;
        try {
            JsonNode rootNode = objectMapper.readTree(geminiResponseJson);
            finalFeedbackText = rootNode
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Gemini API response", e);
        }

        // 6️⃣ FinalFeedback 저장
        FinalFeedback finalFeedback = FinalFeedback.builder()
                .report(report)
                .finalFeedback(finalFeedbackText)
                .build();
        finalFeedbackRepo.save(finalFeedback);

        // 7️⃣ 응답 DTO 반환
        return FinalFeedbackResponseDto.builder()
                .finalFeedbackId(finalFeedback.getId())
                .reportId(report.getId())
                .finalFeedback(finalFeedback.getFinalFeedback())
                .build();
    }

    // --- Helper Methods ---
    private void mapAudioResult(String key, String emotion, String jsonSegments,
                                java.util.Map<String, AnalysisCallbackDto.AnalysisResult> results) {
        if (jsonSegments != null) {
            AnalysisCallbackDto.AnalysisResult result = new AnalysisCallbackDto.AnalysisResult();
            result.setEmotion(emotion);
            try {
                result.setSegments(java.util.Arrays.asList(
                        objectMapper.readValue(jsonSegments, AnalysisCallbackDto.Segment[].class)
                ));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse " + key + " segments JSON", e);
            }
            results.put(key, result);
        }
    }

    private void mapVideoResult(String key, String emotion, String jsonSegments,
                                java.util.Map<String, AnalysisCallbackDto.AnalysisResult> results) {
        if (jsonSegments != null) {
            AnalysisCallbackDto.AnalysisResult result = new AnalysisCallbackDto.AnalysisResult();
            result.setEmotion(emotion);
            try {
                result.setSegments(java.util.Arrays.asList(
                        objectMapper.readValue(jsonSegments, AnalysisCallbackDto.Segment[].class)
                ));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse " + key + " segments JSON", e);
            }
            results.put(key, result);
        }
    }
}

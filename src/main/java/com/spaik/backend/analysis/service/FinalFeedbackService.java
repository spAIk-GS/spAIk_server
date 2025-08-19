package com.spaik.backend.analysis.service;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        String presentationId = requestDto.getPresentationId();
        Report report = reportRepo.findByPresentationPresentationId(presentationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Report not found for presentationId: " + presentationId));

        AudioFeedback audioFeedback = audioRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException(
                        "AudioFeedback not found for reportId: " + report.getId()));

        VideoFeedback videoFeedback = videoRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException(
                        "VideoFeedback not found for reportId: " + report.getId()));

        // DTO 변환
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

        // Gemini API 호출
        String geminiResponseJson;
        try {
            // null 필드 제거해 토큰 절감
            String payload = objectMapper.copy()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(analysisCallbackDto);

            geminiResponseJson = geminiClient.requestFinalFeedback(payload);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize analysis DTO for Gemini", e);
        }

        // Gemini 응답 파싱
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

        // FinalFeedback 저장
        FinalFeedback finalFeedback = FinalFeedback.builder()
                .report(report)
                .finalFeedback(finalFeedbackText)
                .build();
        finalFeedbackRepo.save(finalFeedback);

        return FinalFeedbackResponseDto.builder()
                .finalFeedbackId(finalFeedback.getId())
                .reportId(report.getId())
                .finalFeedback(finalFeedback.getFinalFeedback())
                .build();
    }

    private java.util.List<AnalysisCallbackDto.Segment> parseSegmentsToList(String json) {
        if (json == null) return java.util.Collections.emptyList();
        String trimmed = json.trim();
        if (trimmed.isEmpty() || "null".equalsIgnoreCase(trimmed)) {
            return java.util.Collections.emptyList();
        }
        try {
            AnalysisCallbackDto.Segment[] arr =
                    objectMapper.readValue(trimmed, AnalysisCallbackDto.Segment[].class);
            if (arr == null) return java.util.Collections.emptyList();
            return java.util.Arrays.asList(arr); // arr는 이제 null 아님
        } catch (Exception e) {
            // 파싱 실패 시도 빈 리스트로 폴백 (로그는 필요시 추가)
            return java.util.Collections.emptyList();
        }
    }

    private void mapAudioResult(String key, String emotion, String jsonSegments,
                                java.util.Map<String, AnalysisCallbackDto.AnalysisResult> results) {
        java.util.List<AnalysisCallbackDto.Segment> segs = parseSegmentsToList(jsonSegments);
        if (segs.isEmpty() && (emotion == null || emotion.isBlank())) {
            return; // 완전 빈 데이터면 스킵
        }
        AnalysisCallbackDto.AnalysisResult result = new AnalysisCallbackDto.AnalysisResult();
        result.setEmotion(emotion);
        result.setSegments(segs);
        results.put(key, result);
    }

    private void mapVideoResult(String key, String emotion, String jsonSegments,
                                java.util.Map<String, AnalysisCallbackDto.AnalysisResult> results) {
        java.util.List<AnalysisCallbackDto.Segment> segs = parseSegmentsToList(jsonSegments);
        if (segs.isEmpty() && (emotion == null || emotion.isBlank())) {
            return;
        }
        AnalysisCallbackDto.AnalysisResult result = new AnalysisCallbackDto.AnalysisResult();
        result.setEmotion(emotion);
        result.setSegments(segs);
        results.put(key, result);
    }
}

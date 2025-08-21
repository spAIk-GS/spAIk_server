package com.spaik.backend.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.AnalysisCallbackDto;
import com.spaik.backend.analysis.dto.FinalAnalysisResultDto;
import com.spaik.backend.analysis.dto.ReportResponseDto;
import com.spaik.backend.analysis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ReportRepository reportRepository;
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final VideoFeedbackRepository videoFeedbackRepository;
    private final FinalFeedbackRepository finalFeedbackRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public FinalAnalysisResultDto getResultByPresentationId(String presentationId) {

        Report report = reportRepository.findByPresentationPresentationId(presentationId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found for presentationId: " + presentationId));

        AudioFeedback audioFeedback = audioFeedbackRepository.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException("AudioFeedback not found"));
        VideoFeedback videoFeedback = videoFeedbackRepository.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException("VideoFeedback not found"));
        FinalFeedback finalFeedback = finalFeedbackRepository.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException("FinalFeedback not found"));

        ReportResponseDto summaryDto = ReportResponseDto.builder()
                .reportId(report.getId())
                .presentationId(presentationId)
                .createdAt(report.getCreatedAt())
                .finalFeedback(finalFeedback.getFinalFeedback())
                .build();

        // --- Details DTO 생성 (모든 수치 데이터를 포함하도록 수정) ---
        AnalysisCallbackDto detailsDto = buildDetailsDto(presentationId, audioFeedback, videoFeedback);

        return FinalAnalysisResultDto.builder()
                .summary(summaryDto)
                .details(detailsDto)
                .build();
    }

    // 👇 Details DTO를 생성하는 로직을 별도 메소드로 분리하여 가독성 향상
    private AnalysisCallbackDto buildDetailsDto(String presentationId, AudioFeedback audioFeedback, VideoFeedback videoFeedback) {

        // --- Video 정보 채우기 ---
        Map<String, AnalysisCallbackDto.AnalysisResult> videoResults = new HashMap<>();

        // Movement
        AnalysisCallbackDto.AnalysisResult movementResult = new AnalysisCallbackDto.AnalysisResult();
        movementResult.setEmotion(videoFeedback.getMovementEmotion());
        movementResult.setMovement_percent(videoFeedback.getMovementPercent());
        movementResult.setSegments(parseSegmentsToList(videoFeedback.getMovementSegmentsJson()));
        videoResults.put("movement", movementResult);

        // Gaze
        AnalysisCallbackDto.AnalysisResult gazeResult = new AnalysisCallbackDto.AnalysisResult();
        gazeResult.setEmotion(videoFeedback.getGazeEmotion());
        gazeResult.setFocus_level(videoFeedback.getFocusLevel());
        gazeResult.setSegments(parseSegmentsToList(videoFeedback.getGazeSegmentsJson()));
        videoResults.put("gaze", gazeResult);

        AnalysisCallbackDto.VideoAnalysis videoDetails = new AnalysisCallbackDto.VideoAnalysis();
        videoDetails.setAnalysisId(videoFeedback.getAnalysisIdVideo());
        videoDetails.setStatus(videoFeedback.getStatus().name());
        videoDetails.setResults(videoResults);

        // --- Audio 정보 채우기 ---
        Map<String, AnalysisCallbackDto.AnalysisResult> audioResults = new HashMap<>();

        // Speed
        AnalysisCallbackDto.AnalysisResult speedResult = new AnalysisCallbackDto.AnalysisResult();
        speedResult.setEmotion(audioFeedback.getSpeedEmotion());
        speedResult.setValue(audioFeedback.getSpeedValue());
        speedResult.setSegments(parseSegmentsToList(audioFeedback.getSpeedSegmentsJson()));
        audioResults.put("speed", speedResult);

        // Pitch
        AnalysisCallbackDto.AnalysisResult pitchResult = new AnalysisCallbackDto.AnalysisResult();
        pitchResult.setEmotion(audioFeedback.getPitchEmotion());
        pitchResult.setValue(audioFeedback.getPitchValue());
        pitchResult.setSegments(parseSegmentsToList(audioFeedback.getPitchSegmentsJson()));
        audioResults.put("pitch", pitchResult);

        // Volume
        AnalysisCallbackDto.AnalysisResult volumeResult = new AnalysisCallbackDto.AnalysisResult();
        volumeResult.setEmotion(audioFeedback.getVolumeEmotion());
        volumeResult.setDecibels(audioFeedback.getDecibels());
        // volume_anomalies는 필요 시 추가
        audioResults.put("volume", volumeResult);

        // Stutter
        AnalysisCallbackDto.AnalysisResult stutterResult = new AnalysisCallbackDto.AnalysisResult();
        stutterResult.setEmotion(audioFeedback.getStutterEmotion());
        stutterResult.setStutter_count(audioFeedback.getStutterCount());
        stutterResult.setStutter_details(parseStutterDetailsToList(audioFeedback.getStutterDetailsJson()));
        audioResults.put("stutter", stutterResult);

        AnalysisCallbackDto.AudioAnalysis audioDetails = new AnalysisCallbackDto.AudioAnalysis();
        audioDetails.setAnalysisId(audioFeedback.getAnalysisIdAudio());
        audioDetails.setStatus(audioFeedback.getStatus().name());
        audioDetails.setResults(audioResults);

        // --- 최종 Details DTO 조립 ---
        AnalysisCallbackDto detailsDto = new AnalysisCallbackDto();
        detailsDto.setPresentationId(presentationId);
        detailsDto.setAudio(audioDetails);
        detailsDto.setVideo(videoDetails);

        return detailsDto;
    }


    // --- 아래는 FinalFeedbackService에서 사용했던 Helper 메소드들입니다 ---

    private List<AnalysisCallbackDto.Segment> parseSegmentsToList(String json) {
        if (json == null || json.trim().isEmpty() || "null".equalsIgnoreCase(json.trim())) {
            return Collections.emptyList();
        }
        try {
            return Arrays.asList(objectMapper.readValue(json, AnalysisCallbackDto.Segment[].class));
        } catch (JsonProcessingException e) {
            // 로깅을 추가하면 더 좋습니다.
            return Collections.emptyList();
        }
    }

    private void mapAudioResult(String key, String emotion, String jsonSegments, Map<String, AnalysisCallbackDto.AnalysisResult> results) {
        List<AnalysisCallbackDto.Segment> segs = parseSegmentsToList(jsonSegments);
        if (segs.isEmpty() && (emotion == null || emotion.isBlank())) {
            return;
        }
        AnalysisCallbackDto.AnalysisResult result = new AnalysisCallbackDto.AnalysisResult();
        result.setEmotion(emotion);
        result.setSegments(segs);
        results.put(key, result);
    }

    private void mapVideoResult(String key, String emotion, String jsonSegments, Map<String, AnalysisCallbackDto.AnalysisResult> results) {
        List<AnalysisCallbackDto.Segment> segs = parseSegmentsToList(jsonSegments);
        if (segs.isEmpty() && (emotion == null || emotion.isBlank())) {
            return;

        }
        AnalysisCallbackDto.AnalysisResult result = new AnalysisCallbackDto.AnalysisResult();
        result.setEmotion(emotion);
        result.setSegments(segs);
        results.put(key, result);
    }

    private List<AnalysisCallbackDto.StutterDetail> parseStutterDetailsToList(String json) {
        if (json == null || json.trim().isEmpty() || "null".equalsIgnoreCase(json.trim())) {
            return Collections.emptyList();
        }
        try {
            return Arrays.asList(objectMapper.readValue(json, AnalysisCallbackDto.StutterDetail[].class));
        } catch (JsonProcessingException e) {
            // log.error("Failed to parse stutter details JSON", e);
            return Collections.emptyList();
        }
    }
}
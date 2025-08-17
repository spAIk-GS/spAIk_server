package com.spaik.backend.analysis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.FinalFeedbackRequestDto;
import com.spaik.backend.analysis.dto.FinalFeedbackResponseDto;
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
        // 1Report 조회
        String presentationId = requestDto.getPresentationId();
        Report report = reportRepo.findByPresentationPresentationId(presentationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Report not found for presentationId: " + presentationId));

        // AudioFeedback, VideoFeedback 조회
        AudioFeedback audioFeedback = audioRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException(
                        "AudioFeedback not found for reportId: " + report.getId()));

        VideoFeedback videoFeedback = videoRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException(
                        "VideoFeedback not found for reportId: " + report.getId()));

        // JSON 변환 (모든 분석 데이터 포함)
        String audioJson;
        String videoJson;
        try {
            audioJson = objectMapper.writeValueAsString(audioFeedback);
            videoJson = objectMapper.writeValueAsString(videoFeedback);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize feedback to JSON", e);
        }

        // Gemini API 호출
        String geminiResponseJson = geminiClient.requestFinalFeedback(audioJson, videoJson);

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

        // 응답 DTO 반환
        return FinalFeedbackResponseDto.builder()
                .finalFeedbackId(finalFeedback.getId())
                .reportId(report.getId())
                .finalFeedback(finalFeedback.getFinalFeedback())
                .build();
    }
}

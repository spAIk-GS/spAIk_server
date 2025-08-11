package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.gemini.GeminiClient;
import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.FinalFeedbackRequestDto;
import com.spaik.backend.analysis.dto.FullReportResponseDto;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import com.spaik.backend.analysis.repository.FinalFeedbackRepository;
import com.spaik.backend.analysis.repository.ReportRepository;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

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
    public FullReportResponseDto createFinalFeedback(FinalFeedbackRequestDto requestDto) {

        Long presentationId = requestDto.getPresentationId();

        // 1. Report 조회
        Optional<Report> optionalReport = reportRepo.findByPresentation_PresentationId(presentationId);
        Report report = optionalReport.orElseThrow(() -> new IllegalArgumentException("Report not found for presentationId: " + presentationId));

        // 2. AudioFeedback, VideoFeedback 조회
        AudioFeedback audioFeedback = audioRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException("AudioFeedback not found for reportId: " + report.getId()));

        VideoFeedback videoFeedback = videoRepo.findByReport(report)
                .orElseThrow(() -> new IllegalArgumentException("VideoFeedback not found for reportId: " + report.getId()));

        // 3. GeminiClient를 사용하여 최종 피드백 요청 (JSON 응답을 받음)
        String geminiResponseJson = geminiClient.requestFinalFeedback(
                presentationId,
                audioFeedback.getContentSummary(),
                videoFeedback.getBodyMovementFeedback() + " " + videoFeedback.getGazeOutFeedback()
        );

        String finalFeedbackText;
        try {
            // ObjectMapper를 사용하여 JSON 응답에서 텍스트를 추출
            JsonNode rootNode = objectMapper.readTree(geminiResponseJson);
            finalFeedbackText = rootNode.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Gemini API response", e);
        }

        // 4. FinalFeedback 엔티티 저장
        FinalFeedback finalFeedback = FinalFeedback.builder()
                .report(report)
                .finalFeedback(finalFeedbackText)
                .build();

        finalFeedbackRepo.save(finalFeedback);

        // 5. 응답 DTO 생성 (FullReportResponseDto 사용)
        return FullReportResponseDto.builder()
                .reportId(report.getId())
                .presentationId(report.getPresentation().getPresentationId())
                .audioFeedback(audioFeedback)
                .videoFeedback(videoFeedback)
                .finalFeedback(finalFeedback)
                .build();
    }
}
package com.spaik.backend.analysis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.AnalysisCallbackDto;
import com.spaik.backend.analysis.dto.FinalFeedbackRequestDto;
import com.spaik.backend.analysis.dto.ReportResponseDto;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import com.spaik.backend.analysis.repository.ReportRepository;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackService {

    private final VideoFeedbackRepository videoFeedbackRepository;
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final ReportRepository reportRepository;
    private final FinalFeedbackService finalFeedbackService;
    private final SSEService sseService;
    private final ObjectMapper objectMapper;

    @Transactional
    public Optional<ReportResponseDto> saveAnalysisResult(AnalysisCallbackDto dto) {

        // 1️⃣ Report 조회
        Report report = reportRepository.findByPresentationPresentationId(dto.getPresentationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Report not found for presentationId: " + dto.getPresentationId()));

        // 2️⃣ Video 저장
        if (dto.getVideo() != null) {
            VideoFeedback videoFeedback = videoFeedbackRepository.findByReport(report)
                    .orElse(VideoFeedback.builder()
                            .report(report)
                            .status(AnalysisStatus.PENDING)
                            .createdAt(LocalDateTime.now())
                            .build());

            videoFeedback.setAnalysisIdVideo(dto.getVideo().getAnalysisId());
            videoFeedback.setStatus(AnalysisStatus.COMPLETED);

            if (dto.getVideo().getResults() != null) {
                try {
                    videoFeedback.setMovementSegmentsJson(
                            objectMapper.writeValueAsString(dto.getVideo().getResults().get("movement")));
                    videoFeedback.setGazeSegmentsJson(
                            objectMapper.writeValueAsString(dto.getVideo().getResults().get("gaze")));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to serialize video segments", e);
                }
            }
            videoFeedbackRepository.save(videoFeedback);
        }

        // 3️⃣ Audio 저장
        if (dto.getAudio() != null) {
            AudioFeedback audioFeedback = audioFeedbackRepository.findByReport(report)
                    .orElse(AudioFeedback.builder()
                            .report(report)
                            .status(AnalysisStatus.PENDING)
                            .createdAt(LocalDateTime.now())
                            .build());

            audioFeedback.setAnalysisIdAudio(dto.getAudio().getAnalysisId());
            audioFeedback.setStatus(AnalysisStatus.COMPLETED);

            if (dto.getAudio().getResults() != null) {
                try {
                    audioFeedback.setSpeedSegmentsJson(
                            objectMapper.writeValueAsString(dto.getAudio().getResults().get("speed")));
                    audioFeedback.setPitchSegmentsJson(
                            objectMapper.writeValueAsString(dto.getAudio().getResults().get("pitch")));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to serialize audio segments", e);
                }
            }
            audioFeedbackRepository.save(audioFeedback);
        }

        // 4️⃣ 최종 피드백 생성
        boolean videoCompleted = videoFeedbackRepository.findByReport(report)
                .map(f -> f.getStatus() == AnalysisStatus.COMPLETED).orElse(false);
        boolean audioCompleted = audioFeedbackRepository.findByReport(report)
                .map(f -> f.getStatus() == AnalysisStatus.COMPLETED).orElse(false);

        ReportResponseDto finalReport = null;
        if (videoCompleted && audioCompleted && report.getFinalFeedback() == null) {
            // ✅ FinalFeedbackRequestDto 생성 후 전달
            FinalFeedbackRequestDto requestDto = FinalFeedbackRequestDto.builder()
                    .presentationId(report.getPresentation().getPresentationId())
                    .build();
            finalFeedbackService.createFinalFeedback(requestDto);

            finalReport = ReportResponseDto.builder()
                    .reportId(report.getId())
                    .presentationId(report.getPresentation().getPresentationId())
                    .createdAt(report.getCreatedAt())
                    .build();

            // 5️⃣ SSE로 프론트에 실시간 전송
            sseService.sendUpdate(report.getPresentation().getPresentationId(), finalReport);
        }

        return Optional.ofNullable(finalReport);
    }
}

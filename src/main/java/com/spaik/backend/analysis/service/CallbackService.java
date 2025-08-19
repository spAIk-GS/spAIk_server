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
import com.spaik.backend.analysis.repository.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CallbackService {

    private final VideoFeedbackRepository videoFeedbackRepository;
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final ReportRepository reportRepository;
    private final PresentationRepository presentationRepository;
    private final FinalFeedbackService finalFeedbackService;
    private final SSEService sseService;
    private final ObjectMapper objectMapper;

    @Transactional
    public Optional<ReportResponseDto> saveAnalysisResult(AnalysisCallbackDto dto) {

        // 1️⃣ Presentation 조회
        Presentation presentation = presentationRepository.findByPresentationId(dto.getPresentationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Presentation not found: " + dto.getPresentationId()));

        // 2️⃣ Report 조회
        Report report = reportRepository.findByPresentationPresentationId(dto.getPresentationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Report not found for presentationId: " + dto.getPresentationId()));

        // Video 저장 로직
        if (dto.getVideo() != null) {
            VideoFeedback videoFeedback = videoFeedbackRepository.findByReport(report)
                    .orElse(VideoFeedback.builder()
                            .report(report)
                            .status(AnalysisStatus.PENDING)
                            .build());

            videoFeedback.setAnalysisIdVideo(dto.getVideo().getAnalysisId());
            videoFeedback.setStatus(AnalysisStatus.COMPLETED);

            if (dto.getVideo().getResults() != null) {
                try {
                    // movement
                    AnalysisCallbackDto.AnalysisResult movementResult = dto.getVideo().getResults().get("movement");
                    if (movementResult != null) {
                        videoFeedback.setMovementEmotion(movementResult.getEmotion());
                        videoFeedback.setMovementSegmentsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(movementResult.getSegments())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
                    }
                    // gaze
                    AnalysisCallbackDto.AnalysisResult gazeResult = dto.getVideo().getResults().get("gaze");
                    if (gazeResult != null) {
                        videoFeedback.setGazeEmotion(gazeResult.getEmotion());
                        videoFeedback.setGazeSegmentsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(gazeResult.getSegments())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to serialize video segments", e);
                }
            }
            videoFeedbackRepository.save(videoFeedback);
        }

        // Audio 저장 로직
        if (dto.getAudio() != null) {
            AudioFeedback audioFeedback = audioFeedbackRepository.findByReport(report)
                    .orElse(AudioFeedback.builder()
                            .report(report)
                            .status(AnalysisStatus.PENDING)
                            .build());

            audioFeedback.setAnalysisIdAudio(dto.getAudio().getAnalysisId());
            audioFeedback.setStatus(AnalysisStatus.COMPLETED);

            if (dto.getAudio().getResults() != null) {
                try {
                    // speed
                    AnalysisCallbackDto.AnalysisResult speedResult = dto.getAudio().getResults().get("speed");
                    if (speedResult != null) {
                        audioFeedback.setSpeedEmotion(speedResult.getEmotion());
                        audioFeedback.setSpeedSegmentsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(speedResult.getSegments())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
                    }
                    // pitch
                    AnalysisCallbackDto.AnalysisResult pitchResult = dto.getAudio().getResults().get("pitch");
                    if (pitchResult != null) {
                        audioFeedback.setPitchEmotion(pitchResult.getEmotion());
                        audioFeedback.setPitchSegmentsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(pitchResult.getSegments())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
                    }
                    // volume
                    AnalysisCallbackDto.AnalysisResult volumeResult = dto.getAudio().getResults().get("volume");
                    if (volumeResult != null) {
                        audioFeedback.setVolumeEmotion(volumeResult.getEmotion());
                        audioFeedback.setVolumeSegmentsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(volumeResult.getSegments())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
                    }
                    // stutter
                    AnalysisCallbackDto.AnalysisResult stutterResult = dto.getAudio().getResults().get("stutter");
                    if (stutterResult != null) {
                        audioFeedback.setStutterEmotion(stutterResult.getEmotion());
                        audioFeedback.setStutterSegmentsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(stutterResult.getSegments())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to serialize audio segments", e);
                }
            }
            audioFeedbackRepository.save(audioFeedback);
        }

        // 최종 피드백 생성 조건 확인
        boolean videoCompleted = videoFeedbackRepository.findByReport(report)
                .map(f -> f.getStatus() == AnalysisStatus.COMPLETED).orElse(false);
        boolean audioCompleted = audioFeedbackRepository.findByReport(report)
                .map(f -> f.getStatus() == AnalysisStatus.COMPLETED).orElse(false);

        ReportResponseDto finalReport = null;
        if (videoCompleted && audioCompleted && report.getFinalFeedback() == null) {
            FinalFeedbackRequestDto requestDto = FinalFeedbackRequestDto.builder()
                    .presentationId(report.getPresentation().getPresentationId())
                    .build();
            //finalFeedbackService.createFinalFeedback(requestDto);
            CompletableFuture.runAsync(() -> {
                try {
                    finalFeedbackService.createFinalFeedback(requestDto);
                } catch (Exception e) {
                    // 최소 수정: 콘솔 로그. (원하면 slf4j 로깅으로 교체 가능)
                    e.printStackTrace();
                }
            });

            finalReport = ReportResponseDto.builder()
                    .reportId(report.getId())
                    .presentationId(report.getPresentation().getPresentationId())
                    .createdAt(report.getCreatedAt())
                    .build();

            sseService.sendUpdate(report.getPresentation().getPresentationId(), finalReport);
        }

        return Optional.ofNullable(finalReport);
    }
}

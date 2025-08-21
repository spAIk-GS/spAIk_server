package com.spaik.backend.analysis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.*;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import com.spaik.backend.analysis.repository.ReportRepository;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import com.spaik.backend.analysis.repository.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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
    public Optional<ReportResponseDto> saveAnalysisResult(AnalysisCallbackDto dto) throws JsonProcessingException {

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
                        videoFeedback.setMovementPercent(movementResult.getMovement_percent());
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
                        videoFeedback.setFocusLevel(gazeResult.getFocus_level());
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
                        audioFeedback.setSpeedValue(speedResult.getValue());
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
                        audioFeedback.setPitchValue(pitchResult.getValue());
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
                        audioFeedback.setDecibels(volumeResult.getDecibels());
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
                        audioFeedback.setStutterCount(stutterResult.getStutter_count());
                        audioFeedback.setStutterDetailsJson(
                                objectMapper.writeValueAsString(
                                        java.util.Optional.ofNullable(stutterResult.getStutter_details())
                                                .orElse(java.util.Collections.emptyList())
                                )
                        );
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

            FinalFeedbackResponseDto feedbackResponse = finalFeedbackService.createFinalFeedback(requestDto);

            // 1. 종합 피드백 DTO 생성 (기존 finalReport)
            ReportResponseDto summaryDto = ReportResponseDto.builder()
                    .reportId(report.getId())
                    .presentationId(report.getPresentation().getPresentationId())
                    .createdAt(report.getCreatedAt())
                    .finalFeedback(feedbackResponse.getFinalFeedback())
                    .build();

            // 2. 상세 분석 데이터 DTO는 이미 'dto' 파라미터에 있습니다.
            AnalysisCallbackDto detailDto = dto;

            // 3. 새로 만든 FinalAnalysisResultDto에 종합과 상세 데이터를 모두 담습니다.
            FinalAnalysisResultDto finalResult = FinalAnalysisResultDto.builder()
                    .summary(summaryDto)
                    .details(detailDto)
                    .build();

            // 4. 최종 결과 객체를 JSON으로 변환하여 SSE로 전송합니다.
            String finalResultJson = objectMapper.writeValueAsString(Map.of(
                    "status", "COMPLETED",
                    "result", finalResult // 👈 finalReport 대신 finalResult를 담습니다.
            ));
            sseService.sendUpdate(report.getPresentation().getPresentationId(), finalResultJson);

            // Optional<ReportResponseDto>를 반환해야 하므로 기존 finalReport 변수를 사용합니다.
            finalReport = summaryDto;
        }

        return Optional.ofNullable(finalReport);
    }
}

package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.AudioFeedback;
import com.spaik.backend.analysis.domain.Report;
import com.spaik.backend.analysis.dto.VideoAnalysisResultDto;
import com.spaik.backend.analysis.dto.AudioAnalysisResultDto;
import com.spaik.backend.analysis.dto.FinalFeedbackRequestDto;
import com.spaik.backend.analysis.dto.FullReportResponseDto;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import com.spaik.backend.analysis.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackService {

    private final VideoFeedbackRepository videoFeedbackRepository;
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final ReportRepository reportRepository;
    private final FinalFeedbackService finalFeedbackService;

    // AI 비디오 분석 결과 저장
    @Transactional
    public Optional<FullReportResponseDto> saveVideoResult(VideoAnalysisResultDto dto) {
        VideoFeedback videoFeedback = videoFeedbackRepository.findByAnalysisId(dto.getAnalysisId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid analysisId: " + dto.getAnalysisId()));

        videoFeedback.setStatus(
                dto.getStatus() == VideoAnalysisResultDto.Status.COMPLETED
                        ? AnalysisStatus.COMPLETED
                        : AnalysisStatus.PENDING
        );

        if (dto.getResults() != null) {
            videoFeedback.setBodyMovement(dto.getResults().getBody_movement().getValue());
            videoFeedback.setBodyMovementValue(parseValue(dto.getResults().getBody_movement().getValue()));
            videoFeedback.setBodyMovementFeedback(dto.getResults().getBody_movement().getFeedback());

            videoFeedback.setGazeOut(dto.getResults().getGaze_out().getValue());
            videoFeedback.setGazeOutValue(parseValue(dto.getResults().getGaze_out().getValue()));
            videoFeedback.setGazeOutFeedback(dto.getResults().getGaze_out().getFeedback());
        }

        videoFeedbackRepository.save(videoFeedback);

        if (videoFeedback.getStatus() == AnalysisStatus.COMPLETED) {
            return Optional.ofNullable(videoFeedback.getReport())
                .map(report -> checkAndGenerateFinalFeedback(report.getId()));
        }
        return Optional.empty();
    }

    // AI 음성 분석 결과 저장
    @Transactional
    public Optional<FullReportResponseDto> saveAudioResult(AudioAnalysisResultDto dto) {
        AudioFeedback audioFeedback = audioFeedbackRepository.findByAnalysisId(dto.getAnalysisId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid analysisId: " + dto.getAnalysisId()));

        audioFeedback.setStatus(
                dto.getStatus() == AudioAnalysisResultDto.Status.COMPLETED
                        ? AnalysisStatus.COMPLETED
                        : AnalysisStatus.PENDING
        );

        if (dto.getResults() != null) {
            if (dto.getResults().containsKey("speed")) {
                audioFeedback.setSpeedValue(parseValue(dto.getResults().get("speed").getValue()));
                audioFeedback.setSpeedFeedback(dto.getResults().get("speed").getFeedback());
            }
            if (dto.getResults().containsKey("pitch")) {
                audioFeedback.setPitchValue(parseValue(dto.getResults().get("pitch").getValue()));
                audioFeedback.setPitchFeedback(dto.getResults().get("pitch").getFeedback());
            }
            if (dto.getResults().containsKey("volume")) {
                audioFeedback.setVolumeDecibels(parseValue(dto.getResults().get("volume").getValue()));
                audioFeedback.setVolumeFeedback(dto.getResults().get("volume").getFeedback());
            }
            if (dto.getResults().containsKey("stutter")) {
                audioFeedback.setStutterCount(parseInt(dto.getResults().get("stutter").getValue()));
                audioFeedback.setStutterFeedback(dto.getResults().get("stutter").getFeedback());
            }
            if (dto.getResults().containsKey("content_summary")) {
                audioFeedback.setContentSummary(dto.getResults().get("content_summary").getFeedback());
            }
        }

        audioFeedbackRepository.save(audioFeedback);

        if (audioFeedback.getStatus() == AnalysisStatus.COMPLETED) {
            return Optional.ofNullable(audioFeedback.getReport())
                .map(report -> checkAndGenerateFinalFeedback(report.getId()));
        }
        return Optional.empty();
    }

    // 두 분석이 모두 완료되었는지 확인하고 최종 피드백 생성
    private FullReportResponseDto checkAndGenerateFinalFeedback(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found for id: " + reportId));

        boolean audioCompleted = audioFeedbackRepository.findByReport(report)
                .map(feedback -> feedback.getStatus() == AnalysisStatus.COMPLETED)
                .orElse(false);

        boolean videoCompleted = videoFeedbackRepository.findByReport(report)
                .map(feedback -> feedback.getStatus() == AnalysisStatus.COMPLETED)
                .orElse(false);

        if (audioCompleted && videoCompleted && report.getFinalFeedback() == null) {
            FinalFeedbackRequestDto requestDto = new FinalFeedbackRequestDto();
            requestDto.setPresentationId(report.getPresentation().getPresentationId());
            return finalFeedbackService.createFinalFeedback(requestDto);
        }
        return null;
    }

    private Double parseValue(String value) {
        try {
            return value != null ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
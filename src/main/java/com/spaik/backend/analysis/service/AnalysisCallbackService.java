/*
package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.VoiceFeedback;
import com.spaik.backend.analysis.dto.VideoAnalysisResultDto;
import com.spaik.backend.analysis.dto.VoiceAnalysisResultDto;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import com.spaik.backend.analysis.repository.VoiceFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalysisCallbackService {

    private final VideoFeedbackRepository videoFeedbackRepository;
    private final VoiceFeedbackRepository voiceFeedbackRepository;

    @Transactional
    public void saveVideoResult(VideoAnalysisResultDto dto) {
        VideoFeedback videoFeedback = videoFeedbackRepository.findByAnalysisId(dto.getAnalysisId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid analysisId: " + dto.getAnalysisId()));

        // 상태 업데이트
        videoFeedback.setStatus(
                dto.getStatus() == VideoAnalysisResultDto.Status.COMPLETED
                        ? AnalysisStatus.COMPLETED
                        : AnalysisStatus.PENDING
        );

        if (dto.getResults() != null) {
            // 값 저장
            videoFeedback.setBodyMovement(dto.getResults().getBody_movement().getValue());
            videoFeedback.setBodyMovementValue(parseValue(dto.getResults().getBody_movement().getValue()));
            videoFeedback.setBodyMovementFeedback(dto.getResults().getBody_movement().getFeedback());

            videoFeedback.setGazeOut(dto.getResults().getGaze_out().getValue());
            videoFeedback.setGazeOutValue(parseValue(dto.getResults().getGaze_out().getValue()));
            videoFeedback.setGazeOutFeedback(dto.getResults().getGaze_out().getFeedback());
        }

        videoFeedbackRepository.save(videoFeedback);
    }

    @Transactional
    public void saveVoiceResult(VoiceAnalysisResultDto dto) {
        VoiceFeedback voiceFeedback = voiceFeedbackRepository.findByAnalysisId(dto.getAnalysisId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid analysisId: " + dto.getAnalysisId()));

        // 상태 업데이트
        voiceFeedback.setStatus(
                "COMPLETED".equalsIgnoreCase(dto.getStatus())
                        ? AnalysisStatus.COMPLETED
                        : AnalysisStatus.PENDING
        );

        if (dto.getResults() != null) {
            // 속도
            if (dto.getResults().containsKey("speed")) {
                voiceFeedback.setSpeedValue(parseValue(dto.getResults().get("speed").getValue()));
                voiceFeedback.setSpeedFeedback(dto.getResults().get("speed").getFeedback());
            }
            // 피치
            if (dto.getResults().containsKey("pitch")) {
                voiceFeedback.setPitchValue(parseValue(dto.getResults().get("pitch").getValue()));
                voiceFeedback.setPitchFeedback(dto.getResults().get("pitch").getFeedback());
            }
            // 볼륨
            if (dto.getResults().containsKey("volume")) {
                voiceFeedback.setVolumeDecibels(parseValue(dto.getResults().get("volume").getValue()));
                voiceFeedback.setVolumeFeedback(dto.getResults().get("volume").getFeedback());
            }
            // 더듬기
            if (dto.getResults().containsKey("stutter")) {
                voiceFeedback.setStutterCount(parseInt(dto.getResults().get("stutter").getValue()));
                voiceFeedback.setStutterFeedback(dto.getResults().get("stutter").getFeedback());
            }
            // 내용 요약
            if (dto.getResults().containsKey("content_summary")) {
                voiceFeedback.setContentSummary(dto.getResults().get("content_summary").getFeedback());
            }
        }

        voiceFeedbackRepository.save(voiceFeedback);
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

*/
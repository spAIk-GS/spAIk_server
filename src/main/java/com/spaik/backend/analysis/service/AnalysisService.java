package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.AudioFeedback;
import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisResponseDto;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final VideoFeedbackRepository videoFeedbackRepository;
    private final AudioFeedbackRepository voiceFeedbackRepository;
    private final RestTemplate restTemplate;

    private static final String VIDEO_ANALYSIS_URL = "http://ai-server/analysis/video";
    private static final String VOICE_ANALYSIS_URL = "http://ai-server/analysis/voice";

    @Transactional
    public AnalysisResponseDto requestVideoAnalysis(AnalysisRequestDto dto) {
        String analysisId = "video-" + UUID.randomUUID();

        VideoFeedback videoFeedback = VideoFeedback.builder()
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        videoFeedbackRepository.save(videoFeedback);

        // AI 서버에 분석 요청 (비동기 처리)
        restTemplate.postForEntity(VIDEO_ANALYSIS_URL, dto, Void.class);

        return AnalysisResponseDto.builder()
                .analysisId(analysisId)
                .status("PENDING")
                .build();
    }

    @Transactional
    public AnalysisResponseDto requestVoiceAnalysis(AnalysisRequestDto dto) {
        String analysisId = "voice-" + UUID.randomUUID();

        AudioFeedback voiceFeedback = AudioFeedback.builder()
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        voiceFeedbackRepository.save(voiceFeedback);

        restTemplate.postForEntity(VOICE_ANALYSIS_URL, dto, Void.class);

        return AnalysisResponseDto.builder()
                .analysisId(analysisId)
                .status("PENDING")
                .build();
    }
}

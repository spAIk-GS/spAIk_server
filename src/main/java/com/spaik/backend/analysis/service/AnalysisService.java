package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.AudioFeedback;
import com.spaik.backend.analysis.domain.Report;
import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisResponseDto;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import com.spaik.backend.analysis.repository.ReportRepository;
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
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final ReportRepository reportRepository;
    private final RestTemplate restTemplate;

    //ai 분석 요청 추후 ai서버 주소로 교체
    private static final String AI_ANALYSIS_URL = "http://ai-server/analysis";

    @Transactional
    public AnalysisResponseDto requestAnalysis(AnalysisRequestDto dto) {

        // PresentationId로 Report 조회
        Report report = reportRepository.findByPresentationPresentationId(dto.getPresentationId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        // VideoFeedback 초기 생성
        VideoFeedback videoFeedback = VideoFeedback.builder()
                .report(report)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        videoFeedbackRepository.save(videoFeedback);

        // AudioFeedback 초기 생성
        AudioFeedback audioFeedback = AudioFeedback.builder()
                .report(report)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        audioFeedbackRepository.save(audioFeedback);

        // AI 서버에 요청
        restTemplate.postForEntity(AI_ANALYSIS_URL, dto, Void.class);

        String analysisId = "full-" + UUID.randomUUID();
        return AnalysisResponseDto.builder()
                .analysisId(analysisId)
                .status("PENDING")
                .build();
    }
}

package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.analysis.domain.VideoFeedback;
import com.spaik.backend.analysis.domain.AudioFeedback;
import com.spaik.backend.analysis.domain.Report;
import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisStartRequestDto;
import com.spaik.backend.analysis.dto.AnalysisStartResponseDto;
import com.spaik.backend.analysis.repository.VideoFeedbackRepository;
import com.spaik.backend.analysis.repository.AudioFeedbackRepository;
import com.spaik.backend.analysis.repository.ReportRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final VideoFeedbackRepository videoFeedbackRepository;
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final ReportRepository reportRepository;
    private final RestTemplate restTemplate;

    // AI 서버 분석 요청 URL
    private static final String AI_ANALYSIS_URL = "http://ai-server/analysis";

    @Transactional
    public AnalysisStartResponseDto startAnalysis(AnalysisStartRequestDto startDto) {

        // 1️⃣ PresentationId로 Report 조회
        Report report = reportRepository.findByPresentationPresentationId(startDto.getPresentationId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        // 2️⃣ VideoFeedback 초기 생성
        VideoFeedback videoFeedback = VideoFeedback.builder()
                .report(report)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        videoFeedbackRepository.save(videoFeedback);

        // 3️⃣ AudioFeedback 초기 생성
        AudioFeedback audioFeedback = AudioFeedback.builder()
                .report(report)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        audioFeedbackRepository.save(audioFeedback);

        // 4️⃣ AI 서버 요청을 위한 DTO 생성
        AnalysisRequestDto aiRequestDto = new AnalysisRequestDto();
        aiRequestDto.setPresentationId(startDto.getPresentationId());
        aiRequestDto.setS3Url(generateDownloadUrl(report)); // S3 다운로드 URL 생성 메서드 호출

        // 5️⃣ AI 서버에 분석 요청
        restTemplate.postForEntity(AI_ANALYSIS_URL, aiRequestDto, Void.class);

        // 6️⃣ 프론트에 응답 반환
        return AnalysisStartResponseDto.builder()
                .status("PENDING")
                .build();
    }

    // 예시: Report 기반 S3 다운로드 URL 생성
    private String generateDownloadUrl(Report report) {
        // return s3Service.generatePresignedGetUrl(report.getS3KeyName());
        return "https://example-bucket.s3.amazonaws.com/" + report.getPresentation().getPresentationId();
    }
}

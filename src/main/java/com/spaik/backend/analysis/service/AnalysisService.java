package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.*;
import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisStartRequestDto;
import com.spaik.backend.analysis.dto.AnalysisStartResponseDto;
import com.spaik.backend.analysis.repository.*;
import com.spaik.backend.analysis.s3.S3Service;
import com.spaik.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final PresentationRepository presentationRepository;
    private final ReportRepository reportRepository;
    private final VideoFeedbackRepository videoFeedbackRepository;
    private final AudioFeedbackRepository audioFeedbackRepository;
    private final RestTemplate restTemplate;
    private final S3Service s3Service;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    @Transactional
    public AnalysisStartResponseDto startAnalysis(AnalysisStartRequestDto requestDto, User user) {

        String fileName = requestDto.getFileName();

        // 1️⃣ Presentation 조회 (없으면 생성)
        Presentation presentation = presentationRepository
                .findFirstByUserAndFileNameOrderByCreatedAtDesc(user, fileName)
                .orElseGet(() -> {
                    Presentation newPresentation = Presentation.builder()
                            .presentationId(UUID.randomUUID().toString())
                            .user(user)
                            .fileName(fileName)
                            .s3keyName(fileName)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return presentationRepository.save(newPresentation);
                });

        // 2️⃣ Report 생성 (없으면 생성)
        Report report = reportRepository.findByPresentationPresentationId(presentation.getPresentationId())
                .orElseGet(() -> {
                    Report newReport = Report.builder()
                            .presentation(presentation)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return reportRepository.save(newReport);
                });

        // 3️⃣ VideoFeedback 생성
        VideoFeedback videoFeedback = VideoFeedback.builder()
                .report(report)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        videoFeedbackRepository.save(videoFeedback);

        // 4️⃣ AudioFeedback 생성
        AudioFeedback audioFeedback = AudioFeedback.builder()
                .report(report)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        audioFeedbackRepository.save(audioFeedback);

        // 5️⃣ S3 다운로드 URL 생성
        String s3DownloadUrl = s3Service.generatePresignedGetUrl(presentation.getS3keyName());

        // 6️⃣ AI 서버 요청 DTO 생성
        AnalysisRequestDto aiRequestDto = AnalysisRequestDto.builder()
                .presentationId(presentation.getPresentationId())
                .s3Url(s3DownloadUrl)
                .build();

        // 7️⃣ AI 서버 호출 (비동기일 경우에도 즉시 응답 가능)
        restTemplate.postForEntity(aiServerUrl, aiRequestDto, Void.class);

        // 8️⃣ 프론트 응답 반환
        return AnalysisStartResponseDto.builder()
                .status("PENDING")
                .build();
    }
}

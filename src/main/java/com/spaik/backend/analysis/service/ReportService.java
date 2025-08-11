package com.spaik.backend.analysis.service;

import com.spaik.backend.analysis.domain.Presentation;
import com.spaik.backend.analysis.domain.Report;
import com.spaik.backend.analysis.dto.ReportRequestDto;
import com.spaik.backend.analysis.dto.ReportResponseDto;
import com.spaik.backend.analysis.repository.PresentationRepository;
import com.spaik.backend.analysis.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepo;
    private final PresentationRepository presentationRepo; // Presentation 엔티티를 조회하기 위해 필요

    @Transactional
    public ReportResponseDto createReport(ReportRequestDto requestDto) {
        // 1. Presentation 엔티티 조회 (존재하지 않으면 예외 발생)
        Presentation presentation = presentationRepo.findById(requestDto.getPresentationId())
            .orElseThrow(() -> new IllegalArgumentException("Presentation not found with id: " + requestDto.getPresentationId()));

        // 2. DTO를 Report 엔티티로 변환
        Report report = Report.builder()
            .presentation(presentation)
            .build();

        // 3. 데이터베이스에 저장
        reportRepo.save(report);

        // 4. 저장된 엔티티를 DTO로 변환하여 반환
        return ReportResponseDto.builder()
            .reportId(report.getId())
            .presentationId(report.getPresentation().getPresentationId())
            .createdAt(report.getCreatedAt())
            .build();
    }
}
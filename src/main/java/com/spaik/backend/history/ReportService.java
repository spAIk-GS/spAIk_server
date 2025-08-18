package com.spaik.backend.history;

import com.spaik.backend.analysis.domain.Report;
import com.spaik.backend.history.ReportHistoryDto;
import com.spaik.backend.analysis.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    // 사용자별 Report 히스토리 조회 (페이징)
    public Page<ReportHistoryDto> getReportsByUserId(Long userId, Pageable pageable) {
        return reportRepository.findByPresentationUserId(userId, pageable)
                .map(report -> ReportHistoryDto.builder()
                        .reportId(report.getId()) 
                        .presentationId(report.getPresentation().getPresentationId())
                        .createdAt(report.getCreatedAt())
                        .build()
                );
    }
}


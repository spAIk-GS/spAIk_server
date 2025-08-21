package com.spaik.backend.history;

import com.spaik.backend.analysis.domain.Report;
import com.spaik.backend.history.ReportHistoryDto;
import com.spaik.backend.analysis.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.spaik.backend.analysis.s3.S3Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final S3Service s3Service;

    public Page<ReportHistoryDto> getReportsByUserId(Long userId, Pageable pageable) {
        return reportRepository.findByPresentationUserId(userId, pageable)
                .map(report -> {
                    // 1. 썸네일 파일 이름을 생성합니다. (기존 로직 활용)
                    String originalFileName = report.getPresentation().getFileName();
                    String thumbnailKey = generateThumbnailName(originalFileName);

                    // 2. S3 서비스로 Pre-signed URL을 생성합니다.
                    String thumbnailUrl = s3Service.generatePresignedGetUrl(thumbnailKey);

                    // 3. DTO를 빌드할 때 thumbnailUrl을 포함시킵니다.
                    return ReportHistoryDto.builder()
                            .reportId(report.getId())
                            .presentationId(report.getPresentation().getPresentationId())
                            .createdAt(report.getCreatedAt())
                            .thumbnailUrl(thumbnailUrl) // 👈 생성된 URL 추가
                            .build();
                });
    }

    // 썸네일 파일 이름을 생성하는 헬퍼 메소드 (기존 다른 서비스에 있던 것을 가져오거나 새로 만듭니다)
    private String generateThumbnailName(String originalFileName) {
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        return baseName + "_thumbnail.jpeg";
    }
}


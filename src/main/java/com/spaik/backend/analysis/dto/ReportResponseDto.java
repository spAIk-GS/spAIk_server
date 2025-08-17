package com.spaik.backend.analysis.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponseDto {
    private Long reportId;
    private String presentationId;
    private LocalDateTime createdAt;
}
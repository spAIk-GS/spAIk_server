package com.spaik.backend.analysis.dto;

import lombok.Builder;
import lombok.Getter;

// 서버 → 프론트 (분석 시작 응답)
@Getter
@Builder
public class AnalysisStartResponseDto {
    private String status; // PENDING
    private String presentationId;
}

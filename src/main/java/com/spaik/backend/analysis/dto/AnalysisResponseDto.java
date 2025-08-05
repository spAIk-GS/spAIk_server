package com.spaik.backend.analysis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponseDto {
    private String analysisId;
    private String status; // PENDING
}

package com.spaik.backend.analysis.dto;

import lombok.Builder;
import lombok.Getter;

// 최종 분석 결과를 모두 담아 프론트엔드로 보낼 DTO
@Getter
@Builder
public class FinalAnalysisResultDto {
    private ReportResponseDto summary; // 종합 피드백 (Gemini 결과)
    private AnalysisCallbackDto details; // 상세 분석 데이터 (Audio/Video 결과)
}
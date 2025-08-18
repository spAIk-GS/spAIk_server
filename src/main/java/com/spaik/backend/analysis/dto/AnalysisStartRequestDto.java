package com.spaik.backend.analysis.dto;

import lombok.Getter;
import lombok.Setter;

// 프론트 → 서버 (분석 시작 요청)
@Getter
@Setter
public class AnalysisStartRequestDto {
    private String fileName;
}

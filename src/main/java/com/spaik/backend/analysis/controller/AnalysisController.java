package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisResponseDto;
import com.spaik.backend.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    // 프론트에서 분석 시작 요청
    @PostMapping("/start")
    public ResponseEntity<AnalysisResponseDto> startAnalysis(@RequestBody AnalysisRequestDto dto) {
        AnalysisResponseDto response = analysisService.requestAnalysis(dto);
        return ResponseEntity.ok(response);
    }
}

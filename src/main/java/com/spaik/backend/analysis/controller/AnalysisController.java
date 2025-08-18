package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.AnalysisStartRequestDto;
import com.spaik.backend.analysis.dto.AnalysisStartResponseDto;
import com.spaik.backend.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/start")
    public ResponseEntity<AnalysisStartResponseDto> startAnalysis(@RequestBody AnalysisStartRequestDto dto) {
        AnalysisStartResponseDto response = analysisService.startAnalysis(dto);
        return ResponseEntity.ok(response);
    }
}

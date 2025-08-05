package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisResponseDto;
import com.spaik.backend.analysis.service.AnalysisService;
import com.spaik.backend.report.entity.Report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/video")
    public AnalysisResponseDto requestVideoAnalysis(@RequestBody AnalysisRequestDto dto) {
        return analysisService.requestVideoAnalysis(dto);
    }

    @PostMapping("/voice")
    public AnalysisResponseDto requestVoiceAnalysis(@RequestBody AnalysisRequestDto dto) {
        return analysisService.requestVoiceAnalysis(dto);
    }
}

package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.AnalysisRequestDto;
import com.spaik.backend.analysis.dto.AnalysisResponseDto;
import com.spaik.backend.analysis.service.AnalysisService;
import com.spaik.backend.analysis.dto.VideoAnalysisResultDto;
import com.spaik.backend.analysis.dto.AudioAnalysisResultDto;
import com.spaik.backend.analysis.domain.Report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/video")
    public AnalysisResponseDto requestVideoAnalysis(@RequestBody AnalysisRequestDto dto) {
        return analysisService.requestVideoAnalysis(dto);
    }

    @PostMapping("/audio")
    public AnalysisResponseDto requestVoiceAnalysis(@RequestBody AnalysisRequestDto dto) {
        return analysisService.requestVoiceAnalysis(dto);
    }

}


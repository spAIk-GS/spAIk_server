package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.AnalysisStartRequestDto;
import com.spaik.backend.analysis.dto.AnalysisStartResponseDto;
import com.spaik.backend.analysis.service.AnalysisService;
import com.spaik.backend.auth.jwt.CustomUserDetails;
import com.spaik.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping("/start")
    public ResponseEntity<AnalysisStartResponseDto> startAnalysis(
            @RequestBody AnalysisStartRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails principal) {

        User user = principal.getUser();
        AnalysisStartResponseDto response = analysisService.startAnalysis(dto, user);
        return ResponseEntity.ok(response);
    }

}

package com.spaik.backend.analysis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spaik.backend.analysis.dto.AnalysisCallbackDto;
import com.spaik.backend.analysis.dto.ReportResponseDto;
import com.spaik.backend.analysis.service.CallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class CallbackController {

    private final CallbackService callbackService;

    // AI 서버 콜백 수신
    @PostMapping("/callback")
    public ResponseEntity<ReportResponseDto> receiveFullAnalysisResult(@RequestBody AnalysisCallbackDto dto) throws JsonProcessingException {
        Optional<ReportResponseDto> responseDto = callbackService.saveAnalysisResult(dto); // ← 여기
        return responseDto.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.noContent().build());
    }
}

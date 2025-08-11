package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.VideoAnalysisResultDto;
import com.spaik.backend.analysis.dto.AudioAnalysisResultDto;
import com.spaik.backend.analysis.dto.FullReportResponseDto;
import com.spaik.backend.analysis.service.CallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/analysis/callback")
@RequiredArgsConstructor
public class CallbackController {

    private final CallbackService callbackService;

    @PostMapping("/video")
    public ResponseEntity<FullReportResponseDto> receiveVideoAnalysisResult(@RequestBody VideoAnalysisResultDto dto) {
        Optional<FullReportResponseDto> responseDto = callbackService.saveVideoResult(dto);
        return responseDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/audio")
    public ResponseEntity<FullReportResponseDto> receiveAudioAnalysisResult(@RequestBody AudioAnalysisResultDto dto) {
        Optional<FullReportResponseDto> responseDto = callbackService.saveAudioResult(dto);
        return responseDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
}
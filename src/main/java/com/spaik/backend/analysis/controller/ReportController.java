package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.ReportRequestDto;
import com.spaik.backend.analysis.dto.ReportResponseDto;
import com.spaik.backend.analysis.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto requestDto) {
        // DTO를 서비스에 전달하여 Report 생성
        ReportResponseDto responseDto = reportService.createReport(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
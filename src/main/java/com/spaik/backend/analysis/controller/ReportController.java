package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.dto.ReportRequestDto;
import com.spaik.backend.analysis.dto.ReportResponseDto;
import com.spaik.backend.analysis.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report") 
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{presentation_id}")
    public ResponseEntity<ReportResponseDto> createReport(@PathVariable("presentation_id") Long presentationId) {
        // DTO를 서비스에 전달하여 Report 생성
        ReportRequestDto requestDto = new ReportRequestDto();
        requestDto.setPresentationId(presentationId);

        ReportResponseDto responseDto = reportService.createReport(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}

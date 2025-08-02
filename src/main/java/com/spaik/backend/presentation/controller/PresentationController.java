package com.spaik.backend.presentation.controller;

import com.spaik.backend.presentation.service.PresentationService;
import com.spaik.backend.presentation.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presentations")
@RequiredArgsConstructor
public class PresentationController {

    private final PresentationService presentationService;

    // 리포트 조회
    @GetMapping("/report/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(presentationService.getReport(id));
    }
}

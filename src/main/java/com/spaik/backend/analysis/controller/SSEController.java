package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.service.SSEService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class SSEController {

    private final SSEService sseService;

    // SSE 구독
    @GetMapping("/subscribe/{presentationId}")
    public SseEmitter subscribe(@PathVariable String presentationId) {
        return sseService.createEmitter(presentationId);
    }
}

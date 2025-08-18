package com.spaik.backend.analysis.controller;

import com.spaik.backend.analysis.service.SSEService;
import com.spaik.backend.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class SSEController {

    private final SSEService sseService;
    private final JwtTokenProvider jwtTokenProvider;

    // SSE 구독
    @GetMapping("/subscribe/{presentationId}")
    public SseEmitter subscribe(
            @PathVariable String presentationId,
            @RequestParam("token") String token
    ) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }
        // 토큰에서 사용자 이메일 추출 가능
        String email = jwtTokenProvider.getEmailFromToken(token);
        System.out.println("SSE 연결: " + email + " - " + presentationId);

        return sseService.createEmitter(presentationId);
    }
}

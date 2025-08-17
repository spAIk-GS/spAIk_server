package com.spaik.backend.analysis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SSEService {

    // presentationId 기준으로 Emitter 저장
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 프론트 연결 시 Emitter 생성
    public SseEmitter createEmitter(String presentationId) {
        SseEmitter emitter = new SseEmitter(0L); // timeout 무제한
        emitters.put(presentationId, emitter);

        emitter.onCompletion(() -> emitters.remove(presentationId));
        emitter.onTimeout(() -> emitters.remove(presentationId));

        return emitter;
    }

    // 프론트로 이벤트 전송
    public void sendUpdate(String presentationId, Object data) {
        SseEmitter emitter = emitters.get(presentationId);
        if (emitter != null) {
            try {
                emitter.send(data);
            } catch (IOException e) {
                log.error("SSE 전송 실패: {}", e.getMessage());
                emitters.remove(presentationId);
            }
        }
    }
}

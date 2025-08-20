package com.spaik.backend.analysis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class SSEService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // 하트비트를 위한 스케줄러 추가
    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

    public SseEmitter createEmitter(String presentationId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(presentationId, emitter);
        log.info("New emitter created for {}. Total emitters: {}", presentationId, emitters.size());

        // 하트비트(Heartbeat) 스케줄러 추가
        final Runnable heartbeatTask = () -> {
            try {
                // 클라이언트에서 무시되는 주석(comment)을 보내 연결을 유지
                emitter.send(SseEmitter.event().comment("heartbeat"));
                log.trace("Heartbeat sent for {}", presentationId); // trace 레벨로 로그 변경
            } catch (IOException e) {
                log.warn("Failed to send heartbeat for {}, closing emitter.", presentationId);
                emitter.complete(); // 에러 발생 시 emitter를 완료시켜 정리되도록 함
            }
        };

        // 15초마다 하트비트 작업을 예약
        final ScheduledFuture<?> heartbeatFuture = this.heartbeatExecutor.scheduleAtFixedRate(heartbeatTask, 15, 15, TimeUnit.SECONDS);

        // 연결이 종료되면(onCompletion, onTimeout) 하트비트 작업도 중지
        Runnable cleanupTask = () -> {
            log.info("SSE cleanup for: {}. Stopping heartbeat.", presentationId);
            heartbeatFuture.cancel(true);
            emitters.remove(presentationId);
            log.info("Emitter removed for {}. Total emitters: {}", presentationId, emitters.size());
        };

        emitter.onCompletion(cleanupTask);
        emitter.onTimeout(cleanupTask);

        // 최초 연결 성공 메시지 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE connection established. Waiting for analysis results..."));
            log.info("✅ SSE initial event sent for: {}", presentationId);
        } catch (IOException e) {
            log.error("❌ Failed to send initial SSE event for {}: {}", presentationId, e.getMessage());
            emitter.complete(); // 실패 시 즉시 정리
        }

        return emitter;
    }

    public void sendUpdate(String presentationId, Object data) {
        SseEmitter emitter = emitters.get(presentationId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(data));
                log.info("📩 SSE data sent to {}", presentationId);
            } catch (IOException e) {
                log.error("❌ Failed to send SSE data for {}: {}", presentationId, e.getMessage());
                emitter.complete(); // 실패 시 즉시 정리
            }
        } else {
            log.warn("Emitter not found for presentationId: {}, could not send update.", presentationId);
        }
    }
}

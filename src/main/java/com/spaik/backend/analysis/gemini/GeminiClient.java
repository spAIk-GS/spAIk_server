package com.spaik.backend.analysis.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiClient {

    private final RestClient geminiRestClient;
    private final ObjectMapper objectMapper; // RetryInfo 파싱용

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String MODEL_PATH = "/v1beta/models/gemini-1.5-pro-latest:generateContent";

    /** ✅ 권장: 분석 전체 JSON(analysisCallbackDto 직렬화)을 한 번만 보내는 버전 */
    public String requestFinalFeedback(String payload) {
        Map<String, Object> requestBody = buildRequestBody(payload);
        return callGeminiWithRetry(requestBody, 3);
    }

    /** (이전 호환용) 기존 2-인자 버전은 단일 인자로 위임 */
    @Deprecated
    public String requestFinalFeedback(String audioJson, String videoJson) {
        String combined = "{\"audio\":" + audioJson + ",\"video\":" + videoJson + "}";
        return requestFinalFeedback(combined);
    }

    /** parts: '지시문 1개' + '데이터 JSON 1개' */
    private Map<String, Object> buildRequestBody(String payload) {
        String prompt = """
            당신은 사용자의 발표/면접을 분석하여 전문적인 피드백을 제공하는 발표 코치입니다.
            아래 JSON 분석 데이터를 바탕으로:
            1) 발표의 강점(간결/구체), 2) 개선점, 3) 실천 팁(행동/연습 중심),
            4) 참고 자료/훈련 방법을 한국어로 작성하세요.
            """;

        return Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt),
                                        Map.of("text", payload)
                                )
                        )
                )
        );
    }

    /** 429(쿼터/레이트리밋) 시 RetryInfo 기반 백오프 재시도 */
    private String callGeminiWithRetry(Map<String, Object> requestBody, int maxAttempts) {
        log.info("[Gemini API] 현재 사용 중인 API 키의 끝 5자리: ...{}",
                geminiApiKey.substring(geminiApiKey.length() - 5));
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                // ▼▼▼ 1. 요청 직전 로그 ▼▼▼
                log.info("[Gemini API] 요청 시작 (시도: {}/{})", attempt, maxAttempts);
                // log.debug("[Gemini API] 요청 본문: {}", objectMapper.writeValueAsString(requestBody)); // (필요 시) 전체 요청 내용을 보려면 이 로그의 주석을 푸세요.

                String response = geminiRestClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path(MODEL_PATH)
                                .queryParam("key", geminiApiKey)
                                .build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .retrieve()
                        .body(String.class);

                // ▼▼▼ 2. 성공 응답 로그 ▼▼▼
                log.info("[Gemini API] 응답 성공!");
                // log.debug("[Gemini API] 응답 내용 (Raw): {}", response); // (필요 시) 전체 응답 내용을 보려면 이 로그의 주석을 푸세요.

                return response;

            } catch (HttpClientErrorException.TooManyRequests e) {
                // ▼▼▼ 3. 특정 오류(429) 로그 ▼▼▼
                long delayMs = parseRetryDelayMs(e.getResponseBodyAsString())
                        .orElse(4000L * attempt);
                log.warn("[Gemini API] 오류 (429 Too Many Requests): {}ms 후 재시도합니다.", delayMs);

                sleep(delayMs);
                if (attempt >= maxAttempts) {
                    log.error("[Gemini API] 최대 재시도 횟수({})를 초과했습니다.", maxAttempts);
                    throw e;
                }

            } catch (Exception e) {
                // ▼▼▼ 4. 그 외 모든 오류 로그 ▼▼▼
                log.error("[Gemini API] 처리 중 심각한 오류 발생 (시도: {}): ", attempt, e);
                throw new RuntimeException("Gemini API 호출에 실패했습니다.", e);
            }
        }
    }

    /** Google RPC RetryInfo 파싱: "retryDelay": "38s" → 밀리초 */
    private Optional<Long> parseRetryDelayMs(String errorBody) {
        try {
            JsonNode root = objectMapper.readTree(errorBody);
            for (JsonNode d : root.path("error").path("details")) {
                if ("type.googleapis.com/google.rpc.RetryInfo".equals(d.path("@type").asText())) {
                    String s = d.path("retryDelay").asText(); // ex) "38s" or "0.5s"
                    if (s.endsWith("s")) {
                        double sec = Double.parseDouble(s.substring(0, s.length() - 1));
                        return Optional.of((long) (sec * 1000));
                    }
                }
            }
        } catch (Exception ignore) {}
        return Optional.empty();
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }
}

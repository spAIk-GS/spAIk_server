package com.spaik.backend.analysis.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceAnalysisResultDto {

    private String analysisId;
    private Status status; // enum 타입으로 변경

    private String videoId; // 완료 시에만 존재
    private Map<String, FeedbackResult> results; // speed, pitch 등

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeedbackResult {
        private String feedback;
        private String value;
    }

    public enum Status {
        PENDING,
        COMPLETED
    }
}

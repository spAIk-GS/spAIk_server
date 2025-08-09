package com.spaik.backend.analysis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoAnalysisResultDto {

    private String analysisId;
    private String videoId; // PENDING일 경우 null 가능
    private Status status; // PENDING, COMPLETED

    private Results results; // 완료 시에만 값 존재

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Results {
        private FeedbackItem body_movement;
        private FeedbackItem gaze_out;
        private String content_summary;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeedbackItem {
        private String feedback;
        private String value;
    }

    public enum Status {
        PENDING,
        COMPLETED
    }
}

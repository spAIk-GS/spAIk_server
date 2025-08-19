package com.spaik.backend.analysis.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AnalysisCallbackDto {
    private String presentationId;
    private AudioAnalysis audio;
    private VideoAnalysis video;

    @Data
    public static class AudioAnalysis {
        private String analysisId;
        private String status;
        private Map<String, AnalysisResult> results; // speed, pitch, volume, stutter
    }

    @Data
    public static class VideoAnalysis {
        private String analysisId;
        private String status;
        private Map<String, AnalysisResult> results; // movement, gaze
    }

    @Data
    public static class AnalysisResult {
        private String emotion; // "좋음", "보통", "나쁨"

        // 공통 세그먼트
        private List<Segment> segments;

        // audio/video 추가 필드
        private String feedback;           // speed, pitch, volume, stutter 공통
        private Double value;              // speed, pitch 값
        private Double decibels;           // volume 전용
        private List<String> volume_anomalies; // volume 전용

        private Integer stutter_count;     // stutter 전용
        private List<StutterDetail> stutter_details; // stutter 전용
    }

    @Data
    public static class Segment {
        private double start_time_sec;
        private double end_time_sec;

        // audio 측정값
        private Double value;            // speed, pitch 등 값
        private Double movement_percent; // video movement 전용
        private Double focus_level;      // video gaze 전용
    }

    @Data
    public static class StutterDetail {
        private String sentence;
        private List<String> timestamps;
        private List<String> stutter_words;
    }
}

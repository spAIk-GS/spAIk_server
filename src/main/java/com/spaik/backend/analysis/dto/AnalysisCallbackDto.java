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
        private String emotion;             // "좋음", "보통", "나쁨"
        private Double movement_percent; // ← 추가: movement 결과 레벨
        private Double focus_level;      // ← 추가: gaze 결과 레벨
        // 공통 세그먼트
        private List<Segment> segments;

        // Audio 전용
        private Double value;               // speed, pitch
        private Double decibels;            // volume
        private List<String> volume_anomalies; // volume
        private Integer stutter_count;      // stutter
        private List<StutterDetail> stutter_details; // stutter
    }

    @Data
    public static class Segment {
        private double start_time_sec;
        private double end_time_sec;

        // audio/video 측정값
        private Double value;            // speed, pitch
        private Double movement_percent; // video movement
        private Double focus_level;      // video gaze
    }

    @Data
    public static class StutterDetail {
        private String sentence;
        private List<String> timestamps;
        private List<String> stutter_words;
    }
}

package com.spaik.backend.analysis.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoFeedbackId;

    @Column(name = "analysis_id_video", unique = true)
    private String analysisIdVideo;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message; // 실패 시 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    /** 분석 결과 */
    @Column(name = "movement_emotion")
    private String movementEmotion;
    @Column(name = "gaze_emotion")
    private String gazeEmotion;

    @Column(name = "movement_segments", columnDefinition = "TEXT")
    private String movementSegmentsJson;
    @Column(name = "gaze_segments", columnDefinition = "TEXT")
    private String gazeSegmentsJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

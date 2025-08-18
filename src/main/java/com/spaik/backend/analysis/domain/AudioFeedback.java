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
public class AudioFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long audioFeedbackId;

    @Column(name = "analysis_id_audio", unique = true)
    private String analysisIdAudio;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message; // 실패 시 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    /** 분석 결과 */
    @Column(name = "speed_emotion")
    private String speedEmotion; // "좋음", "보통", "부족"

    @Column(name = "speed_segments", columnDefinition = "TEXT")
    private String speedSegmentsJson; // segments JSON 저장

    @Column(name = "pitch_emotion")
    private String pitchEmotion;

    @Column(name = "pitch_segments", columnDefinition = "TEXT")
    private String pitchSegmentsJson;

    @Column(name = "volume_emotion")
    private String volumeEmotion;

    @Column(name = "volume_segments", columnDefinition = "TEXT")
    private String volumeSegmentsJson;

    @Column(name = "stutter_emotion")
    private String stutterEmotion;

    @Column(name = "stutter_segments", columnDefinition = "TEXT")
    private String stutterSegmentsJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

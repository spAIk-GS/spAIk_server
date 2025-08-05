// 음성 피드백 분석 결과를 저장하는 엔티티

package com.spaik.backend.analysis.domain;

import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.report.entity.Report;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voiceFeedbackId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(name = "speed_value")
    private Double speedValue;

    @Column(name = "speed_feedback", columnDefinition = "TEXT")
    private String speedFeedback;

    @Column(name = "pitch_value")
    private Double pitchValue;

    @Column(name = "pitch_feedback", columnDefinition = "TEXT")
    private String pitchFeedback;

    @Column(name = "volume_decibels")
    private Double volumeDecibels;

    @Column(name = "volume_feedback", columnDefinition = "TEXT")
    private String volumeFeedback;

    @Column(name = "stutter_count")
    private Integer stutterCount;

    @Column(name = "stutter_feedback", columnDefinition = "TEXT")
    private String stutterFeedback;

    @Column(name = "content_summary", columnDefinition = "TEXT")
    private String contentSummary;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

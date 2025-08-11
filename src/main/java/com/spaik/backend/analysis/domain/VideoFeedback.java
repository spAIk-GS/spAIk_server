// 영상 피드백 분석 결과를 저장하는 엔티티

package com.spaik.backend.analysis.domain;
import com.spaik.backend.analysis.domain.AnalysisStatus;
import com.spaik.backend.analysis.domain.Report;

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
    @Column(name = "video_feedback_id")
    private Long id;

    @Column(name = "analysis_id", unique = true)
    private String analysisId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    // reportId 대신 Report 엔티티를 직접 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "body_movement")
    private String bodyMovement;

    @Column(name = "gaze_out")
    private String gazeOut;

    @Column(name = "body_movement_value")
    private Double bodyMovementValue;

    @Column(name = "body_movement_feedback", columnDefinition = "TEXT")
    private String bodyMovementFeedback;

    @Column(name = "gaze_out_value")
    private Double gazeOutValue;

    @Column(name = "gaze_out_feedback", columnDefinition = "TEXT")
    private String gazeOutFeedback;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}

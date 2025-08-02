// 영상 피드백 분석 결과를 저장하는 엔티티

package com.spaik.backend.presentation.domain;

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

    // reportId 대신 Report 엔티티를 직접 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "body_movement")
    private String bodyMovement;

    @Column(name = "gaze_out")
    private String gazeOut;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

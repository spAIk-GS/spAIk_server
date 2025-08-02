// 음성 피드백 분석 결과를 저장하는 엔티티

package com.spaik.backend.presentation.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    private String type; // "speed", "pitch", "scatter"

    private String startTime;

    private String endTime;

    private String value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

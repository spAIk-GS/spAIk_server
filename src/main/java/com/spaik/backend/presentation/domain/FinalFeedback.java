// 최종 피드백 종합 결과를 저장하는 엔티티

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
public class FinalFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "final_feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "final_feedback", columnDefinition = "TEXT")
    private String finalFeedback;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

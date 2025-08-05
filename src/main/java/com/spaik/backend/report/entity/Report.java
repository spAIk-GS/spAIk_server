// 분석 보고서를 나타내는 엔티티

package com.spaik.backend.report.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.spaik.backend.analysis.domain.Presentation;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentation_id", nullable = false)
    private Presentation presentation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

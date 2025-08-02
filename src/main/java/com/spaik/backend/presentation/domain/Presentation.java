// 업로드된 발표 영상을 나타내는 도메인 엔티티

package com.spaik.backend.presentation.domain;
import com.spaik.backend.user.entity.User;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "presentation")
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presentation_id")
    private Long presentationId;

    // User 엔티티와 다대일 관계, 외래키 user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(name = "s3_video_url", nullable = false)
    private String s3VideoUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
}

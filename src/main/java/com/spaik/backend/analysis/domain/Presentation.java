package com.spaik.backend.analysis.domain;

import com.spaik.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "presentation")
public class Presentation {

    @Id
    @Column(name = "presentation_id")
    private String presentationId;

    // User 엔티티와 다대일 관계, 외래키 user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "s3_keyname", nullable = false)
    private String s3keyName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (this.presentationId == null) {
            this.presentationId = UUID.randomUUID().toString(); // UUID로 ID 자동 생성
        }
        this.createdAt = LocalDateTime.now();
    }
}

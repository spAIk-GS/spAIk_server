package com.spaik.backend.analysis.domain;

import com.spaik.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    // User와 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "s3_keyname", nullable = false)
    private String s3keyName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Presentation 삭제 시 관련 Report도 삭제
    @OneToMany(mappedBy = "presentation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.presentationId == null) {
            this.presentationId = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }
}

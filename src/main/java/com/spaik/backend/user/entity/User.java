package com.spaik.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.spaik.backend.analysis.domain.Presentation;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;  // ex: USER, ADMIN

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;  // ex: LOCAL, GOOGLE

    @Column(name = "provider_id")
    private String providerId;  // Google의 sub 값

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // User 삭제 시 관련 Presentation도 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Presentation> presentations;
}

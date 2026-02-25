package com.library.digitallibrary.auth;

import com.library.digitallibrary.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "EMAIL" or "PHONE"
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Instant expiresAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}

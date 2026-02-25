package com.library.digitallibrary.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Display name / username
    @Column(nullable = false)
    private String username;

    // Optional: email or phone (one of them can be null)
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    // Hashed password later (we'll do BCrypt in Auth step)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // PENDING until admin approves
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    // Confirmation flags
    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private boolean phoneVerified;

    // Profile picture url/path
    private String profileImageUrl;
}

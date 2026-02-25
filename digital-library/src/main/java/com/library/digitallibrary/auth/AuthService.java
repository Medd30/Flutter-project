package com.library.digitallibrary.auth;

import com.library.digitallibrary.auth.dto.*;
import com.library.digitallibrary.auth.repo.VerificationTokenRepository;
import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.Role;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public Long registerEmail(RegisterEmailRequest req) {
        if (userRepository.existsByEmail(req.email)) {
            throw new RuntimeException("Email already used");
        }

        User user = User.builder()
                .username(req.username)
                .email(req.email)
                .phone(null)
                .password(passwordEncoder.encode(req.password))
                .role(Role.USER)
                .status(AccountStatus.PENDING)
                .emailVerified(false)
                .phoneVerified(false)
                .profileImageUrl(null)
                .build();

        user = userRepository.save(user);

        String code = generate6DigitCode();

        tokenRepository.deleteByUserIdAndType(user.getId(), "EMAIL");

        tokenRepository.save(VerificationToken.builder()
                .user(user)
                .type("EMAIL")
                .code(code)
                .expiresAt(Instant.now().plusSeconds(10 * 60))
                .build());

// 🔥 SEND REAL EMAIL
        emailService.sendVerificationEmail(user.getEmail(), code);

        return user.getId();
    }

    public Long registerPhone(RegisterPhoneRequest req) {
        if (userRepository.existsByPhone(req.phone)) {
            throw new RuntimeException("Phone already used");
        }

        User user = User.builder()
                .username(req.username)
                .email(null)
                .phone(req.phone)
                .password(passwordEncoder.encode(req.password))
                .role(Role.USER)
                .status(AccountStatus.PENDING)
                .emailVerified(false)
                .phoneVerified(false)
                .profileImageUrl(null)
                .build();

        user = userRepository.save(user);

        String code = generate6DigitCode();
        tokenRepository.deleteByUserIdAndType(user.getId(), "PHONE");
        tokenRepository.save(VerificationToken.builder()
                .user(user)
                .type("PHONE")
                .code(code)
                .expiresAt(Instant.now().plusSeconds(10 * 60))
                .build());

        return user.getId();
    }

    @Transactional
    public void verifyEmail(VerifyRequest req) {
        verify(req.userId, "EMAIL", req.code);
        User user = userRepository.findById(req.userId).orElseThrow();
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void verifyPhone(VerifyRequest req) {
        verify(req.userId, "PHONE", req.code);
        User user = userRepository.findById(req.userId).orElseThrow();
        user.setPhoneVerified(true);
        userRepository.save(user);
    }


    private void verify(Long userId, String type, String code) {
        VerificationToken token = tokenRepository.findByUserIdAndTypeAndCode(userId, type, code)
                .orElseThrow(() -> new RuntimeException("Invalid code"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Code expired");
        }

        tokenRepository.deleteByUserIdAndType(userId, type);
    }

    public AuthResponse login(LoginRequest req) {
        Optional<User> byEmail = userRepository.findByEmail(req.identifier);
        Optional<User> byPhone = userRepository.findByPhone(req.identifier);

        User user = byEmail.or(() -> byPhone).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // must be verified
        boolean verified = (user.getEmail() != null && user.isEmailVerified())
                || (user.getPhone() != null && user.isPhoneVerified());

        if (!verified) {
            throw new RuntimeException("Account not verified yet");
        }

        if (user.getStatus() == AccountStatus.DELETED) {
            throw new RuntimeException("Account deleted");
        }

        // must be approved by admin
        if (user.getStatus() != AccountStatus.APPROVED) {
            throw new RuntimeException("Account not approved yet");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());
        return AuthResponse.builder()
                .userId(user.getId())
                .token(token)
                .role(user.getRole().name())
                .build();
    }

    public String getLatestCode(Long userId, String type) {
        return tokenRepository.findTopByUserIdAndTypeOrderByIdDesc(userId, type)
                .map(VerificationToken::getCode)
                .orElse(null);
    }

    private String generate6DigitCode() {
        int n = 100000 + new Random().nextInt(900000);
        return String.valueOf(n);
    }
}

package com.library.digitallibrary.auth;

import com.library.digitallibrary.auth.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/email")
    public Map<String, Object> registerEmail(@Valid @RequestBody RegisterEmailRequest req) {
        Long userId = authService.registerEmail(req);

        return Map.of(
                "userId", userId,
                "message", "Verification email sent."
        );
    }

    @PostMapping("/register/phone")
    public Map<String, Object> registerPhone(@Valid @RequestBody RegisterPhoneRequest req) {
        Long userId = authService.registerPhone(req);

        // DEV ONLY: show code to test quickly
        String code = authService.getLatestCode(userId, "PHONE");

        return Map.of(
                "userId", userId,
                "message", "Registered. Verify your phone.",
                "devCode", code
        );
    }

    @PostMapping("/verify/email")
    public Map<String, Object> verifyEmail(@Valid @RequestBody VerifyRequest req) {
        authService.verifyEmail(req);
        return Map.of("message", "Email verified. Wait for admin approval.");
    }

    @PostMapping("/verify/phone")
    public Map<String, Object> verifyPhone(@Valid @RequestBody VerifyRequest req) {
        authService.verifyPhone(req);
        return Map.of("message", "Phone verified. Wait for admin approval.");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}

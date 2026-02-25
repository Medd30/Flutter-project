package com.library.digitallibrary.me;

import com.library.digitallibrary.me.dto.ChangePasswordRequest;
import com.library.digitallibrary.me.dto.MeProfileResponse;
import com.library.digitallibrary.me.dto.UpdateMeProfileRequest;
import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me/profile")
@RequiredArgsConstructor
public class MeProfileController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public MeProfileResponse me() {
        Long userId = CurrentUser.requireUserId();
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (u.getStatus() == AccountStatus.DELETED) {
            throw new RuntimeException("Account deleted");
        }

        return MeProfileResponse.from(u);
    }

    @PatchMapping
    public MeProfileResponse update(@RequestBody UpdateMeProfileRequest req) {
        Long userId = CurrentUser.requireUserId();
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (u.getStatus() == AccountStatus.DELETED) {
            throw new RuntimeException("Account deleted");
        }

        if (req.getUsername() != null) {
            String username = req.getUsername().trim();
            if (username.isEmpty()) throw new RuntimeException("Username required");
            u.setUsername(username);
        }

        if (req.getProfileImageUrl() != null) {
            String url = req.getProfileImageUrl().trim();
            u.setProfileImageUrl(url.isEmpty() ? null : url);
        }

        userRepository.save(u);
        return MeProfileResponse.from(u);
    }

    @PostMapping("/password")
    public void changePassword(@RequestBody ChangePasswordRequest req) {
        Long userId = CurrentUser.requireUserId();
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (u.getStatus() == AccountStatus.DELETED) {
            throw new RuntimeException("Account deleted");
        }

        String current = req.getCurrentPassword() == null ? "" : req.getCurrentPassword();
        String next = req.getNewPassword() == null ? "" : req.getNewPassword();

        if (current.isBlank() || next.isBlank()) {
            throw new RuntimeException("Passwords are required");
        }
        if (next.length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters");
        }

        if (!passwordEncoder.matches(current, u.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        u.setPassword(passwordEncoder.encode(next));
        userRepository.save(u);
    }
}
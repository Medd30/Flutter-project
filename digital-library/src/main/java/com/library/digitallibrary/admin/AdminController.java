package com.library.digitallibrary.admin;

import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    // list all pending users
    @GetMapping("/pending-users")
    public List<User> pendingUsers() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getStatus() == AccountStatus.PENDING)
                .toList();
    }

    // approve user
    @PostMapping("/approve/{userId}")
    public String approve(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(AccountStatus.APPROVED);
        userRepository.save(user);

        return "User approved";
    }

    // reject user
    @PostMapping("/reject/{userId}")
    public String reject(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(AccountStatus.REJECTED);
        userRepository.save(user);

        return "User rejected";
    }
}

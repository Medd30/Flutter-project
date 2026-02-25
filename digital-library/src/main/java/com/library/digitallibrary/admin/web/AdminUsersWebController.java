package com.library.digitallibrary.admin.web;

import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.Role;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUsersWebController {

    private final UserRepository userRepository;

    @GetMapping
    public String listUsers(
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) String q,
            Model model
    ) {
        List<User> users = userRepository.findAll();

        boolean hasStatus = status != null;
        boolean hasQuery = q != null && !q.isBlank();

        // ✅ default behavior: hide DELETED users unless admin explicitly requested status=DELETED
        if (!hasStatus) {
            users = users.stream()
                    .filter(u -> u.getStatus() != AccountStatus.DELETED)
                    .toList();
        }

        // ✅ status filter (if provided, including DELETED)
        if (hasStatus) {
            users = users.stream()
                    .filter(u -> u.getStatus() == status)
                    .toList();
        }

        // ✅ search filter (username/email/phone)
        if (hasQuery) {
            String s = q.trim().toLowerCase();
            users = users.stream().filter(u ->
                    (u.getUsername() != null && u.getUsername().toLowerCase().contains(s)) ||
                            (u.getEmail() != null && u.getEmail().toLowerCase().contains(s)) ||
                            (u.getPhone() != null && u.getPhone().toLowerCase().contains(s))
            ).toList();
        }

        model.addAttribute("users", users);
        model.addAttribute("status", status);
        model.addAttribute("q", q == null ? "" : q);

        model.addAttribute("statuses", AccountStatus.values());
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow();
        u.setStatus(AccountStatus.APPROVED);
        userRepository.save(u);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow();
        u.setStatus(AccountStatus.REJECTED);
        userRepository.save(u);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String softDelete(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow();
        u.setStatus(AccountStatus.DELETED);
        userRepository.save(u);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            @PathVariable Long id,
            @RequestParam @NotBlank String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam Role role,
            @RequestParam AccountStatus status
    ) {
        User u = userRepository.findById(id).orElseThrow();
        u.setUsername(username.trim());
        u.setEmail(email == null || email.isBlank() ? null : email.trim());
        u.setPhone(phone == null || phone.isBlank() ? null : phone.trim());
        u.setRole(role);
        u.setStatus(status);
        userRepository.save(u);

        return "redirect:/admin/users";
    }
}
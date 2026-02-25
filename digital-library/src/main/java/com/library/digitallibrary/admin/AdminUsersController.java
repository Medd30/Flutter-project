package com.library.digitallibrary.admin;

import com.library.digitallibrary.admin.dto.AdminUserResponse;
import com.library.digitallibrary.admin.dto.UpdateUserRequest;
import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.Role;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {

    private final UserRepository userRepository;

    // ✅ GET /api/admin/users?search=&status=&role=
    // ✅ GET /api/admin/users?search=&status=&role=
// default: DO NOT show DELETED users unless status=DELETED is requested
    @GetMapping
    public List<AdminUserResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) Role role
    ) {
        List<User> users;

        boolean hasSearch = search != null && !search.isBlank();
        boolean hasStatus = status != null;
        boolean hasRole = role != null;

        if (hasStatus && hasRole) {
            users = userRepository.findByStatusAndRole(status, role);
        } else if (hasStatus) {
            users = userRepository.findByStatus(status);
        } else if (hasRole) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findAll();
        }

        // ✅ default filter: hide DELETED unless the admin explicitly asked for DELETED
        if (!hasStatus) {
            users = users.stream()
                    .filter(u -> u.getStatus() != AccountStatus.DELETED)
                    .toList();
        }

        // ✅ optional search (username/email/phone) - keep DELETED hidden too unless status=DELETED
        if (hasSearch) {
            String q = search.trim().toLowerCase();
            users = users.stream()
                    .filter(u ->
                            (u.getUsername() != null && u.getUsername().toLowerCase().contains(q)) ||
                                    (u.getEmail() != null && u.getEmail().toLowerCase().contains(q)) ||
                                    (u.getPhone() != null && u.getPhone().toLowerCase().contains(q))
                    )
                    .toList();
        }

        return users.stream().map(AdminUserResponse::from).toList();
    }

    // ✅ GET /api/admin/users/{id}
    @GetMapping("/{id}")
    public AdminUserResponse getOne(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return AdminUserResponse.from(u);
    }

    // ✅ POST /api/admin/users/{id}/approve
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(AccountStatus.APPROVED);
        userRepository.save(u);
        return "User approved";
    }

    // ✅ POST /api/admin/users/{id}/reject
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(AccountStatus.REJECTED);
        userRepository.save(u);
        return "User rejected";
    }

    // ✅ PATCH /api/admin/users/{id}
    @PatchMapping("/{id}")
    public AdminUserResponse update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getUsername() != null) u.setUsername(req.getUsername());
        if (req.getEmail() != null) u.setEmail(req.getEmail());
        if (req.getPhone() != null) u.setPhone(req.getPhone());
        if (req.getRole() != null) u.setRole(req.getRole());
        if (req.getStatus() != null) u.setStatus(req.getStatus());
        if (req.getEmailVerified() != null) u.setEmailVerified(req.getEmailVerified());
        if (req.getPhoneVerified() != null) u.setPhoneVerified(req.getPhoneVerified());
        if (req.getProfileImageUrl() != null) u.setProfileImageUrl(req.getProfileImageUrl());

        // Note: if later you want admin to reset password:
        // if (req.getNewPassword() != null) u.setPassword(passwordEncoder.encode(req.getNewPassword()));

        userRepository.save(u);
        return AdminUserResponse.from(u);
    }

    // ✅ DELETE /api/admin/users/{id}
    // We’ll do SOFT delete by default to avoid FK breakage.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Soft delete approach:
        // If you already have AccountStatus.DELETED, use it.
        // If you don't, keep it as REJECTED for now.
        u.setStatus(AccountStatus.DELETED);

        userRepository.save(u);

        // Hard delete (NOT recommended unless cascading is handled):
        // userRepository.delete(u);
    }
}
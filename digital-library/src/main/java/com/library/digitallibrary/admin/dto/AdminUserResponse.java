package com.library.digitallibrary.admin.dto;

import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.Role;
import com.library.digitallibrary.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Role role;
    private AccountStatus status;
    private boolean emailVerified;
    private boolean phoneVerified;
    private String profileImageUrl;

    public static AdminUserResponse from(User u) {
        return AdminUserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(u.getRole())
                .status(u.getStatus())
                .emailVerified(u.isEmailVerified())
                .phoneVerified(u.isPhoneVerified())
                .profileImageUrl(u.getProfileImageUrl())
                .build();
    }
}
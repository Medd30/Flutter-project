package com.library.digitallibrary.admin.dto;

import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.Role;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
    private String phone;
    private Role role;
    private AccountStatus status;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private String profileImageUrl;

    // Optional later:
    // private String newPassword;
}
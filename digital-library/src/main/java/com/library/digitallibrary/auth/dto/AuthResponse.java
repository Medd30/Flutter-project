package com.library.digitallibrary.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private Long userId;
    private String token;
    private String role;
}

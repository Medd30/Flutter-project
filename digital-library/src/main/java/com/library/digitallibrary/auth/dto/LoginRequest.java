package com.library.digitallibrary.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @NotBlank
    public String identifier; // email OR phone

    @NotBlank
    public String password;
}

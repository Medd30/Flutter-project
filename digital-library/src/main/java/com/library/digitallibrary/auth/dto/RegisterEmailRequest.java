package com.library.digitallibrary.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterEmailRequest {
    @NotBlank
    public String username;

    @Email @NotBlank
    public String email;

    @NotBlank
    public String password;
}

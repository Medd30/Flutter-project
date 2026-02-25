package com.library.digitallibrary.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterPhoneRequest {
    @NotBlank
    public String username;

    @NotBlank
    public String phone;

    @NotBlank
    public String password;
}

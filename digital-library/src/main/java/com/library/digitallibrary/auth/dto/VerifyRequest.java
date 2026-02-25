package com.library.digitallibrary.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VerifyRequest {
    public Long userId;

    @NotBlank
    public String code;
}

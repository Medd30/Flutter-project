package com.library.digitallibrary.me.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMeProfileRequest {
    private String username;
    private String profileImageUrl; // optional (keep for later if you want)
}
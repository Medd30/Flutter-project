package com.library.digitallibrary.me.dto;

import com.library.digitallibrary.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String profileImageUrl;

    public static MeProfileResponse from(User u) {
        return MeProfileResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .profileImageUrl(u.getProfileImageUrl())
                .build();
    }
}
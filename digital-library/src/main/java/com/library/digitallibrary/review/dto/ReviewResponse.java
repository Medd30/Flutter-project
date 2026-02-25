package com.library.digitallibrary.review.dto;

import com.library.digitallibrary.review.Review;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;
    private Instant createdAt;

    private Long userId;
    private String username;

    private boolean mine;

    public static ReviewResponse from(Review r, Long currentUserId) {
        return ReviewResponse.builder()
                .id(r.getId())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .userId(r.getUser().getId())
                .username(r.getUser().getUsername())
                .mine(currentUserId != null && r.getUser().getId().equals(currentUserId))
                .build();
    }
}
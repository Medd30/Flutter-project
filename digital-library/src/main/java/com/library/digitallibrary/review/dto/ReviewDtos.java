package com.library.digitallibrary.review.dto;

import java.time.Instant;

public class ReviewDtos {

    public record ReviewResponse(
            Long id,
            int rating,
            String comment,
            Instant createdAt,
            UserMini user
    ) {}

    public record UserMini(
            Long id,
            String username,
            String profileImageUrl
    ) {}
}

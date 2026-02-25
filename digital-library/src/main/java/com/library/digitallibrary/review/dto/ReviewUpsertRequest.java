package com.library.digitallibrary.review.dto;

import lombok.Data;

@Data
public class ReviewUpsertRequest {
    private int rating;     // 1..5
    private String comment; // optional
}
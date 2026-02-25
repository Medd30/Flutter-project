package com.library.digitallibrary.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewRequest {
    private int rating;     // 1..5
    private String comment; // optional
}
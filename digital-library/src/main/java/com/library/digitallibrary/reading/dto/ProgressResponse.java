package com.library.digitallibrary.reading.dto;

public record ProgressResponse(
        Integer lastPage,
        Double percentage
) {}
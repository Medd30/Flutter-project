package com.library.digitallibrary.book.dto;

public record BookDetailsResponse(
        Long id,
        String title,
        String author,
        String category,
        String coverImageUrl,
        String pdfUrl,
        double avgRating,
        long reviewCount,
        boolean isFavorite,
        boolean isReadLater
) {}

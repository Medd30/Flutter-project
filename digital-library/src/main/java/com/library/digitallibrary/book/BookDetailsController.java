package com.library.digitallibrary.book;

import com.library.digitallibrary.book.dto.BookDetailsResponse;
import com.library.digitallibrary.book.repo.BookRepository;
import com.library.digitallibrary.book.repo.FavoriteRepository;
import com.library.digitallibrary.book.repo.ReadLaterRepository;
import com.library.digitallibrary.common.SecurityUtils;
import com.library.digitallibrary.review.repo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookDetailsController {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReadLaterRepository readLaterRepository;

    @GetMapping("/{bookId}/details")
    public BookDetailsResponse details(@PathVariable Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow();

        long count = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId).size();
        double avg = 0.0;

        if (count > 0) {
            avg = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId)
                    .stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);
        }

        Long userId = SecurityUtils.currentUserId();
        boolean isFav = false;
        boolean isLater = false;

        if (userId != null) {
            isFav = favoriteRepository.existsByUserIdAndBookId(userId, bookId);
            isLater = readLaterRepository.existsByUserIdAndBookId(userId, bookId);
        }

        return new BookDetailsResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getCoverImageUrl(),
                book.getPdfUrl(),
                avg,
                count,
                isFav,
                isLater
        );
    }
}

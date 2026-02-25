package com.library.digitallibrary.review;

import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.book.repo.BookRepository;
import com.library.digitallibrary.common.SecurityUtils;
import com.library.digitallibrary.review.dto.ReviewDtos;
import com.library.digitallibrary.review.repo.ReviewRepository;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @GetMapping("/book/{bookId}")
    public List<ReviewDtos.ReviewResponse> bookReviews(@PathVariable Long bookId) {
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping("/{bookId}")
    public ReviewDtos.ReviewResponse addReview(@PathVariable Long bookId, @RequestBody ReviewRequest req) {

        Long userId = SecurityUtils.currentUserId();

        if (req.rating < 1 || req.rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be 1..5");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Review review = reviewRepository.findByUserIdAndBookId(userId, bookId)
                .orElse(Review.builder()
                        .user(user)
                        .book(book)
                        .createdAt(Instant.now())
                        .build());

        review.setRating(req.rating);
        review.setComment(req.comment);

        return toDto(reviewRepository.save(review));
    }

    private ReviewDtos.ReviewResponse toDto(Review r) {
        var u = r.getUser();
        return new ReviewDtos.ReviewResponse(
                r.getId(),
                r.getRating(),
                r.getComment(),
                r.getCreatedAt(),
                new ReviewDtos.UserMini(u.getId(), u.getUsername(), u.getProfileImageUrl())
        );
    }

    public record ReviewRequest(int rating, String comment) {}
}

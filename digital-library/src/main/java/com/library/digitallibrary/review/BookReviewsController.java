package com.library.digitallibrary.review;

import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.book.repo.BookRepository;
import com.library.digitallibrary.review.dto.ReviewResponse;
import com.library.digitallibrary.review.dto.ReviewUpsertRequest;
import com.library.digitallibrary.review.repo.ReviewRepository;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books/{bookId}/reviews")
public class BookReviewsController {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        Object principal = auth.getPrincipal();
        // Adjust this depending on what you store in JwtAuthFilter:
        // If principal is UserDetails with username/email => resolve user from DB.
        String usernameOrEmail = principal.toString();

        // If your login uses email:
        return userRepository.findByEmail(usernameOrEmail).map(User::getId)
                .orElseGet(() -> userRepository.findByPhone(usernameOrEmail).map(User::getId).orElse(null));
    }

    @GetMapping
    public List<ReviewResponse> list(@PathVariable Long bookId) {
        Long me = currentUserId();
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId)
                .stream()
                .map(r -> ReviewResponse.from(r, me))
                .toList();
    }

    @PostMapping
    public ReviewResponse upsert(@PathVariable Long bookId, @RequestBody ReviewUpsertRequest req) {
        Long me = currentUserId();
        if (me == null) throw new RuntimeException("Not authenticated");

        if (req.getRating() < 1 || req.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findById(me)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findByUserIdAndBookId(bookId, me)
                .orElseGet(() -> Review.builder().book(book).user(user).build());

        review.setRating(req.getRating());
        review.setComment(req.getComment() == null ? null : req.getComment().trim());

        Review saved = reviewRepository.save(review);
        return ReviewResponse.from(saved, me);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMine(@PathVariable Long bookId) {
        Long me = currentUserId();
        if (me == null) throw new RuntimeException("Not authenticated");

        Review review = reviewRepository.findByUserIdAndBookId(bookId, me)
                .orElseThrow(() -> new RuntimeException("No review to delete"));

        reviewRepository.delete(review);
    }
}
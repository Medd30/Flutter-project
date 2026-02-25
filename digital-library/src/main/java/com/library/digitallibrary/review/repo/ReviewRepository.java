package com.library.digitallibrary.review.repo;

import com.library.digitallibrary.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);

    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);
}

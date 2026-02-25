package com.library.digitallibrary.admin;

import com.library.digitallibrary.review.repo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewRepository reviewRepository;

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return "Review deleted";
    }
}

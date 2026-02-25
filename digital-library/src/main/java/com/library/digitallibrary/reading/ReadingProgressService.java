package com.library.digitallibrary.reading;

import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.book.repo.BookRepository;
import com.library.digitallibrary.reading.dto.ProgressResponse;
import com.library.digitallibrary.reading.dto.SaveProgressRequest;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingProgressService {

    private final ReadingProgressRepository progressRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;

    public ProgressResponse getProgress(Long userId, Long bookId) {
        return progressRepo.findByUserIdAndBookId(userId, bookId)
                .map(p -> new ProgressResponse(p.getLastPage(), p.getPercentage()))
                .orElse(new ProgressResponse(1, 0.0)); // default
    }

    @Transactional
    public ProgressResponse saveProgress(Long userId, Long bookId, SaveProgressRequest req) {
        // validate existence
        User user = userRepo.findById(userId).orElseThrow();
        Book book = bookRepo.findById(bookId).orElseThrow();

        ReadingProgress p = progressRepo.findByUserIdAndBookId(userId, bookId)
                .orElseGet(() -> ReadingProgress.builder()
                        .user(user)
                        .book(book)
                        .lastPage(1)
                        .percentage(0.0)
                        .build());

        // clamp values (extra safety)
        int lastPage = Math.max(1, req.lastPage());
        double percentage = Math.max(0.0, Math.min(100.0, req.percentage()));

        p.setLastPage(lastPage);
        p.setPercentage(percentage);

        progressRepo.save(p);
        return new ProgressResponse(p.getLastPage(), p.getPercentage());
    }
}
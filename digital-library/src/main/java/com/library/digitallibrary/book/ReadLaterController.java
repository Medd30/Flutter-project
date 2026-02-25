package com.library.digitallibrary.book;

import com.library.digitallibrary.book.repo.BookRepository;
import com.library.digitallibrary.book.repo.ReadLaterRepository;
import com.library.digitallibrary.common.SecurityUtils;
import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/me/read-later")
@RequiredArgsConstructor
public class ReadLaterController {

    private final ReadLaterRepository readLaterRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Book> myReadLater() {
        Long userId = SecurityUtils.currentUserId();
        return readLaterRepository.findByUserId(userId)
                .stream()
                .map(ReadLater::getBook)
                .toList();
    }

    @PostMapping("/{bookId}")
    public Book addReadLater(@PathVariable Long bookId) {
        Long userId = SecurityUtils.currentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        if (readLaterRepository.existsByUserIdAndBookId(userId, bookId)) {
            return book;
        }

        readLaterRepository.save(ReadLater.builder()
                .user(user)
                .book(book)
                .build());

        return book;
    }

    @DeleteMapping("/{bookId}")
    public Map<String, Object> removeReadLater(@PathVariable Long bookId) {
        Long userId = SecurityUtils.currentUserId();

        if (!readLaterRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not in read later");
        }

        readLaterRepository.deleteByUserIdAndBookId(userId, bookId);

        return Map.of(
                "removed", true,
                "bookId", bookId
        );
    }
}

package com.library.digitallibrary.book;

import com.library.digitallibrary.book.repo.BookRepository;
import com.library.digitallibrary.book.repo.FavoriteRepository;
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
@RequestMapping("/api/me/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Book> myFavorites() {
        Long userId = SecurityUtils.currentUserId();
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getBook)
                .toList();
    }

    @PostMapping("/{bookId}")
    public Book addFavorite(@PathVariable Long bookId) {
        Long userId = SecurityUtils.currentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            // return the book anyway (idempotent)
            return book;
        }

        favoriteRepository.save(Favorite.builder()
                .user(user)
                .book(book)
                .build());

        return book;
    }

    @DeleteMapping("/{bookId}")
    public Map<String, Object> removeFavorite(@PathVariable Long bookId) {
        Long userId = SecurityUtils.currentUserId();

        if (!favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not in favorites");
        }

        favoriteRepository.deleteByUserIdAndBookId(userId, bookId);

        return Map.of(
                "removed", true,
                "bookId", bookId
        );
    }
}

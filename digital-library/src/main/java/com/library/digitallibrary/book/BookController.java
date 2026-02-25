package com.library.digitallibrary.book;

import com.library.digitallibrary.book.repo.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    // GET /api/books?search=harry&category=Fantasy
    @GetMapping
    public List<Book> listBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category
    ) {
        boolean hasSearch = search != null && !search.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        if (hasSearch && hasCategory) {
            return bookRepository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(search.trim(), category.trim());
        }
        if (hasSearch) {
            return bookRepository.findByTitleContainingIgnoreCase(search.trim());
        }
        if (hasCategory) {
            return bookRepository.findByCategoryIgnoreCase(category.trim());
        }
        return bookRepository.findAll();
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}

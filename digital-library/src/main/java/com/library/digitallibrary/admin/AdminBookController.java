package com.library.digitallibrary.admin;

import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.book.repo.BookRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookRepository bookRepository;

    @PostMapping
    public Book create(@RequestBody CreateBookRequest req) {
        Book book = Book.builder()
                .title(req.title)
                .author(req.author)
                .category(req.category)
                .coverImageUrl(req.coverImageUrl)
                .pdfUrl(req.pdfUrl)
                .build();
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "Deleted";
    }

    @Getter @Setter
    public static class CreateBookRequest {
        public String title;
        public String author;
        public String category;
        public String coverImageUrl;
        public String pdfUrl;
    }
}

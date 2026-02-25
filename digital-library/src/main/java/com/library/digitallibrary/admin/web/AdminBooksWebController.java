package com.library.digitallibrary.admin.web;

import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.book.repo.BookRepository;
//import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBooksWebController {

    private final BookRepository bookRepository;

    @GetMapping
    public String list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            Model model
    ) {
        boolean hasQ = q != null && !q.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        List<Book> books;
        if (hasQ && hasCategory) {
            books = bookRepository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(q.trim(), category.trim());
        } else if (hasQ) {
            books = bookRepository.findByTitleContainingIgnoreCase(q.trim());
        } else if (hasCategory) {
            books = bookRepository.findByCategoryIgnoreCase(category.trim());
        } else {
            books = bookRepository.findAll();
        }

        model.addAttribute("books", books);
        model.addAttribute("q", hasQ ? q.trim() : "");
        model.addAttribute("category", hasCategory ? category.trim() : "");
        return "admin/books";
    }

    // Create book (cover optional)
    @PostMapping
    public String create(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,

            // ✅ new: file uploads (pdf required, cover optional)
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile
    ) throws IOException {

        if (pdfFile == null || pdfFile.isEmpty()) {
            throw new RuntimeException("PDF file is required");
        }

        // 1) ensure folder exists
        Path uploadDir = Paths.get("uploads/books");
        Files.createDirectories(uploadDir);

        // 2) save pdf
        String pdfName = System.currentTimeMillis() + "_" +
                pdfFile.getOriginalFilename().replaceAll("\\s+", "_");
        Path pdfPath = uploadDir.resolve(pdfName);
        Files.copy(pdfFile.getInputStream(), pdfPath, StandardCopyOption.REPLACE_EXISTING);

        // 3) save cover if provided
        String coverUrl = null;
        if (coverFile != null && !coverFile.isEmpty()) {
            String coverName = System.currentTimeMillis() + "_" +
                    coverFile.getOriginalFilename().replaceAll("\\s+", "_");
            Path coverPath = uploadDir.resolve(coverName);
            Files.copy(coverFile.getInputStream(), coverPath, StandardCopyOption.REPLACE_EXISTING);
            coverUrl = "/files/" + coverName;
        }

        // 4) create and save book
        Book b = new Book();
        b.setTitle(title.trim());
        b.setAuthor(author == null || author.isBlank() ? null : author.trim());
        b.setCategory(category == null || category.isBlank() ? null : category.trim());

        // ✅ store ONLY relative path
        b.setPdfUrl("/files/" + pdfName);
        b.setCoverImageUrl(coverUrl);

        bookRepository.save(b);
        return "redirect:/admin/books";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, name="pdfUrl") String pdfUrl,
            @RequestParam(required = false, name="coverUrl") String coverUrl
    ) {
        Book b = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        b.setTitle(title.trim());
        b.setAuthor(author == null || author.isBlank() ? null : author.trim());
        b.setCategory(category == null || category.isBlank() ? null : category.trim());
        b.setPdfUrl(pdfUrl == null || pdfUrl.isBlank() ? null : pdfUrl.trim());
        b.setCoverImageUrl(coverUrl == null || coverUrl.isBlank() ? null : coverUrl.trim());

        bookRepository.save(b);
        return "redirect:/admin/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        // Hard delete. If later FK constraints block it, we’ll switch to soft delete.
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}
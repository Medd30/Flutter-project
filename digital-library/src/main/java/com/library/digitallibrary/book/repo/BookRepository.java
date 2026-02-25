package com.library.digitallibrary.book.repo;

import com.library.digitallibrary.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Basic search use-cases (we’ll improve later)
    List<Book> findByCategoryIgnoreCase(String category);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(String title, String category);

    @Query("""
        select b from Book b
        where (:q is null or :q = '' or
              lower(b.title) like lower(concat('%', :q, '%')) or
              lower(b.author) like lower(concat('%', :q, '%')) or
              lower(b.category) like lower(concat('%', :q, '%'))
        )
        and (:category is null or :category = '' or lower(b.category) = lower(:category))
        order by b.id desc
    """)
    List<Book> search(@Param("q") String q, @Param("category") String category);

}

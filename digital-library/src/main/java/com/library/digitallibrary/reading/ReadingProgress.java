package com.library.digitallibrary.reading;

import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "reading_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "last_page", nullable = false)
    private Integer lastPage;

    @Column(name = "percentage", nullable = false)
    private Double percentage;
}
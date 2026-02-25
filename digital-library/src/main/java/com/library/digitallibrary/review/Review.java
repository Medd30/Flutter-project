package com.library.digitallibrary.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.library.digitallibrary.book.Book;
import com.library.digitallibrary.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import jakarta.persistence.PrePersist;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 1..5

    @Column(length = 1000)
    private String comment;

    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    //@JsonIgnore
    @JsonBackReference
    private Book book;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }

}

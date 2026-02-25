package com.library.digitallibrary.book;

import com.library.digitallibrary.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The owner of the favorite
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // The favorited book
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;
}

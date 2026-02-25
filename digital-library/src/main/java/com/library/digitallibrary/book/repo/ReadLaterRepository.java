package com.library.digitallibrary.book.repo;

import com.library.digitallibrary.book.ReadLater;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ReadLaterRepository extends JpaRepository<ReadLater, Long> {

    List<ReadLater> findByUserId(Long userId);

    Optional<ReadLater> findByUserIdAndBookId(Long userId, Long bookId);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    @Modifying
    @Transactional
    void deleteByUserIdAndBookId(Long userId, Long bookId);

}

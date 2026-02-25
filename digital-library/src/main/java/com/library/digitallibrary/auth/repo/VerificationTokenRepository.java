package com.library.digitallibrary.auth.repo;

import com.library.digitallibrary.auth.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findTopByUserIdAndTypeOrderByIdDesc(Long userId, String type);
    Optional<VerificationToken> findByUserIdAndTypeAndCode(Long userId, String type, String code);
    void deleteByUserIdAndType(Long userId, String type);
}

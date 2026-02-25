package com.library.digitallibrary.user.repo;

import com.library.digitallibrary.user.AccountStatus;
import com.library.digitallibrary.user.Role;
import com.library.digitallibrary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    // ✅ new
    List<User> findByStatus(AccountStatus status);

    // ✅ optional filters (we’ll use them for list endpoint)
    List<User> findByRole(Role role);
    List<User> findByStatusAndRole(AccountStatus status, Role role);

    // ✅ simple “search”
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByEmailContainingIgnoreCase(String email);


}
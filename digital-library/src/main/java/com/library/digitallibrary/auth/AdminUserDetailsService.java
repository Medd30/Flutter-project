package com.library.digitallibrary.auth;

import com.library.digitallibrary.user.User;
import com.library.digitallibrary.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username field on login page = email OR phone
        User u = userRepository.findByEmail(username).orElse(null);
        if (u == null) u = userRepository.findByPhone(username).orElse(null);
        if (u == null) throw new UsernameNotFoundException("User not found");

        return new org.springframework.security.core.userdetails.User(
                (u.getEmail() != null ? u.getEmail() : u.getPhone()),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
        );
    }
}
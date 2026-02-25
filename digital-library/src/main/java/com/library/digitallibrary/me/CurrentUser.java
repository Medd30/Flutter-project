package com.library.digitallibrary.me;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    private CurrentUser() {}

    public static Long requireUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new RuntimeException("Not authenticated");

        // Most common: auth.getName() == "123"
        String name = auth.getName();
        if (name != null) {
            try {
                return Long.parseLong(name);
            } catch (NumberFormatException ignored) {}
        }

        // Sometimes principal is the userId as String/Long
        Object principal = auth.getPrincipal();
        if (principal instanceof Long l) return l;
        if (principal instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException ignored) {}
        }

        throw new RuntimeException("Cannot resolve userId from JWT authentication");
    }
}
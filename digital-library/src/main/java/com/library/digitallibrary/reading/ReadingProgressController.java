//package com.library.digitallibrary.reading;
//
//import com.library.digitallibrary.auth.*; // adjust to your actual class
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//@RestController
//@RequestMapping("/api/me/progress")
//@RequiredArgsConstructor
//public class ReadingProgressController {
//
//    private final ReadingProgressService service;
//
//    private Long currentUserId(JW principal) {
//        if (principal == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token");
//        }
//        return principal.getId();
//    }
//
//    @GetMapping("/{bookId}")
//    public ReadingProgressDto get(
//            @PathVariable Long bookId,
//            @org.springframework.security.core.annotation.AuthenticationPrincipal AppUserDetails principal
//    ) {
//        return service.get(currentUserId(principal), bookId);
//    }
//
//    @PostMapping("/{bookId}")
//    public ReadingProgressDto upsert(
//            @PathVariable Long bookId,
//            @RequestBody UpsertProgressRequest req,
//            @org.springframework.security.core.annotation.AuthenticationPrincipal AppUserDetails principal
//    ) {
//        return service.upsert(currentUserId(principal), bookId, req);
//    }
//}
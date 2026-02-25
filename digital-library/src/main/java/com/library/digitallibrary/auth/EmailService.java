package com.library.digitallibrary.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Digital Library - Email Verification");
        message.setText("""
                Your verification code is:

                %s

                This code expires in 10 minutes.
                """.formatted(code));

        mailSender.send(message);
    }
}
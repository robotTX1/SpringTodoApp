package com.robottx.springtodoapp.application.email.service;

import com.robottx.springtodoapp.model.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.email}")
    private String email;
    private final JavaMailSender mailSender;

    public void sendForgotPasswordMail(User user, String link) throws MessagingException, UnsupportedEncodingException {
        String subject = "Forgot Password";
        String senderName = "Forgot Password Service";
        String mailContent = """
                    <p>Hey, %s</p>
                    <br>
                    <p>You recently tried to change your password.</p>
                    <p>In order to change your password follow the link below</p>
                    <a href="%s">Change Password</a>
                    <br>
                    <p>If you did not request a password change please delete this email</p>
                    <br>
                    <p>Sincerely,</p>
                    <p>Robocorp</p>
                """.formatted(user.getUsername(), link);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(email, senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}

package com.robottx.springtodoapp.application.email.service;

import com.robottx.springtodoapp.model.user.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendForgotPasswordMail(User user, String link) throws MessagingException, UnsupportedEncodingException;

    void sendVerificationEmail(User user, String link) throws MessagingException, UnsupportedEncodingException;
}

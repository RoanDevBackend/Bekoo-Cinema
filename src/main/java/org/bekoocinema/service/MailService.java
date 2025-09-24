package org.bekoocinema.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendMail(String subject, String email, String content, boolean isHtml) throws UnsupportedEncodingException, MessagingException;
}

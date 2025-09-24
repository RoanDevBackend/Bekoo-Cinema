package org.bekoocinema.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
@EnableAsync
public class MailServiceImpl implements MailService {

    final JavaMailSender javaMailSender;

    @Value("${mail.user}")
    String from;

    @Async
    @Override
    public void sendMail(String subject, String email, String content, boolean isHtml) throws UnsupportedEncodingException, MessagingException {
        var message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(from, "Bekoo Cinema");
        helper.setTo(email);

        helper.setSubject(subject);
        helper.setText(content, isHtml);
        javaMailSender.send(message);

    }
}

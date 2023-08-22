package com.vux.security.service.impl;

import com.vux.security.entity.User;
import com.vux.security.payload.RegisterRequest;
import com.vux.security.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final Configuration configuration;

    @Async
    @Override
    public void sendSimpleMessage(
      String to, String subject, String text, User user) throws MessagingException, TemplateException, IOException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setTo(to);
        String emailContent = getEmailContent(text, user);
        helper.setText(emailContent, true);
        emailSender.send(mimeMessage);
    }


    @Async
    @Override
    public void sendSimpleMessage(
            String to, String subject, String content
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ttcssendmail@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
    }
    public String getEmailContent(String content, User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("content", content);
        configuration.getTemplate("email.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}

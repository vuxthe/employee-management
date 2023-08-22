package com.vux.security.service;

import com.vux.security.entity.User;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

public interface EmailService {


	void sendSimpleMessage(String to, String subject, String text, User user) throws MessagingException, TemplateException, IOException;

	void sendSimpleMessage(String to, String subject, String text);

}

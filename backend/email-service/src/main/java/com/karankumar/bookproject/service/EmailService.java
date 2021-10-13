package com.karankumar.bookproject.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {

    void sendSimpleMessage(String from, String to, String message);
    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel)
            throws MessagingException;
}

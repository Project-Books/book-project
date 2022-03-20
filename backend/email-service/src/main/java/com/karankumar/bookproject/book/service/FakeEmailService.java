package com.karankumar.bookproject.book.service;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Profile({"local", "dev", "test"})
@Component
@ExcludeFromJacocoGeneratedReport
public class FakeEmailService implements EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FakeEmailService.class);

  @Override
  public void sendSimpleMessage(String from, String to, String message) {
    LOGGER.info("[Mail] email sent to -> {}", to);
    System.out.println(
        "----------------------------------------- Simple Message Mail Sent -----------------------------------------");
    System.out.println("From: " + from);
    System.out.println("To: " + to);
    System.out.println("Message: " + message);
  }

  @Override
  public void sendMessageUsingThymeleafTemplate(
      String to, String subject, Map<String, Object> templateModel) {
    LOGGER.info("[Mail] email sent to -> {}", to);
    System.out.println(
        "----------------------------------------- Send Message with thymleaf -----------------------------------------");
    System.out.println("To: " + to);
    System.out.println("Subject: " + subject);
  }
}

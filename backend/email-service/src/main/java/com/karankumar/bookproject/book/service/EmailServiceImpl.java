/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2021  Karan Kumar
This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Profile("prod")
@Service
public class EmailServiceImpl implements EmailService {
  public static final String NOREPLY_ADDRESS = "noreply@karankumar.com";
  private final JavaMailSender emailSender;
  private final SpringTemplateEngine thymeleafTemplateEngine;

  @Autowired
  public EmailServiceImpl(
      JavaMailSender emailSender, SpringTemplateEngine thymeleafTemplateEngine) {
    this.emailSender = emailSender;
    this.thymeleafTemplateEngine = thymeleafTemplateEngine;
  }

  @Override
  public void sendSimpleMessage(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(NOREPLY_ADDRESS);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    emailSender.send(message);
  }

  public void sendMessageUsingThymeleafTemplate(
      String to, String subject, Map<String, Object> templateModel) throws MessagingException {

    Context thymeleafContext = new Context();
    thymeleafContext.setVariables(templateModel);

    String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);

    sendHtmlMessage(to, subject, htmlBody);
  }

  private void sendHtmlMessage(String to, String subject, String htmlBody)
      throws MessagingException {

    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(NOREPLY_ADDRESS);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true);
    emailSender.send(message);
  }
}

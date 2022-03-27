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

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.karankumar.bookproject.configuration.EmailConfiguration;
import com.karankumar.bookproject.configuration.ThymeleafConfiguration;
import com.karankumar.bookproject.mock.MockEmailTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(classes = {EmailConfiguration.class, ThymeleafConfiguration.class})
class EmailServiceTest {

  EmailServiceImpl emailService;
  String messageBody;
  String recipient;
  String subject;
  Map<String, Object> thymeleafModel;

  @Autowired private SpringTemplateEngine templateEngine;

  @Resource JavaMailSenderImpl javaMailSender;

  @RegisterExtension
  static GreenMailExtension greenMail =
      new GreenMailExtension(ServerSetupTest.SMTP)
          .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "test"))
          .withPerMethodLifecycle(true);

  @BeforeEach
  void setUp() {
    messageBody = "This is test message";
    recipient = "test@karankumar.com";
    subject = "Test subject";
    emailService = new EmailServiceImpl(javaMailSender, templateEngine);
    thymeleafModel =
        MockEmailTemplate.getTestTemplate(emailService.getUsernameFromEmail(recipient));
  }

  @Test
  void GivenRecipientSubjectBody_WhenSendSimpleMessage_ThenSendMessage() {
    emailService.sendSimpleMessage(recipient, subject, messageBody);

    MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];

    assertSoftly(
        softly -> {
          softly.assertThat(messageBody).isEqualTo(GreenMailUtil.getBody(receivedMessage));
          try {
            softly.assertThat(receivedMessage.getAllRecipients().length).isOne();
            softly
                .assertThat(recipient)
                .isEqualTo(receivedMessage.getAllRecipients()[0].toString());
            softly.assertThat(subject).isEqualTo(receivedMessage.getSubject());
          } catch (MessagingException e) {
            Assertions.fail("Messaging exception thrown");
          }
        });
  }

  @Test
  void GivenRecipientSubjectBody_WhenSendMessageUsingThymeleafTemplate_ThenSendMessage()
      throws MessagingException {
    emailService.sendMessageUsingThymeleafTemplate(recipient, subject, thymeleafModel);

    MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];

    assertSoftly(
        softly -> {
          softly
              .assertThat(GreenMailUtil.getBody(receivedMessage))
              .contains("<p>" + messageBody + "</p>");
          try {
            softly.assertThat(receivedMessage.getAllRecipients().length).isOne();
            softly
                .assertThat(recipient)
                .isEqualTo(receivedMessage.getAllRecipients()[0].toString());
            softly.assertThat(subject).isEqualTo(receivedMessage.getSubject());
          } catch (MessagingException e) {
            Assertions.fail("Messaging exception thrown");
          }
        });
  }
}

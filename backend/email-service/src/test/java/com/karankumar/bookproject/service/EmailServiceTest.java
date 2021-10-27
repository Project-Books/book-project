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

package com.karankumar.bookproject.service;

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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {EmailConfiguration.class, ThymeleafConfiguration.class, EmailServiceImpl.class})
class EmailServiceTest {

    @Autowired
    EmailServiceImpl emailService;
    String messageBody;
    String recipient;
    String subject;
    Map<String, Object> thymeleafModel;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "test"))
            .withPerMethodLifecycle(true);

    @BeforeEach
    void setUp() {
        messageBody = "This is test message";
        recipient = "test@karankumar.com";
        subject = "Test subject";
        thymeleafModel = MockEmailTemplate.getTestTemplate(emailService.getUsernameFromEmail(recipient));
    }

    @Test
    void GivenRecipientSubjectBody_WhenSendSimpleMessage_ThenSendMessage() {
        emailService.sendSimpleMessage(recipient,subject, messageBody);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];

        Assertions.assertAll(
                () -> assertEquals(messageBody, GreenMailUtil.getBody(receivedMessage)),
                () -> assertEquals(1, receivedMessage.getAllRecipients().length),
                () -> assertEquals(recipient, receivedMessage.getAllRecipients()[0].toString()),
                () -> assertEquals(subject, receivedMessage.getSubject())
        );
    }

    @Test
    void GivenRecipientSubjectBody_WhenSendMessageUsingThymeleafTemplate_ThenSendMessage() throws MessagingException {
        emailService.sendMessageUsingThymeleafTemplate(recipient, subject,thymeleafModel);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];

        assertAll(
                () -> assertTrue(GreenMailUtil.getBody(receivedMessage).contains("<p>" + messageBody + "</p>")),
                () -> assertEquals(1, receivedMessage.getAllRecipients().length),
                () -> assertEquals(recipient, receivedMessage.getAllRecipients()[0].toString()),
                () -> assertEquals(subject, receivedMessage.getSubject())
        );
    }
}

/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.account.controller;

import com.karankumar.bookproject.account.controller.UserController;
import com.karankumar.bookproject.account.dto.UserToRegisterDto;
import com.karankumar.bookproject.account.exception.CurrentUserNotFoundException;
import com.karankumar.bookproject.account.exception.PasswordTooWeakException;
import com.karankumar.bookproject.account.exception.UserAlreadyRegisteredException;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import com.karankumar.bookproject.book.service.EmailService;
import com.karankumar.bookproject.account.exception.IncorrectPasswordException;
import com.karankumar.bookproject.constant.EmailConstant;
import com.karankumar.bookproject.template.EmailTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = {UserService.class, PasswordEncoder.class})
@ActiveProfiles("test")
class UserControllerTest {

  @MockBean UserService userService;

  private final UserController userController;
  private final UserService mockedUserService;
  private final EmailService mockedEmailService;
  private final PasswordEncoder mockPasswordEncoder;

  private final User validUser =
      User.builder().email("valid@testmail.com").password("aaaaAAAA1234@").build();

  UserControllerTest() {
    mockedUserService = mock(UserService.class);
    mockedEmailService = mock(EmailService.class);
    mockPasswordEncoder = mock(PasswordEncoder.class);
    userController = new UserController(mockedUserService, mockPasswordEncoder, mockedEmailService);
  }

  @Test
  void getUser_returnsUser_ifPresent() {
    User user = validUser;
    when(mockedUserService.findUserById(any(Long.class))).thenReturn(Optional.of(user));

    assertThat(userController.getUser(0L)).isEqualTo(user);
  }

  @Test
  void getUser_returnsNotFound_ifUserIsEmpty() {
    when(mockedUserService.findUserById(any(Long.class))).thenReturn(Optional.empty());

    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> userController.getUser(0L));
  }

  @Test
  void emailExists_returnsOk_ifTrue() {
    // given
    when(mockedUserService.findUserByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

    // when
    ResponseEntity<String> response = userController.emailExists("test@email.com");

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void emailExists_returnsNotFound_ifFalse() {
    // given
    when(mockedUserService.findUserByEmail(anyString())).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> response = userController.emailExists("test@email.com");

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void updateEmail_returns404_ifUserNotFound() {
    // given
    when(mockedUserService.getCurrentUser()).thenThrow(new CurrentUserNotFoundException("a"));

    // when
    ResponseEntity<String> response = userController.updateEmail("anything", "anything");

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
          softly
              .assertThat(response.getBody())
              .isEqualTo(UserController.CURRENT_USER_NOT_FOUND_ERROR_MESSAGE);
        });
  }

  @Test
  void updateEmail_returns401_ifPasswordIncorrect() {
    // given
    doThrow(new IncorrectPasswordException("anything"))
        .when(mockedUserService)
        .changeUserEmail(any(), anyString(), anyString());

    // when
    ResponseEntity<String> response = userController.updateEmail("anything", "anything");

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void updateEmail_returns400_ifNewEmailSameAsCurrent() {
    // given
    doThrow(new UserAlreadyRegisteredException("anything"))
        .when(mockedUserService)
        .changeUserEmail(any(), anyString(), anyString());

    // when
    ResponseEntity<String> response = userController.updateEmail("anything", "anything");

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void updateEmail_returnsBadRequest_ifConstraintViolated() {
    // given
    doThrow(new ConstraintViolationException(new HashSet<>()))
        .when(mockedUserService)
        .changeUserEmail(any(), anyString(), anyString());

    // when
    ResponseEntity<String> response = userController.updateEmail("anything", "anything");

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void updatePassword_returns401_ifExistingPasswordNotCorrect() throws MessagingException {
    // given
    User user = User.builder().build();
    when(mockedUserService.getCurrentUser()).thenReturn(user);

    String veryStrongPassword = "verystrongpasswordsd";
    when(mockedUserService.passwordIsIncorrect(anyString())).thenReturn(true);
    UserController userController =
        new UserController(mockedUserService, mockPasswordEncoder, mockedEmailService);

    // when
    ResponseEntity<String> response =
        userController.updatePassword("StrongPassword007", veryStrongPassword);

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
          softly
              .assertThat(response.getBody())
              .isEqualTo(UserController.INCORRECT_PASSWORD_ERROR_MESSAGE);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"password", "fairpassword", "goodpassword1234", "strongpassword12345"})
  void updatePassword_returnsBadRequest_ifPasswordIsNotVeryStrong(String newPassword)
      throws MessagingException {
    // given
    User user = User.builder().build();
    when(mockedUserService.getCurrentUser()).thenReturn(user);
    doThrow(new PasswordTooWeakException("anything"))
        .when(mockedUserService)
        .changeUserPassword(any(), any());

    // when
    ResponseEntity<String> response = userController.updatePassword("anything", newPassword);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void register_throwsBadRequest_whenEmailTaken() {
    when(mockedUserService.register(any(UserToRegisterDto.class)))
        .thenThrow(new UserAlreadyRegisteredException("Taken"));

    ResponseEntity<Object> response =
        userController.register(new UserToRegisterDto("a@b.com", "b"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void register__should_check_if_email_service_is_called() throws MessagingException {
    UserToRegisterDto userDTO = new UserToRegisterDto("michael@jackson.com", "jacksonMichael@123");

    when(mockedEmailService.getUsernameFromEmail(userDTO.getUsername()))
        .thenReturn("michael jackson");

    userController.register(new UserToRegisterDto("michael@jackson.com", "jacksonMichael@123"));

    verify(mockedEmailService, times(1))
        .sendMessageUsingThymeleafTemplate(
            userDTO.getUsername(),
            EmailConstant.ACCOUNT_CREATED_SUBJECT,
            EmailTemplate.getAccountCreatedEmailTemplate(
                mockedEmailService.getUsernameFromEmail(userDTO.getUsername())));
  }
}

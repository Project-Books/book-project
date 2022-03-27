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

import com.karankumar.bookproject.account.dto.UserToDeleteDto;
import com.karankumar.bookproject.account.dto.UserToRegisterDto;
import com.karankumar.bookproject.account.exception.CurrentUserNotFoundException;
import com.karankumar.bookproject.account.exception.IncorrectPasswordException;
import com.karankumar.bookproject.account.exception.PasswordTooWeakException;
import com.karankumar.bookproject.account.exception.UserAlreadyRegisteredException;
import com.karankumar.bookproject.Mappings;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import com.karankumar.bookproject.book.service.EmailService;
import com.karankumar.bookproject.constant.EmailConstant;
import com.karankumar.bookproject.template.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(Mappings.USER)
public class UserController {
  public static final String INCORRECT_PASSWORD_ERROR_MESSAGE =
      "The current password entered is incorrect";

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Could not find the user with ID %d";
  public static final String CURRENT_USER_NOT_FOUND_ERROR_MESSAGE =
      "Could not determine the current user";
  private static final String PASSWORD_WEAK_ERROR_MESSAGE = "Password is too weak";
  private static final String EMAIL_NOT_FOUND = "Email is not registered with us";

  @Autowired
  public UserController(
      UserService userService, PasswordEncoder passwordEncoder, EmailService emailService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  @GetMapping("/user/{id}")
  public User getUser(@PathVariable Long id) {
    return userService
        .findUserById(id)
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_ERROR_MESSAGE, id)));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<String> emailExists(@PathVariable String email) {
    Optional<User> user = userService.findUserByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EMAIL_NOT_FOUND);
    }

    return ResponseEntity.status(HttpStatus.OK).body("Success");
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Object> register(@RequestBody UserToRegisterDto user) {
    try {
      userService.register(user);

      emailService.sendMessageUsingThymeleafTemplate(
          user.getUsername(),
          EmailConstant.ACCOUNT_CREATED_SUBJECT,
          EmailTemplate.getAccountCreatedEmailTemplate(
              emailService.getUsernameFromEmail(user.getUsername())));

      return ResponseEntity.status(HttpStatus.OK).body("User created");
    } catch (UserAlreadyRegisteredException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              "That email address is taken. If you already have an account, "
                  + "you can try logging in.");
    } catch (ConstraintViolationException ex) {
      Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
      List<String> errors = new ArrayList<>();
      for (ConstraintViolation<?> v : violations) {
        errors.add(v.getMessage());
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    } catch (MessagingException e) {
      LOGGER.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCurrentUser(@RequestBody UserToDeleteDto user) throws MessagingException {
    String password = user.getPassword();
    if (passwordEncoder.matches(password, userService.getCurrentUser().getPassword())) {
      User userEntity = userService.getCurrentUser();
      if (userEntity == null || userEntity.getId() == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }
      userService.deleteUserById(userEntity.getId());
      emailService.sendMessageUsingThymeleafTemplate(
          userEntity.getEmail(),
          EmailConstant.ACCOUNT_DELETED_SUBJECT,
          EmailTemplate.getAccountDeletedEmailTemplate(
              emailService.getUsernameFromEmail(userEntity.getEmail())));
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password.");
    }
  }

  @PostMapping("/update-email")
  public ResponseEntity<String> updateEmail(
      @RequestParam("newEmail") String newEmail,
      @RequestParam("currentPassword") String currentPassword) {
    User user;

    try {
      user = userService.getCurrentUser();
    } catch (CurrentUserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CURRENT_USER_NOT_FOUND_ERROR_MESSAGE);
    }

    try {
      userService.changeUserEmail(user, currentPassword, newEmail);
    } catch (IncorrectPasswordException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INCORRECT_PASSWORD_ERROR_MESSAGE);
    } catch (ConstraintViolationException | UserAlreadyRegisteredException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    return ResponseEntity.status(HttpStatus.OK).body("Updated");
  }

  @PostMapping("/update-password")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> updatePassword(
      @RequestParam("currentPassword") String currentPassword,
      @RequestParam("newPassword") String newPassword)
      throws MessagingException {
    User user;

    try {
      user = userService.getCurrentUser();
    } catch (CurrentUserNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CURRENT_USER_NOT_FOUND_ERROR_MESSAGE);
    }

    if (userService.passwordIsIncorrect(currentPassword)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INCORRECT_PASSWORD_ERROR_MESSAGE);
    }

    try {
      userService.changeUserPassword(user, newPassword);
      emailService.sendMessageUsingThymeleafTemplate(
          user.getEmail(),
          EmailConstant.ACCOUNT_PASSWORD_CHANGED_SUBJECT,
          EmailTemplate.getChangePasswordEmailTemplate(
              emailService.getUsernameFromEmail(user.getEmail())));
    } catch (PasswordTooWeakException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PASSWORD_WEAK_ERROR_MESSAGE);
    } catch (ConstraintViolationException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    return ResponseEntity.status(HttpStatus.OK).body("Updated");
  }
}

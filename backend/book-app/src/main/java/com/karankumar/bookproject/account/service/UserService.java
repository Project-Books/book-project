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

package com.karankumar.bookproject.account.service;

import com.karankumar.bookproject.account.constraint.PasswordStrength;
import com.karankumar.bookproject.account.dto.UserToRegisterDto;
import com.karankumar.bookproject.account.exception.CurrentUserNotFoundException;
import com.karankumar.bookproject.account.exception.IncorrectPasswordException;
import com.karankumar.bookproject.account.exception.PasswordTooWeakException;
import com.karankumar.bookproject.account.exception.UserAlreadyRegisteredException;
import com.karankumar.bookproject.account.repository.RoleRepository;
import com.karankumar.bookproject.account.repository.UserRepository;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.account.model.RoleType;
import com.karankumar.bookproject.account.model.Role;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.book.repository.BookRepository;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.nulabinc.zxcvbn.Zxcvbn;
import lombok.NonNull;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final BookRepository bookRepository;
  private final PredefinedShelfService predefinedShelfService;

  public static final String USER_NOT_FOUND_ERROR_MESSAGE = "Could not find the user with ID %d";
  public static final int MAX_FAILED_ATTEMPTS = 3;
  public static final long LOCK_TIME_DURATION = Duration.ofHours(24).toSeconds();

  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      @Lazy PredefinedShelfService predefinedShelfService,
      BookRepository bookRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.predefinedShelfService = predefinedShelfService;
    this.bookRepository = bookRepository;
  }

  public User register(@NonNull UserToRegisterDto userToRegisterDto)
      throws UserAlreadyRegisteredException {
    User userToRegister =
        User.builder()
            .email(userToRegisterDto.getUsername())
            .password(userToRegisterDto.getPassword())
            .build();

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<User>> constraintViolations = validator.validate(userToRegister);

    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(constraintViolations);
    }

    if (userToRegister.getEmail() != null && isEmailInUse(userToRegister.getEmail())) {
      throw new UserAlreadyRegisteredException(
          "A user with the email address " + userToRegister.getEmail() + " already exists");
    }

    userRepository.save(createNewUser(userToRegister));
    authenticateUser(userToRegister);
    return userToRegister;
  }

  private User createNewUser(User user) {
    Role userRole =
        roleRepository
            .findByRole(RoleType.USER.toString())
            .orElseThrow(
                () ->
                    new AuthenticationServiceException("The default user role could not be found"));
    return User.builder()
        .email(user.getEmail())
        .password(passwordEncoder.encode(user.getPassword()))
        .active(true)
        .roles(Set.of(userRole))
        .build();
  }

  public User getCurrentUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new CurrentUserNotFoundException("Current user could not be found"));
  }

  // TODO: this can be removed once we are no longer populating test data
  public List<User> findAll() {
    return userRepository.findAll();
  }

  public Optional<User> findUserById(@NonNull Long id) {
    return userRepository.findById(id);
  }

  public Optional<User> findUserByEmail(@NonNull String userEmail) {
    return userRepository.findByEmail(userEmail);
  }

  private void authenticateUser(User user) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    Authentication authResult =
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    if (authResult.isAuthenticated()) {
      SecurityContextHolder.getContext().setAuthentication(authResult);
    }
  }

  public boolean isEmailInUse(@NonNull String email) {
    return userRepository.findByEmail(email).isPresent();
  }

  public void changeUserEmail(
      @NonNull User user, @NonNull String currentPassword, @NonNull String email) {
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new IncorrectPasswordException("The password you entered is incorrect");
    }

    if (user.getEmail().equalsIgnoreCase(email)) {
      throw new UserAlreadyRegisteredException(
          "The email address you provided is the same as your current one.");
    }

    user.setEmail(email);

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(constraintViolations);
    }

    if (user.getEmail() != null && isEmailInUse(user.getEmail())) {
      throw new UserAlreadyRegisteredException(
          "A user with the email address " + user.getEmail() + " already exists");
    }

    userRepository.save(user);
  }

  /**
   * Determine the if a password has the strength required
   *
   * @param password the string to check its strength
   * @return true if password
   */
  public boolean isPasswordStrengthVeryStrong(String password) {
    return new Zxcvbn().measure(password).getScore()
        >= PasswordStrength.VERY_STRONG.getStrengthNum();
  }

  private boolean isPasswordTooWeak(String password) {
    return !isPasswordStrengthVeryStrong(password);
  }

  public void changeUserPassword(@NonNull User user, @NonNull String newPassword) {
    if (isPasswordTooWeak(newPassword)) {
      throw new PasswordTooWeakException("Password strength is too weak.");
    }
    user.setPassword(newPassword);

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(constraintViolations);
    }

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);
    userRepository.save(user);
  }

  public void deleteUserById(@NonNull Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      // TODO: make the temporal coupling explicit -- this needs to be called before
      // bookRepository.deleteAll()
      removePredefinedShelfFromUserBooks();

      bookRepository.deleteAll();
      userRepository.deleteById(id);
    } else {
      // TODO: throw custom exception.
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_ERROR_MESSAGE, id));
    }
  }

  public boolean passwordIsIncorrect(String password) {
    return !passwordEncoder.matches(getCurrentUser().getPassword(), password);
  }

  private void removePredefinedShelfFromUserBooks() {
    List<PredefinedShelf> predefinedShelves = predefinedShelfService.findAllForLoggedInUser();

    // Add all of the books in each predefined shelf to this set outside of the loop to
    // avoid a concurrent modification exception
    Set<Book> outerBooks = new HashSet<>();
    for (PredefinedShelf p : predefinedShelves) {
      p.removeUser();
      outerBooks.addAll(p.getBooks());
    }

    outerBooks.forEach(Book::removePredefinedShelf);
  }

  public User increaseFailAttempts(User user) {
    int failedAttempts = user.getFailedAttempts();
    user.setFailedAttempts(failedAttempts + 1);

    return userRepository.save(user);
  }

  public User resetFailAttempts(User user) {
    user.setFailedAttempts(0);
    return userRepository.save(user);
  }

  public void lock(User user) {
    user.setLocked(true);
    user.setLockTime(LocalDateTime.now());

    userRepository.save(user);
  }

  public long hoursUntilUnlock(User user) {
    return ChronoUnit.HOURS.between(
        LocalDateTime.now(), user.getLockTime().plusSeconds(LOCK_TIME_DURATION));
  }

  public boolean unlockWhenTimeExpired(User user) {
    long elapsedTimeInSeconds = ChronoUnit.SECONDS.between(user.getLockTime(), LocalDateTime.now());

    boolean shouldUnlock = elapsedTimeInSeconds > LOCK_TIME_DURATION;
    if (shouldUnlock) {
      user.setLocked(false);
      user.setFailedAttempts(0);
      userRepository.save(user);
    }

    return shouldUnlock;
  }
}

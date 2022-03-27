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

package com.karankumar.bookproject.book.service;

import static com.karankumar.bookproject.account.service.UserService.USER_NOT_FOUND_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.karankumar.bookproject.account.exception.IncorrectPasswordException;
import com.karankumar.bookproject.account.exception.UserAlreadyRegisteredException;
import com.karankumar.bookproject.account.service.UserService;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.book.repository.BookRepository;
import com.karankumar.bookproject.account.repository.RoleRepository;
import com.karankumar.bookproject.account.repository.UserRepository;
import java.util.Optional;

import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  private UserService underTest;

  @Mock private PasswordEncoder mockPasswordEncoder;
  @Mock private RoleRepository roleRepository;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private UserRepository mockUserRepository;
  @Mock private BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    bookRepository = mock(BookRepository.class);
    PredefinedShelfService predefinedShelfService = mock(PredefinedShelfService.class);
    underTest =
        new UserService(
            mockUserRepository,
            roleRepository,
            mockPasswordEncoder,
            authenticationManager,
            predefinedShelfService,
            bookRepository);
  }

  @Test
  void register_throwsNullPointerException_ifUserIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.register(null));
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void findUserById_searchesRepository() {
    long id = 1;
    underTest.findUserById(id);
    verify(mockUserRepository, times(1)).findById(id);
  }

  @Test
  void findUserById_throwsNullPointerException_ifIdIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.findUserById(null));
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void findUserByEmail_searchesRepository() {
    String email = "test123@test.com";
    underTest.findUserByEmail(email);
    verify(mockUserRepository, times(1)).findByEmail(email);
  }

  @Test
  void findUserByEmail_throwsNullPointerException_ifEmailIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.findUserByEmail(null));
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void isEmailInUse_throwsNullPointerException_ifEmailIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.isEmailInUse(null));
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void returnTrueIfEmailInUse() {
    // given
    given(mockUserRepository.findByEmail(anyString())).willReturn(Optional.of(mock(User.class)));

    // when
    boolean emailInUse = underTest.isEmailInUse("test@gmail.com");

    // then
    assertThat(emailInUse).isTrue();
  }

  @Test
  void returnFalseIfEmailInUse() {
    // given
    given(mockUserRepository.findByEmail(anyString())).willReturn(Optional.empty());

    // when
    boolean emailInUse = underTest.isEmailInUse("test@gmail.com");

    // then
    assertThat(emailInUse).isFalse();
  }

  @Test
  void changeUserEmail_throwsIncorrectPasswordException_ifPasswordIncorrect() {
    User user = User.builder().password("anything").build();

    when(mockPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

    assertThatExceptionOfType(IncorrectPasswordException.class)
        .isThrownBy(() -> underTest.changeUserEmail(user, "a", "email@email.com"))
        .withMessage("The password you entered is incorrect");
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void changeUserEmail_throwsUserAlreadyRegisteredException_ifPasswordIncorrect() {
    String email = "email@email.com";
    User user = User.builder().email(email).password("anything").build();

    when(mockPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

    assertThatExceptionOfType(UserAlreadyRegisteredException.class)
        .isThrownBy(() -> underTest.changeUserEmail(user, "a", email))
        .withMessage("The email address you provided is the same as your current one.");
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void changeUserPassword_throwsNullPointerException_ifUserIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.changeUserPassword(null, "test"));
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void changeUserPassword_throwsNullPointerException_ifPasswordIsNull() {
    User user = User.builder().build();
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.changeUserPassword(user, null));
    then(mockUserRepository).shouldHaveNoInteractions();
  }

  @Test
  void changeUserPassword_encodesPassword_beforeSaving() {
    // given
    String veryStrongPassword = "VeryStrongPassword007";
    final String email = "test@gmail.com";

    // when
    underTest.changeUserPassword(User.builder().email(email).build(), veryStrongPassword);

    // then
    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    verify(mockUserRepository).save(userArgumentCaptor.capture());
    User expected =
        User.builder()
            .email(email)
            .password(mockPasswordEncoder.encode(veryStrongPassword))
            .build();
    assertThat(userArgumentCaptor.getValue()).isEqualTo(expected);
  }

  @Test
  void deleteUserById_deletesUser_ifUserExists() {
    // given
    User user = User.builder().build();
    given(mockUserRepository.findById(anyLong())).willReturn(Optional.of(user));
    Long expectedId = 1L;

    // when
    underTest.deleteUserById(expectedId);

    // then
    verify(bookRepository).deleteAll();

    ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(mockUserRepository).deleteById(longArgumentCaptor.capture());
    Long actual = longArgumentCaptor.getValue();
    assertThat(actual).isEqualTo(expectedId);
  }

  @Test
  void deleteUserById_throwsNotFound_IfUserDoesNotExist() {
    given(mockUserRepository.findById(anyLong())).willReturn(Optional.empty());
    Long id = 1L;
    String expectedMessage = String.format(USER_NOT_FOUND_ERROR_MESSAGE, id);

    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> underTest.deleteUserById(id))
        .withMessageContaining(expectedMessage);
    then(bookRepository).shouldHaveNoInteractions();
    then(mockUserRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  void resetFailAttempts_setsFailedAttemptsToZero() {
    // given
    User user = mock(User.class);

    // when
    underTest.resetFailAttempts(user);

    // then
    verify(user, times(1)).setFailedAttempts(0);
    verify(mockUserRepository, times(1)).save(any(User.class));
  }

  @Test
  void lock_setsToTrue_andSetsLockTime() {
    User user = mock(User.class);

    underTest.lock(user);

    verify(user, times(1)).setLocked(true);
    verify(user, times(1)).setLockTime(any());
    verify(mockUserRepository, times(1)).save(user);
  }
}

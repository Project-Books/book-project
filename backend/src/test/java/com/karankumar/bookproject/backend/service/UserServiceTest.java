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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.karankumar.bookproject.backend.service.UserService.USER_NOT_FOUND_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userTest;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Mock private RoleRepository roleRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;


    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        PredefinedShelfService predefinedShelfService = mock(PredefinedShelfService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder,
                authenticationManager,
                predefinedShelfService,
                bookRepository
        );
    }

    @Test
    void register_throwsNullPointerException_ifUserIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.register(null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void isEmailInUse_throwsNullPointerException_ifEmailIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.isEmailInUse(null));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void returnTrueIfEmailInUse() {
        // given
        User user = User.builder().build();
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        // when
        boolean emailInUse = underTest.isEmailInUse("test@gmail.com");

        // then
        assertThat(emailInUse).isTrue();
    }

    @Test
    void returnFalseIfEmailInUse() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when
        boolean emailInUse = underTest.isEmailInUse("test@gmail.com");

        // then
        assertThat(emailInUse).isFalse();
    }

    @Test
    void changeUserPassword_throwsNullPointerException_ifUserIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.changeUserPassword(null, "test"));
    }

    @Test
    void changeUserPassword_throwsNullPointerException_ifPasswordIsNull() {
        User user = User.builder().build();
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> underTest.changeUserPassword(user, null));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeUserPassword_encodesPassword_beforeSaving() {
        // given
        String password = "password";

        // when
        underTest.changeUserPassword(User.builder().build(), password);

        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User expected = User.builder()
                            .password(passwordEncoder.encode(password))
                            .build();
        assertThat(userArgumentCaptor.getValue()).isEqualTo(expected);

    }

      @Test
      void deleteUserById_deletesUser_ifUserExists() {
          // given
          User user = User.builder().build();
          given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

          // when
          userService.deleteUserById(1L);

          // then
          verify(bookRepository).deleteAll();
          verify(userRepository).deleteById(anyLong());
      }

      @Test
      void deleteUserById_throwsNotFound_IfUserDoesNotExist() {
          given(userRepository.findById(anyLong())).willReturn(Optional.empty());
          Long id = 1L;
          String expectedMessage = String.format(USER_NOT_FOUND_ERROR_MESSAGE, id);

          assertThatExceptionOfType(ResponseStatusException.class)
                  .isThrownBy(() -> userService.deleteUserById(id))
                  .withMessageContaining(expectedMessage);
          verify(bookRepository, never()).deleteAll();
          verify(userRepository, never()).deleteById(anyLong());
      }
  }

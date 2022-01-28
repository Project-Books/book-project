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

package com.karankumar.bookproject.controller;

import com.karankumar.bookproject.dto.UserToRegisterDto;
import com.karankumar.bookproject.model.account.User;
import com.karankumar.bookproject.service.PasswordTooWeakException;
import com.karankumar.bookproject.service.UserAlreadyRegisteredException;
import com.karankumar.bookproject.service.UserService;
import com.karankumar.bookproject.service.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;

@SpringBootTest(classes = {UserService.class, PasswordEncoder.class})
@ActiveProfiles("test")
class UserControllerTest {
  
   @MockBean UserService userService;

    private final UserController userController;
    private final UserService mockedUserService;
    private final EmailServiceImpl mockedEmailService;
    private final PasswordEncoder mockPasswordEncoder;

    private final User validUser = User.builder()
                                       .email("valid@testmail.com")
                                       .password("aaaaAAAA1234@")
                                       .build();

    UserControllerTest() {
        mockedUserService = mock(UserService.class);
        mockedEmailService = mock(EmailServiceImpl.class);
        mockPasswordEncoder = mock(PasswordEncoder.class);
        userController = new UserController(
                mockedUserService, mockPasswordEncoder, mockedEmailService,
                mock(Environment.class)
        );
    }

    @Test
    void getAllUsers_returnsEmptyList_whenNoUsersExist() {
        when(mockedUserService.findAll()).thenReturn(new ArrayList<>());
        assertThat(userController.getAllUsers().size()).isZero();
    }

    @Test
    void getAllUsers_returnNonEmptyList_whenUserExist() {
        // given
        List<User> users = new ArrayList<>();
        users.add(validUser);
        users.add(validUser);

        // when
        when(mockedUserService.findAll()).thenReturn(users);

        // then
        assertThat(userController.getAllUsers().size()).isEqualTo(users.size());
    }

    @Test
    void getUser_returnsUser_ifPresent() {
        User user = validUser;
        when(mockedUserService.findUserById(any(Long.class)))
            .thenReturn(Optional.of(user));

        assertThat(userController.getUser(0L)).isEqualTo(user);
    }

    @Test
    void getUser_returnsNotFound_ifUserIsEmpty() {
        when(mockedUserService.findUserById(any(Long.class)))
            .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
            .isThrownBy(() -> userController.getUser(0L));
    }
  
    @Test
    void updatePassword_returnsUnauthorised_ifExistingPasswordNotCorrect() throws MessagingException {
        // given
        User user = User.builder().build();
        when(mockedUserService.getCurrentUser()).thenReturn(user);

        String veryStrongPassword = "verystrongpasswordsd";
        when(mockedUserService.passwordIsIncorrect(anyString())).thenReturn(true);
        UserController userController = new UserController(
                mockedUserService, mockPasswordEncoder, mockedEmailService, mock(Environment.class)
        );

        // when
        ResponseEntity<String> response = userController.updatePassword(
                "StrongPassword007", veryStrongPassword
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
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
        ResponseEntity<String> response = userController.updatePassword(
                "anything", newPassword
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void register_throwsBadRequest_whenEmailTaken() {
        when(mockedUserService.register(any(UserToRegisterDto.class)))
                .thenThrow(new UserAlreadyRegisteredException("Taken"));

        ResponseEntity<Object> response = userController.register(
                new UserToRegisterDto("a@b.com", "b")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

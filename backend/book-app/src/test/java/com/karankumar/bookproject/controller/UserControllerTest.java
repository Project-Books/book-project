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

import com.karankumar.bookproject.model.account.User;
import com.karankumar.bookproject.service.EmailServiceImpl;
import com.karankumar.bookproject.service.UserService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {UserService.class, PasswordEncoder.class})
@ActiveProfiles("test")
class UserControllerTest {
  
   @MockBean UserService userService;
   @MockBean PasswordEncoder passwordEncoder;
  
    private final UserController userController;
    private final UserService mockedUserService;
    private final EmailServiceImpl mockedEmailService;

    private final User validUser = User.builder()
                                       .email("valid@testmail.com")
                                       .password("aaaaAAAA1234@")
                                       .build();
                                       
    private final User validUser2 = User.builder()
                                       .email("valid2@testmail.com")
                                       .password("aaaaAAAA1234@")
                                       .build();

    UserControllerTest() {
        mockedUserService = mock(UserService.class);
        mockedEmailService = mock(EmailServiceImpl.class);
        PasswordEncoder mockedPasswordEncoder = mock(PasswordEncoder.class);
        userController = new UserController(mockedUserService, mockedPasswordEncoder, mockedEmailService);
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
    void updatePassword_returnsUnauthorised_ifExistingPasswordNotCorrect() {
        // given
        User user = User.builder().build();
        Mockito.when(userService.getCurrentUser())
               .thenReturn(user);

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(false);
        UserController userController = new UserController(userService, passwordEncoder, mockedEmailService);

        String expectedMessage = String.format(
                "%s \"%s\"",
                HttpStatus.UNAUTHORIZED,
                UserController.INCORRECT_PASSWORD_ERROR_MESSAGE
        );

        // when
        ThrowableAssert.ThrowingCallable callable =
                () -> userController.updatePassword("current", "new");

        // then
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(callable)
                .withMessage(expectedMessage);
    }

    @Test
    @Disabled
    void register_throwsBadRequest_whenEmailTaken() {
        // TODO: implement
    }
}

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

package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserService.class, PasswordEncoder.class})
@ActiveProfiles("test")
class UserControllerTest {
    @MockBean UserService userService;

    @MockBean PasswordEncoder passwordEncoder;

    @Test
    void updatePassword_returnsUnauthorised_ifExistingPasswordNotCorrect() {
        // given
        User user = User.builder().build();
        Mockito.when(userService.getCurrentUser())
               .thenReturn(user);

        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(false);
        UserController userController = new UserController(userService, passwordEncoder);

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

    /*
    1. valid email addser> findByEmail(String email);
}
r. form xxx@xxx.xxx - service layer
    2. does not match with the existing email addr.
     */
    @Test
    void changeEmailAddress_emailAddressChanged_ifUserExist() {
        // Given
        String oldEmail = "user1@user.user";
        User user = User.builder()
                        .email(oldEmail)
                        .build();
        Optional<User> optionalUser = Optional.of(user);
        when(userService.findByEmail(any(String.class))).thenReturn(optionalUser);
        UserController userController = new UserController(userService, passwordEncoder);

        // When
        String newEmail = "user2@user.user";
        User actualUser = userController.changeEmailAddress(oldEmail, newEmail);

        // Then
        assertThat(actualUser.getEmail()).isEqualTo(newEmail);
    }
}

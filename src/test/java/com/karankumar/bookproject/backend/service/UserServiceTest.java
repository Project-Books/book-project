/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.karankumar.bookproject.util.SecurityTestUtils.TEST_USER_NAME;
import static com.karankumar.bookproject.util.SecurityTestUtils.getTestUser;
import static com.karankumar.bookproject.util.SecurityTestUtils.insertTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@Transactional
@DisplayName("UserService should")
class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final User validUser = User.builder()
                                       .username("validUser")
                                       .email("valid@testmail.com")
                                       .password("aaaaAAAA1234@")
                                       .build();

    @Autowired
    UserServiceTest(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Test
    void throwExceptionOnRegisterWithBeanViolations() {
        final User invalidUser = User.builder()
                                     .username("testuser")
                                     .email("testmail")
                                     .password("invalidpassword")
                                     .build();

        assertThatThrownBy(() -> userService.register(invalidUser))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void throwExceptionOnRegisterWithUsernameTaken() {
        userRepository.save(validUser);
        validUser.setEmail("anotherEmail@testmail.com");

        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    void throwExceptionOnRegisterWithEmailTaken() {
        userRepository.save(validUser);
        validUser.setUsername("anotherUsername");

        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    void throwExceptionOnRegisterWithoutUserRole() {
        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(AuthenticationServiceException.class);
    }

    @Test
    void registerValidUser() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(validUser);

        assertThat(userRepository.findByUsername(validUser.getUsername())).isPresent();
    }

    @Test
    void logUserInAfterRegister() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(validUser);

        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
    }

    @Test
    void correctlyReportUsernameIsNotInUse() {
        assertThat(userService.usernameIsInUse("notAtestuser")).isFalse();
    }

    @Test
    void correctlyReportUsernameIsInUse() {
        userRepository.save(validUser);
        assertThat(userService.usernameIsInUse(validUser.getUsername())).isTrue();
    }

    @Test
    void correctlyReportIfUserNameIsNotInUseWithUsernameNotInUse() {
        assertThat(userService.usernameIsNotInUse("testuser")).isTrue();
    }

    @Test
    @Transactional
    void correctlyReportIfUsernameIsNotInUseWithUsernameInUse() {
        userRepository.save(validUser);

        assertThat(userService.usernameIsNotInUse(validUser.getUsername())).isFalse();
    }

    @Test
    void correctlyReportEmailIsNotInUse() {
        assertThat(userService.emailIsInUse("testmail")).isFalse();
    }

    @Test
    void correctlyReportEmailIsInUse() {
        userRepository.save(validUser);

        assertThat(userService.emailIsInUse(validUser.getEmail())).isTrue();
    }

    @Test
    void checkIfEmailIsNotInUseWithEmailNotInUse() {
        assertThat(userService.emailIsNotInUse("testmail")).isTrue();
    }

    @Test
    void checkIfEmailIsNotInUseWithEmailInUse() {
        userRepository.save(validUser);

        assertThat(userService.emailIsNotInUse(validUser.getEmail())).isFalse();
    }

    @Test
    void getLoggedUser() {
        Optional<User> dbUser = userRepository.findByUsername(TEST_USER_NAME);
        User currentUser = userService.getCurrentUser();

        assertThat(currentUser.getUsername()).isEqualTo(TEST_USER_NAME);
        assertThat(dbUser).isPresent().get().isEqualTo(currentUser);
    }

    @Test
    void findAll() {
        List<User> users = Arrays.asList(
                getTestUser(userRepository),
                insertTestUser(userRepository, "test1"),
                insertTestUser(userRepository, "test2"),
                insertTestUser(userRepository, "test3")
        );

        assertThat(userService.findAll()).containsAll(users);
    }
}

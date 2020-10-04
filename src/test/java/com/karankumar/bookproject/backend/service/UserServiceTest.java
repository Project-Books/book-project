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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
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
    UserServiceTest(UserService userService,
                    UserRepository userRepository,
                    RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void throwsExceptionWhenRegisterWithBeanViolations() {
        final User invalidUser = User.builder()
                                     .username("testuser")
                                     .email("testmail")
                                     .password("invalidpassword")
                                     .build();

        assertThrows(ConstraintViolationException.class, () -> userService.register(invalidUser));
    }

    @Test
    void throwsExceptionrWhenRegisterWithUsernameTaken() {
        userRepository.save(validUser);
        validUser.setEmail("anotherEmail@testmail.com");

        assertThrows(UserAlreadyRegisteredException.class, () -> userService.register(validUser));
    }

    @Test
    void throwsExceptionrWhenRegisterWithEmailTaken() {
        userRepository.save(validUser);
        validUser.setUsername("anotherUsername");

        assertThrows(UserAlreadyRegisteredException.class, () -> userService.register(validUser));
    }

    @Test
    void throwsErrorWhenRegisterWithoutUserRole() {
        assertThrows(AuthenticationServiceException.class, () -> userService.register(validUser));
    }

    @Test
    void registerAUser() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(validUser);

        assertThat(userRepository.findByUsername(validUser.getUsername()).isPresent(),
                equalTo(true));
    }

    @Test
    void logUserWhenRegister() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(validUser);

        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated(),
                equalTo(true));
    }

    @Test
    @Transactional
    void tellUserNameIsInUse() {
        assertThat(userService.usernameIsInUse("notAtestuser"), equalTo(false));

        userRepository.save(validUser);
        assertThat(userService.usernameIsInUse(validUser.getUsername()), equalTo(true));
    }

    @Test
    @Transactional
    void tellUserNameIsNotInUse() {
        assertThat(userService.usernameIsNotInUse("testuser"), equalTo(true));

        userRepository.save(validUser);
        assertThat(userService.usernameIsNotInUse(validUser.getUsername()), equalTo(false));
    }

    @Test
    @Transactional
    void tellEmailIsInUse() {
        assertThat(userService.emailIsInUse("testmail"), equalTo(false));

        userRepository.save(validUser);
        assertThat(userService.emailIsInUse(validUser.getEmail()), equalTo(true));
    }


    @Test
    @Transactional
    void telEmailIsNotInUse() {
        assertThat(userService.emailIsNotInUse("testmail"), equalTo(true));
        userRepository.save(validUser);

        assertThat(userService.emailIsNotInUse(validUser.getEmail()), equalTo(false));
    }
}

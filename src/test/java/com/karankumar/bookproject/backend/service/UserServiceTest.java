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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

@IntegrationTest
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
    void register_withBeanViolations_throwsException() {
        final User invalidUser = User.builder()
                                     .username("testuser")
                                     .email("testmail")
                                     .password("invalidpassword")
                                     .build();

        assertThatThrownBy(() -> userService.register(invalidUser))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void register_withUsernameTaken_throwsException() {
        userRepository.save(validUser);
        validUser.setEmail("anotherEmail@testmail.com");

        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    void register_withEmailTaken_throwsException() {
        userRepository.save(validUser);
        validUser.setUsername("anotherUsername");

        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    void register_withoutUserRole_throwsError() {
        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(AuthenticationServiceException.class);
    }

    @Test
    void register_registersUser() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(validUser);

        assertThat(userRepository.findByUsername(validUser.getUsername()).isPresent()).isTrue();
    }

    @Test
    void register_logsUserIn() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(validUser);

        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
    }

    @Test
    void usernameIsInUse_UsernameNotInUse_returnsFalse() {
        assertThat(userService.usernameIsInUse("notAtestuser")).isFalse();
    }

    @Test
    void usernameIsInUse_UsernameInUse_returnsTrue() {
        userRepository.save(validUser);
        assertThat(userService.usernameIsInUse(validUser.getUsername())).isTrue();
    }

    @Test
    void usernameIsNotInUse_UsernameNotInUse_returnsTrue() {
        assertThat(userService.usernameIsNotInUse("testuser")).isTrue();
    }

    @Test
    @Transactional
    void usernameIsNotInUse_UsernameInUse_returnsFalse() {
        userRepository.save(validUser);

        assertThat(userService.usernameIsNotInUse(validUser.getUsername())).isFalse();
    }

    @Test
    void emailIsInUse_EmailNotInUse_returnsFalse() {
        assertThat(userService.emailIsInUse("testmail")).isFalse();
    }

    @Test
    void emailIsInUse_EmailInUse_returnsTrue() {
        userRepository.save(validUser);

        assertThat(userService.emailIsInUse(validUser.getEmail())).isTrue();
    }

    @Test
    void emailIsNotInUse_EmailNotInUse_returnsTrue() {
        assertThat(userService.emailIsNotInUse("testmail")).isTrue();
    }

    @Test
    void emailIsNotInUse_EmailInUse_returnsFalse() {
        userRepository.save(validUser);

        assertThat(userService.emailIsNotInUse(validUser.getEmail())).isFalse();
    }
}

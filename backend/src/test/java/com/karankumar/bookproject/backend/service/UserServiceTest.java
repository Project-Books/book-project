/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

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

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.model.account.Role;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.h2.engine.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.karankumar.bookproject.util.SecurityTestUtils.TEST_USER_EMAIL;
import static com.karankumar.bookproject.util.SecurityTestUtils.getTestUser;
import static com.karankumar.bookproject.util.SecurityTestUtils.insertTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@IntegrationTest
@Transactional
@DisplayName("UserService should")
class UserServiceTest {
    private final UserService userService;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    Authentication authentication;

    @MockBean
    UserRepository userRepository;

    private final User validUser = User.builder()
                                       .email("valid@testmail.com")
                                       .password("aaaaAAAA1234@")
                                       .build();

    @Autowired
    UserServiceTest(UserService userService, UserRepository userRepository//,
                    /*RoleRepository roleRepository*/) {
        this.userService = userService;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
    }

    @Test
    void throwExceptionOnRegisterWithBeanViolations() {
        final User invalidUser = User.builder()
                                     .email("testmail")
                                     .password("invalidpassword")
                                     .build();

        assertThatThrownBy(() -> userService.register(invalidUser))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void throwExceptionOnRegisterWithEmailTaken() {
        userRepository.save(validUser);

        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(UserAlreadyRegisteredException.class);
    }

    @Test
    void throwExceptionOnRegisterWithoutUserRole() {
        assertThatThrownBy(() -> userService.register(validUser))
                .isInstanceOf(AuthenticationServiceException.class);
    }

    @Test
    @DisplayName("throw an exception on an attempt to register a null user")
    void throwExceptionWhenRegisteringNullUser() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.register(null));
    }

    @Test
    void registerValidUser() {
        // given
        roleRepository.save(new Role("USER"));

        // when
        userService.register(validUser);

        // then
        assertThat(userRepository.findByEmail(validUser.getEmail())).isPresent();
    }

    @Test
    void logUserInAfterRegister() {
        roleRepository.save(new Role("USER"));
        userService.register(validUser);

        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
                .isTrue();
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
        // given
        Optional<User> dbUser = userRepository.findByEmail(TEST_USER_EMAIL);

        // when
        User currentUser = userService.getCurrentUser();

        // then
        assertSoftly(softly -> {
            softly.assertThat(currentUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
            softly.assertThat(dbUser).isPresent().get().isEqualTo(currentUser);
        });
    }

    @Test
    void findAllUsers() {
        // given
        List<User> users = Arrays.asList(
                getTestUser(userRepository),
                insertTestUser(userRepository),
                insertTestUser(userRepository),
                insertTestUser(userRepository)
        );

        // when
        List<User> actual = userService.findAll();

        // then
        assertThat(actual).containsAll(users);
    }

//    @Test
//    void findUserById(){
//        //given
//        User user = getTestUser(userRepository);
//
//        List<User> users = Arrays.asList(
//            insertTestUser(userRepository),
//            getTestUser(userRepository),
//            insertTestUser(userRepository),
//            insertTestUser(userRepository)
//        );
//
//        //then
//        assertThat(userService.findUserById((long) 1)).hasValue(user);
//    }
//
      @Test
      // TODO: fix.
      void deleteUserByIdTest() {
          // given
          Role role = new Role("USER");
          roleRepository.save(role);
          when(roleRepository.findByRole(any(String.class))).thenReturn(Optional.of(role));
          User user = User.builder()
                          .email("test@domain.com")
                          .password("sldkfjslk53")
                          .roles(Set.of(role))
                          .build();

          when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                  .thenReturn(authentication);
          when(authentication.isAuthenticated()).thenReturn(true);

          when(userRepository.save(any(User.class))).thenReturn(user);
          User savedUser = userService.register(user);
          Long id = savedUser.getId();
//          UserRepository userRepository = Mockito.mock(UserRepository.class);
          when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(savedUser));

          // when
          userService.deleteUserById(id);
          
          // then
          assertThat(userService.findAll().size()).isZero();
      }
  }

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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.karankumar.bookproject.backend.service.UserService.USER_NOT_FOUND_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;

    @Mock private RoleRepository roleRepository;
    @Mock private AuthenticationManager authenticationManager;
//    @Mock private Authentication authentication;
    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;

//    private final User validUser = User.builder()
//                                       .email("valid@testmail.com")
//                                       .password("aaaaAAAA1234@")
//                                       .build();

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

//    @Test
//    void throwExceptionOnRegisterWithBeanViolations() {
//        final User invalidUser = User.builder()
//                                     .email("testmail")
//                                     .password("invalidpassword")
//                                     .build();
//
//        assertThatThrownBy(() -> userService.register(invalidUser))
//                .isInstanceOf(ConstraintViolationException.class);
//    }
//
//    @Test
//    void throwExceptionOnRegisterWithEmailTaken() {
//        userRepository.save(validUser);
//
//        assertThatThrownBy(() -> userService.register(validUser))
//                .isInstanceOf(UserAlreadyRegisteredException.class);
//    }
//
//    @Test
//    void throwExceptionOnRegisterWithoutUserRole() {
//        assertThatThrownBy(() -> userService.register(validUser))
//                .isInstanceOf(AuthenticationServiceException.class);
//    }
//
//    @Test
//    @DisplayName("throw an exception on an attempt to register a null user")
//    void throwExceptionWhenRegisteringNullUser() {
//        assertThatExceptionOfType(NullPointerException.class)
//                .isThrownBy(() -> userService.register(null));
//    }
//
//    @Test
//    void registerValidUser() {
//        // given
//        roleRepository.save(new Role("USER"));
//
//        // when
//        userService.register(validUser);
//
//        // then
//        assertThat(userRepository.findByEmail(validUser.getEmail())).isPresent();
//    }
//
//    @Test
//    void logUserInAfterRegister() {
//        roleRepository.save(new Role("USER"));
//        userService.register(validUser);
//
//        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
//                .isTrue();
//    }
//
//    @Test
//    void correctlyReportEmailIsNotInUse() {
//        assertThat(userService.emailIsInUse("testmail")).isFalse();
//    }
//
//    @Test
//    void correctlyReportEmailIsInUse() {
//        userRepository.save(validUser);
//
//        assertThat(userService.emailIsInUse(validUser.getEmail())).isTrue();
//    }
//
//    @Test
//    void checkIfEmailIsNotInUseWithEmailNotInUse() {
//        assertThat(userService.emailIsNotInUse("testmail")).isTrue();
//    }
//
//    @Test
//    void checkIfEmailIsNotInUseWithEmailInUse() {
//        userRepository.save(validUser);
//
//        assertThat(userService.emailIsNotInUse(validUser.getEmail())).isFalse();
//    }
//
//    @Test
//    void getLoggedUser() {
//        // given
//        Optional<User> dbUser = userRepository.findByEmail(TEST_USER_EMAIL);
//
//        // when
//        User currentUser = userService.getCurrentUser();
//
//        // then
//        assertSoftly(softly -> {
//            softly.assertThat(currentUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
//            softly.assertThat(dbUser).isPresent().get().isEqualTo(currentUser);
//        });
//    }
//
//    @Test
//    void findAllUsers() {
//        // given
//        List<User> users = Arrays.asList(
//                getTestUser(userRepository),
//                insertTestUser(userRepository),
//                insertTestUser(userRepository),
//                insertTestUser(userRepository)
//        );
//
//        // when
//        List<User> actual = userService.findAll();
//
//        // then
//        assertThat(actual).containsAll(users);
//    }

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

/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2022  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.account.auth.service;

import com.karankumar.bookproject.account.auth.UserDetailsMapper;
import com.karankumar.bookproject.account.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseUserDetailsServiceTest {
  private final UserRepository mockUserRepository;
  private final DatabaseUserDetailsService underTest;

  DatabaseUserDetailsServiceTest() {
    mockUserRepository = mock(UserRepository.class);
    UserDetailsMapper mockUserDetailsMapper = mock(UserDetailsMapper.class);
    underTest = new DatabaseUserDetailsService(mockUserRepository, mockUserDetailsMapper);
  }

  @Test
  void loadUserByUsername_throwsUsernameNotFoundException_ifUserNotFound() {
    String email = "abc@test.com";
    String expectedErrorMessage = String.format("User with the email %s was not found.", email);

    when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> underTest.loadUserByUsername(email))
        .withMessage(expectedErrorMessage);
  }
}

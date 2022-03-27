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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseUserDetailsPasswordServiceTest {
  private final DatabaseUserDetailsPasswordService underTest;
  private final UserRepository mockUserRepository;

  DatabaseUserDetailsPasswordServiceTest() {
    mockUserRepository = mock(UserRepository.class);
    UserDetailsMapper userDetailsMapper = mock(UserDetailsMapper.class);
    underTest = new DatabaseUserDetailsPasswordService(mockUserRepository, userDetailsMapper);
  }

  @Test
  void updatePassword_throwsUsernameNotFoundException_ifUserNotFound() {
    when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("test");

    assertThatExceptionOfType(UsernameNotFoundException.class)
        .isThrownBy(() -> underTest.updatePassword(userDetails, "anything"));
  }
}

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

package com.karankumar.bookproject.account.auth;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomAuthenticationProviderTest {

  private final UserService mockUserService;
  private final CustomAuthenticationProvider underTest;

  CustomAuthenticationProviderTest() {
    mockUserService = mock(UserService.class);
    underTest = new CustomAuthenticationProvider(mockUserService);
  }

  @Test
  void authenticate_throwsLockedException_ifAccountLocked() {
    long zeroIndexedHours = 3L;
    User user = User.builder().locked(true).build();
    when(mockUserService.findUserByEmail(anyString())).thenReturn(Optional.of(user));
    when(mockUserService.hoursUntilUnlock(any(User.class))).thenReturn(zeroIndexedHours);
    when(mockUserService.unlockWhenTimeExpired(any(User.class))).thenReturn(true);

    Authentication mockAuth = mock(Authentication.class);
    when(mockAuth.getName()).thenReturn("test");

    // when/then
    String expectedMessage =
        String.format("User is locked. Please wait %d hours to unlock it.", zeroIndexedHours + 1);
    assertThatExceptionOfType(LockedException.class)
        .isThrownBy(() -> underTest.authenticate(mockAuth))
        .withMessage(expectedMessage);
  }
}

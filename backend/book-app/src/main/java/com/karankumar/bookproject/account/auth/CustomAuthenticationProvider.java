/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2021  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.account.auth;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
  private final UserService userService;

  public CustomAuthenticationProvider(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Optional<User> userOpt = userService.findUserByEmail(authentication.getName());

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (user.isLocked()) {
        boolean locked = userService.unlockWhenTimeExpired(user);

        if (locked) {
          long hoursToUnlock = userService.hoursUntilUnlock(user) + 1;
          String errorMessage =
              String.format("User is locked. Please wait %d hours to unlock it.", hoursToUnlock);
          throw new LockedException(errorMessage);
        }
      }
    }

    return super.authenticate(authentication);
  }
}

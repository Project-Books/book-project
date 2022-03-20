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

package com.karankumar.bookproject.account.auth.listener;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@ExcludeFromJacocoGeneratedReport
@Component
public class AuthenticationBadCredentialsListener
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
  private final UserService userService;

  public AuthenticationBadCredentialsListener(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    String userEmail = event.getAuthentication().getName();
    Optional<User> userOpt = userService.findUserByEmail(userEmail);

    if (userOpt.isPresent()) {
      User user = userService.increaseFailAttempts(userOpt.get());

      if (user.getFailedAttempts() >= UserService.MAX_FAILED_ATTEMPTS) {
        userService.lock(user);
      }
    }
  }
}

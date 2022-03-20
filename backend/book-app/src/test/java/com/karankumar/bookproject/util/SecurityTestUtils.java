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

package com.karankumar.bookproject.util;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.repository.UserRepository;

public class SecurityTestUtils {
  private SecurityTestUtils() {}

  // this must be the same as in resources/data.sql
  public static final String TEST_USER_EMAIL = "user@user.user";

  public static User getTestUser(UserRepository repository) {
    return repository.findByEmail(TEST_USER_EMAIL).orElseThrow();
  }

  public static User insertTestUser(UserRepository repository) {
    User user = User.builder().email(TEST_USER_EMAIL).password("testPa$$123_Paf1").build();
    return repository.save(user);
  }
}

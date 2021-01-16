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

import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.UserRepository;

public class SecurityTestUtils {
    private SecurityTestUtils() {}

    // this has to be the same value as in data.sql
    public static final String TEST_USER_NAME = "testUser";

    public static User getTestUser(UserRepository repository) {
        return repository.findByUsername(TEST_USER_NAME).orElseThrow();
    }

    public static User insertTestUser(UserRepository repository, String username) {
        User user = User.builder()
                .username(username)
                .email(username + "@user.user")
                .password("testPa$$123_Paf1")
                .build();
        return repository.save(user);
    }
}

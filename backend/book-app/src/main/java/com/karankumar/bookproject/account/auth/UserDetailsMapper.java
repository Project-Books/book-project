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

package com.karankumar.bookproject.account.auth;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toUnmodifiableList;

@ExcludeFromJacocoGeneratedReport
@Component
public class UserDetailsMapper {
  public User toUserDetails(com.karankumar.bookproject.account.model.User user) {
    return new User(
        user.getEmail(),
        user.getPassword(),
        user.isActive(),
        true,
        true,
        !user.isLocked(),
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole()))
            .collect(toUnmodifiableList()));
  }
}

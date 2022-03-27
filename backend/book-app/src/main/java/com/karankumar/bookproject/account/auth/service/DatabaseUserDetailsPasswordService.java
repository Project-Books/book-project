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

package com.karankumar.bookproject.account.auth.service;

import com.karankumar.bookproject.account.auth.UserDetailsMapper;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsPasswordService implements UserDetailsPasswordService {
  private final UserRepository userRepository;
  private final UserDetailsMapper userDetailsMapper;

  public DatabaseUserDetailsPasswordService(
      UserRepository userRepository, UserDetailsMapper userDetailsMapper) {
    this.userRepository = userRepository;
    this.userDetailsMapper = userDetailsMapper;
  }

  @Override
  public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
    User user =
        userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User with the email " + userDetails.getUsername() + " was not found."));
    user.setPassword(newPassword);
    return userDetailsMapper.toUserDetails(user);
  }
}

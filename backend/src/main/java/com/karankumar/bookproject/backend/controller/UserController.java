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

package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    public static final String INCORRECT_PASSWORD_ERROR_MESSAGE =
            "The current password entered is incorrect";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    //change email address
    @PutMapping("/change-email-address/{oldEmail}/{newEmail}")
    public User changeEmailAddress(@PathVariable("oldEmail") String oldEmail,
                                   @PathVariable("newEmail") String newEmail) {
        Optional<User> optionalUser = userService.findByEmail(oldEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(newEmail);
            return userService.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find the user");
        }
    }

    @PostMapping("/update-password")
    @ResponseStatus(HttpStatus.OK)
    public User updatePassword(@RequestParam("currentPassword") String currentPassword,
                               @RequestParam("newPassword") String newPassword) {
        User user = userService.getCurrentUser();

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            userService.changeUserPassword(user, newPassword);
            return user;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    INCORRECT_PASSWORD_ERROR_MESSAGE
            );
        }
    }
}

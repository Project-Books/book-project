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

import com.karankumar.bookproject.backend.dto.UserToDeleteDto;
import com.karankumar.bookproject.backend.dto.UserToRegisterDto;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.UserAlreadyRegisteredException;
import com.karankumar.bookproject.backend.service.CurrentUserNotFoundException;
import com.karankumar.bookproject.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(Mappings.USER)
public class UserController {
    public static final String INCORRECT_PASSWORD_ERROR_MESSAGE =
            "The current password entered is incorrect";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Could not find the user with ID %d";
    private static final String CURRENT_USER_NOT_FOUND_ERROR_MESSAGE = "Could not determine the current user";

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findUserById(id)
                          .orElseThrow(() ->
                                  new ResponseStatusException(HttpStatus.NOT_FOUND,
                                  String.format(USER_NOT_FOUND_ERROR_MESSAGE, id))
                          );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> register(@RequestBody UserToRegisterDto user) {
        try {
            userService.register(user);
            return ResponseEntity.status(HttpStatus.OK).body("user created");
        } catch (UserAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email taken");
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<?> v : violations) {
                errors.add(v.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }


    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser(@RequestBody UserToDeleteDto user) {
        String password = user.getPassword();
        if (passwordEncoder.matches(password, userService.getCurrentUser().getPassword())) {
            Long userId = userService.getCurrentUser().getId();
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            userService.deleteUserById(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password.");
        }
   }

    @PostMapping("/update-email")
    @ResponseStatus(HttpStatus.OK)
    public void updateEmail(@RequestParam("newEmail") String newEmail,
                            @RequestParam("currentPassword") String currentPassword) {

        User user;

        try {
            user = userService.getCurrentUser();
        } catch (CurrentUserNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    CURRENT_USER_NOT_FOUND_ERROR_MESSAGE
            );
        }

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            try {
                userService.changeUserEmail(user, newEmail);
            } catch (ConstraintViolationException | UserAlreadyRegisteredException ex) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                );
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    INCORRECT_PASSWORD_ERROR_MESSAGE
            );
        }
    }

    @PostMapping("/update-password")
    @ResponseStatus(HttpStatus.OK)
    public boolean updatePassword(@RequestParam("currentPassword") String currentPassword,
                               @RequestParam("newPassword") String newPassword) {
        User user = userService.getCurrentUser();

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            userService.changeUserPassword(user, newPassword);
            return true;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    INCORRECT_PASSWORD_ERROR_MESSAGE
            );
        }
    }
}

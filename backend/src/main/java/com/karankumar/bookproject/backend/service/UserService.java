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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.account.Role;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void register(@NonNull User user) throws UserAlreadyRegisteredException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        if (user.getUsername() != null && usernameIsInUse(user.getUsername())) {
            throw new UserAlreadyRegisteredException(
                    "The username " + user.getUsername() + " is already taken");
        }

        if (user.getEmail() != null && emailIsInUse(user.getEmail())) {
            throw new UserAlreadyRegisteredException(
                    "A user with the email address " + user.getUsername() + " already exists");
        }

        Role userRole = roleRepository.findByRole("USER")
                                      .orElseThrow(() -> new AuthenticationServiceException(
                                              "The default user role could not be found"));
        User userToRegister = User.builder()
                                  .username(user.getUsername())
                                  .email(user.getEmail())
                                  .password(passwordEncoder.encode(user.getPassword()))
                                  .active(true)
                                  .roles(Set.of(userRole))
                                  .build();

        userRepository.save(userToRegister);

        authenticateUser(user);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authResult =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (authResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
    }

    public boolean usernameIsInUse(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean usernameIsNotInUse(String username) {
        return !usernameIsInUse(username);
    }

    public boolean emailIsInUse(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean emailIsNotInUse(String email) {
        return !emailIsInUse(email);
    }
}

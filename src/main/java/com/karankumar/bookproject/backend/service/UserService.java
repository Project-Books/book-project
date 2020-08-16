package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void register(User user) throws UserAlreadyRegisteredException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (!constraintViolations.isEmpty()) {
            String errors = constraintViolations
                    .stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining());
            throw new BadCredentialsException(errors);
        }

        if (usernameIsInUse(user.getUsername())) {
            throw new UserAlreadyRegisteredException(
                    "The username " + user.getUsername() + " is already taken");
        }

        if (emailIsInUse(user.getEmail())) {
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

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authResult = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (authResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
    }

    public void delete(User user) {
        userRepository.delete(user);
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

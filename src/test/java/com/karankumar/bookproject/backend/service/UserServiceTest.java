package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final User testUser = User.builder()
                                      .username("testuser")
                                      .email("testmail")
                                      .password("aaaaAAAA1234@")
                                      .build();

    @Autowired
    UserServiceTest(UserService userService,
                    UserRepository userRepository,
                    RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void register_withBeanViolations_throwsException() {
        final User invalidUser = User.builder()
                                     .username("testuser")
                                     .email("testmail")
                                     .password("invalidpassword")
                                     .build();

        assertThrows(ConstraintViolationException.class, () -> userService.register(invalidUser));
    }

    @Test
    void register_withUsernameTaken_throwsException() {
        userRepository.save(testUser);
        testUser.setEmail("anotherEmail");

        assertThrows(UserAlreadyRegisteredException.class, () -> userService.register(testUser));
    }

    @Test
    void register_withEmailTaken_throwsException() {
        userRepository.save(testUser);
        testUser.setUsername("anotherUsername");

        assertThrows(UserAlreadyRegisteredException.class, () -> userService.register(testUser));
    }

    @Test
    void register_withoutUserRole_throwsError() {
        assertThrows(AuthenticationServiceException.class, () -> userService.register(testUser));
    }

    @Test
    void register_registersUser() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(testUser);

        assertThat(userRepository.findByUsername(testUser.getUsername()).isPresent(), equalTo(true));
    }

    @Test
    void register_logsUserIn() {
        roleRepository.save(Role.builder().role("USER").build());
        userService.register(testUser);

        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated(), equalTo(true));
    }

    @Test
    void usernameIsInUse_UsernameNotInUse_returnsFalse() {
        assertThat(userService.usernameIsInUse("notAtestuser"), equalTo(false));
    }

    @Test
    void usernameIsInUse_UsernameInUse_returnsTrue() {
        userRepository.save(testUser);

        assertThat(userService.usernameIsInUse("testuser"), equalTo(true));
    }

    @Test
    void usernameIsNotInUse_UsernameNotInUse_returnsTrue() {
        assertThat(userService.usernameIsNotInUse("testuser"), equalTo(true));
    }

    @Test
    @Transactional
    void usernameIsNotInUse_UsernameInUse_returnsFalse() {
        userRepository.save(testUser);

        assertThat(userService.usernameIsNotInUse("testuser"), equalTo(false));
    }

    @Test
    void emailIsInUse_EmailNotInUse_returnsFalse() {
        assertThat(userService.emailIsInUse("testmail"), equalTo(false));
    }

    @Test
    void emailIsInUse_EmailInUse_returnsTrue() {
        userRepository.save(testUser);

        assertThat(userService.emailIsInUse("testmail"), equalTo(true));
    }

    @Test
    void emailIsNotInUse_EmailNotInUse_returnsTrue() {
        assertThat(userService.emailIsNotInUse("testmail"), equalTo(true));
    }

    @Test
    void emailIsNotInUse_EmailInUse_returnsFalse() {
        userRepository.save(testUser);

        assertThat(userService.emailIsNotInUse("testmail"), equalTo(false));
    }
}

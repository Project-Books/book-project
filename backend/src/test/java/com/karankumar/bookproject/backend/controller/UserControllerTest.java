package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    /*
    1. valid email addr. form xxx@xxx.xxx - service layer
    2. does not match with the existing email addr.
     */
    @Test
    void changeEmailAddress_emailAddressChanged_ifUserExist() {
        // Given
        UserService mockedUserService = mock(UserService.class);
        User user = new User();
        String oldEmail = "user1@user.user";
        user.setEmail(oldEmail);
        Optional<User> optionalUser = Optional.of(user);
        when(mockedUserService.findByEmail(any(String.class))).thenReturn(optionalUser);
        UserController userController = new UserController(mockedUserService);

        // When
        String newEmail = "user2@user.user";
        User actualUser = userController.changeEmailAddress(oldEmail, newEmail);

        // Then
        assertThat(actualUser.getEmail()).isEqualTo(newEmail);
    }
}

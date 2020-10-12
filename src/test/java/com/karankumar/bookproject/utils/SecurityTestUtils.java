package com.karankumar.bookproject.utils;

import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.UserRepository;

public class SecurityTestUtils {
    private SecurityTestUtils() {}

    public static User getTestUser(UserRepository repository) {
        return repository.findByUsername("user").orElseThrow();
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

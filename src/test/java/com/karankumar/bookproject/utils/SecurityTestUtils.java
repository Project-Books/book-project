package com.karankumar.bookproject.utils;

import com.karankumar.bookproject.backend.entity.account.User;
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

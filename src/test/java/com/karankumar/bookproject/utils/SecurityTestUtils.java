package com.karankumar.bookproject.utils;

import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.UserRepository;

public class SecurityTestUtils {
    private SecurityTestUtils() {}

    public static User getTestUser(UserRepository repository) {
        return repository.findByUsername("user").orElseThrow();
    }
}

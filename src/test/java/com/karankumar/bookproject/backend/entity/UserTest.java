package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

@IntegrationTest
public class UserTest {
    private static UserRepository userRepository;

    @BeforeAll
    public static void setup(@Autowired UserRepository userRepository) {
        UserTest.userRepository = userRepository;
    }

    @Test
    void testValidUserSaved() {
        // given
        long numberOfUsers = userRepository.count();

        // when
        User user = userWithoutPassword().password("passwordP12&")
                                         .build();
        userRepository.save(user);

        // then
        Assertions.assertEquals(numberOfUsers + 1, userRepository.count());
    }

    private User.UserBuilder userWithoutPassword() {
        return User.builder()
                .username("username")
                .email("email");
    }

    @Test
    void testPasswordLessThan8CharactersIsInvalid() {
        // given
        long numberOfUsers = userRepository.count();

        // when
        User user = userWithoutPassword().password("pP12&")
                                         .build();

        try {
            userRepository.save(user);
        } catch (TransactionSystemException expected) {
        }
        // then
        Assertions.assertEquals(numberOfUsers, userRepository.count());
    }
}

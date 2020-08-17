package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

@IntegrationTest
public class UserTest {
    private static UserRepository userRepository;
    private long initialNumberOfUsers;

    @BeforeAll
    public static void setup(@Autowired UserRepository userRepository) {
        UserTest.userRepository = userRepository;
    }

    @BeforeEach
    void beforeEachSetup() {
        initialNumberOfUsers = UserTest.userRepository.count();
    }

    @Test
    void testValidUserSaved() {
        // given
        User user = userWithoutPassword().password("passwordP1&")
                                         .email("abc@def.com")
                                         .build();

        // when
        userRepository.save(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers + 1, userRepository.count());
    }

    private User.UserBuilder userWithoutPassword() {
        return User.builder()
                   .username("username")
                   .email("email");
    }

    @Test
    void testPasswordLessThan8CharactersIsInvalid() {
        // given
        User user = userWithoutPassword().password("pP1&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    void tryToSaveInvalidUser(User user) {
        try {
            // when
            userRepository.save(user);
        } catch (TransactionSystemException expected) {
        }
    }

    @Test
    void testPasswordWithNoDigitIsInvalid() {
        // given
        User user = userWithoutPassword().password("passwordP&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    @Test
    void testPasswordWithNoLowercaseCharacterIsInvalid() {
        // given
        User user = userWithoutPassword().password("PASSWORD1&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    @Test
    void testPasswordWithNoUppercaseCharacterIsInvalid() {
        // given
        User user = userWithoutPassword().password("password1&")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    @Test
    void testPasswordWithNoSpecialCharacterIsInvalid() {
        // given
        User user = userWithoutPassword().password("passwordP1")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }
    @Test
    void testUserWithMailWithoutDomain() {
        // given
        User user = userWithEmailWithoutDomain().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithoutDomain() {
        return userWithoutEmail()
                .email("email");
    }

    private User.UserBuilder userWithoutEmail() {
        return User.builder()
                   .username("username")
                   .password("passwordP1&");
    }

    @Test
    void testUserWithEmailWithoutAt() {
        // given
        User user = userWithEmailWithoutAt().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithoutAt() {
        return userWithoutEmail()
                .email("emailgoogle.com");
    }

    @Test
    void testUserWithEmailWithoutTopLevelDomain() {
        // given
        User user = userWithEmailWithoutTopLevelDomain().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithoutTopLevelDomain() {
        return userWithoutEmail()
                .email("email@def");
    }

    @Test
    void testUserWithEmailWithoutLocalPart() {
        // given
        User user = userWithEmailWithoutLocalPart().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithoutLocalPart() {
        return userWithoutEmail()
                .email("@def.com");
    }

    @Test
    void testUserWithEmailWithSpace() {
        // given
        User user = userWithEmailWithSpace().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithSpace() {
        return userWithoutEmail()
                .email(" @def.org");
    }

    @Test
    void testUserWithEmailWithQuotes() {
        // given
        User user = userWithEmailWithQuotes().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        Assertions.assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithQuotes() {
        return userWithoutEmail()
                .email("\"abc@def.org");
    }

}

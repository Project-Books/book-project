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

package com.karankumar.bookproject.backend.entity.account;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class UserTest {
    private final UserRepository userRepository;
    private long initialNumberOfUsers;

    UserTest(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        initialNumberOfUsers = userRepository.count();
    }

    @Test
    void testValidUserSaved() {
        // given
        User user = userWithoutPassword().password("passwordP1&132")
                                         .email("abc@def.com")
                                         .build();

        // when
        userRepository.save(user);

        // then
        assertEquals(initialNumberOfUsers + 1, userRepository.count());
    }

    private User.UserBuilder userWithoutPassword() {
        return User.builder()
                   .username("username")
                   .email("email");
    }

    @Test
    void testWeakPasswordIsInvalid() {
        // given
        User user = userWithoutPassword().password("123456789")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        assertEquals(initialNumberOfUsers, userRepository.count());
    }
    
    @Test
    void testFairPasswordIsInvalid() {
        // given
        User user = userWithoutPassword().password("aPassWorD")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        assertEquals(initialNumberOfUsers, userRepository.count());
    }
    
    @Test
    void testGoodPasswordIsInvalid() {
        // given
        User user = userWithoutPassword().password("testPa$$123")
                                         .build();

        // when
        tryToSaveInvalidUser(user);

        // then
        assertEquals(initialNumberOfUsers, userRepository.count());
    }
    
    void tryToSaveInvalidUser(User user) {
        try {
            userRepository.save(user);
        } catch (TransactionSystemException expected) { }
    }

    @Test
    void testUserWithMailWithoutDomain() {
        // given
        User user = userWithEmailWithoutDomain().build();

        // when
        tryToSaveInvalidUser(user);

        // then
        assertEquals(initialNumberOfUsers, userRepository.count());
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
        assertEquals(initialNumberOfUsers, userRepository.count());
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
        assertEquals(initialNumberOfUsers, userRepository.count());
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
        assertEquals(initialNumberOfUsers, userRepository.count());
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
        assertEquals(initialNumberOfUsers, userRepository.count());
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
        assertEquals(initialNumberOfUsers, userRepository.count());
    }

    private User.UserBuilder userWithEmailWithQuotes() {
        return userWithoutEmail().email("\"abc@def.org");
    }
}

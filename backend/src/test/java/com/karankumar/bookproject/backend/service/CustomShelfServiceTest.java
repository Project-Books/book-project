/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.model.CustomShelf;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.CustomShelfRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import com.karankumar.bookproject.util.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.karankumar.bookproject.util.SecurityTestUtils.TEST_USER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@Transactional
@DisplayName("CustomShelfService should")
class CustomShelfServiceTest {
    private final CustomShelfService customShelfService;
    private final CustomShelfRepository customShelfRepository;
    private final User user;

    private static final List<String> SHELF_NAMES =
            List.of("CustomShelf1", "CustomShelf2", "CustomShelf3");

    @Autowired
    CustomShelfServiceTest(CustomShelfService customShelfService,
                           CustomShelfRepository customShelfRepository,
                           UserRepository userRepository) {
        this.customShelfService = customShelfService;
        this.customShelfRepository = customShelfRepository;
        this.user = SecurityTestUtils.getTestUser(userRepository);
    }

    @BeforeEach
    void setUp() {
        customShelfRepository.deleteAll();
        customShelfRepository.saveAll(
                SHELF_NAMES.stream()
                        .map(customShelfService::createCustomShelf)
                        .collect(Collectors.toList())
        );
    }

    @Test
    @DisplayName("successfully find an existing custom shelf by ID")
    void findExistingCustomShelfById() {
        // given
        CustomShelf newShelf = new CustomShelf("Test shelf", user);
        customShelfService.save(newShelf);

        // when
        Optional<CustomShelf> shelfFound = customShelfService.findById(newShelf.getId());

        // then
        assertThat(shelfFound).isPresent();
    }

    @Test
    void findExistingCustomShelfByName() {
        // given
        CustomShelf customShelf = new CustomShelf("test", user);
        customShelfService.save(customShelf);

        // when
        List<CustomShelf> customShelves = customShelfService.findAll(customShelf.getShelfName());

        // then
        assertThat(customShelves).containsExactly(customShelf);
    }

    @Test
    @DisplayName("find all custom shelves belonging to the logged in user")
    void findAllCustomShelvesForLoggedInUser() {
        // when
        List<CustomShelf> shelves = customShelfService.findAllForLoggedInUser();

        assertSoftly(softly -> {
            softly.assertThat(shelves).isNotNull().hasSameSizeAs(SHELF_NAMES);
            softly.assertThat(shelves)
                    .extracting(CustomShelf::getShelfName)
                    .containsExactlyInAnyOrderElementsOf(SHELF_NAMES);
            softly.assertThat(shelves)
                    .extracting(CustomShelf::getUser)
                    .extracting(User::getEmail)
                    .allMatch(email -> email.equals(TEST_USER_EMAIL));
        });
    }

    @Test
    void findShelfByName() {
        // given
        CustomShelf expected = new CustomShelf("test", user);
        customShelfService.save(expected);

        // when
        Shelf actual = customShelfService.getCustomShelfByName(expected.getShelfName()).get();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("return all custom shelves when the shelf to search by is null")
    void returnAllShelvesWhenNullParameter() {
        List<CustomShelf> actual = customShelfService.findAll(null);
        assertThat(actual.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("be able to delete a custom shelf")
    void deleteCustomShelf() {
        // given
        CustomShelf customShelf = new CustomShelf("test", user);
        customShelfService.save(customShelf);
        assumeThat(customShelfService.findAll()).contains(customShelf);
        Long id = customShelf.getId();

        // when
        customShelfService.delete(customShelf);

        // then
        assertThat(customShelfService.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("throw an error on an attempt to save a null custom shelf")
    void throwErrorWhenSavingANullCustomShelf() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.save(null));
    }

    @Test
    @DisplayName("throw an error on an attempt to delete a null custom shelf")
    void throwErrorWhenDeletingANullCustomShelf() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.delete(null));
    }
}

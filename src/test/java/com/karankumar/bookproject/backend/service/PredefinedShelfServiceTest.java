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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;

import static com.karankumar.bookproject.util.SecurityTestUtils.TEST_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.karankumar.bookproject.backend.repository.UserRepository;
import com.karankumar.bookproject.util.SecurityTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IntegrationTest
@DisplayName("PredefinedShelfService should")
@Transactional
class PredefinedShelfServiceTest {
    private final PredefinedShelfService predefinedShelfService;
    private final UserRepository userRepository;

    @Autowired
    PredefinedShelfServiceTest(PredefinedShelfService predefinedShelfService,
                               UserRepository userRepository) {
        this.predefinedShelfService = predefinedShelfService;
        this.userRepository = userRepository;
    }

    @Test
    @DisplayName("throw an exception on an attempt to save a null predefined shelf")
    void notSaveNullPredefinedShelf() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> predefinedShelfService.save(null));
    }

    @Test
    void findAllPredefinedShelvesForLoggedInUser() {
        List<PredefinedShelf> shelves = predefinedShelfService.findAllForLoggedInUser();
        assertThat(shelves).isNotNull().hasSameSizeAs(PredefinedShelf.ShelfName.values());

        assertSoftly(softly ->
                softly.assertThat(shelves).allSatisfy(shelf ->
                        assertThat(shelf.getUser().getUsername()).isEqualTo(TEST_USER_NAME)
                )
        );
    }

    @Test
    void findAllPredefinedShelvesForPredefinedShelfNameAndLoggedInUser() {
        PredefinedShelf shelf = predefinedShelfService
                .findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName.TO_READ);
        assertThat(shelf).isNotNull();

        assertSoftly(softly -> {
            softly.assertThat(shelf.getPredefinedShelfName())
                  .isEqualTo(PredefinedShelf.ShelfName.TO_READ);
            softly.assertThat(shelf.getUser().getUsername()).isEqualTo(TEST_USER_NAME);
        });
    }

    @Test
    void saveValidPredefinedShelf() {
        // given
        long initialCount = predefinedShelfService.count();
        PredefinedShelf existingToReadShelf = predefinedShelfService.findToReadShelf();
        PredefinedShelf testShelf = new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ,
                SecurityTestUtils.getTestUser(userRepository));

        // when
        predefinedShelfService.save(testShelf);

        // then
        List<PredefinedShelf> expected = Arrays.asList(existingToReadShelf, testShelf);

        assertSoftly(softly -> {
            softly.assertThat(predefinedShelfService.count()).isEqualTo(initialCount + 1);
            softly.assertThat(predefinedShelfService.findAllForLoggedInUser()).containsAll(expected);
        });
    }

    @Test
    void getPredefinedShelfNamesCorrectlyAsStrings() {
        List<String> actualShelfNames = predefinedShelfService.getPredefinedShelfNamesAsStrings();
        List<String> expectedShelfNames =
                Stream.of(PredefinedShelf.ShelfName.values()).map(Enum::toString).collect(
                        Collectors.toList());

        assertSoftly(softly -> {
            softly.assertThat(actualShelfNames).hasSize(expectedShelfNames.size());
            softly.assertThat(expectedShelfNames).containsAll(actualShelfNames);
        });

    }
}

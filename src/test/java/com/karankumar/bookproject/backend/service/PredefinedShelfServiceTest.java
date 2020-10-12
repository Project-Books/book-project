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

import static com.karankumar.bookproject.utils.SecurityTestUtils.TEST_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
@DisplayName("PredefinedShelfService should")
class PredefinedShelfServiceTest {
    @Autowired private PredefinedShelfService predefinedShelfService;

    @Test
    void notSaveNullPredefinedShelf() {
        // given
        long initialCount = predefinedShelfService.count();

        // when
        predefinedShelfService.save(null);

        // then
        assertThat(predefinedShelfService.count()).isEqualTo(initialCount);
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
            softly.assertThat(shelf.getPredefinedShelfName()).isEqualTo(PredefinedShelf.ShelfName.TO_READ);
            softly.assertThat(shelf.getUser().getUsername()).isEqualTo(TEST_USER_NAME);
        });
    }
}

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

package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
class PredefinedShelfTest {
    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;

    @Autowired
    PredefinedShelfTest(BookService bookService, PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @Test
    void testPredefinedShelvesWithoutBooksShouldStillExist() {
        // given
        resetBookService();

        // when
        List<PredefinedShelf> shelves = predefinedShelfService.findAllForLoggedInUser();

        // then
        assertSoftly(
                softly -> {
                    softly.assertThat(shelves).hasSize(4);
                    softly.assertThat(shelves.stream().map(PredefinedShelf::getPredefinedShelfName))
                          .contains(
                                  ShelfName.TO_READ,
                                  ShelfName.READING,
                                  ShelfName.READ,
                                  ShelfName.DID_NOT_FINISH
                          );
                }
        );

    }

    private void resetBookService() {
        bookService.deleteAll();
    }
}

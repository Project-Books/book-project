/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.dto.GoodreadsBookImport;
import com.karankumar.bookproject.backend.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("ImportService should")
class ImportServiceTest {
    private final ImportService importService;
    private final BookService bookService;

    @Autowired
    ImportServiceTest(ImportService importService,
                      BookService bookService) {
        this.importService = importService;
        this.bookService = bookService;
    }

    @BeforeEach
    public void setUp() {
        resetServices();
    }

    private void resetServices() {
        bookService.deleteAll();
    }

    @Test
    void goodreadsBookImportIsSavedWhenAuthorAndPredefinedShelfIsNotEmpty() {
        // given
        GoodreadsBookImport goodreadsBookImport = new GoodreadsBookImport();
        goodreadsBookImport.setTitle("Blink: The Power of Thinking Without Thinking");
        goodreadsBookImport.setAuthor("Malcolm Gladwell");
        goodreadsBookImport.setBookshelves("currently-reading");

        // when
        List<Book> savedBooks =
                importService.importGoodreadsBooks(Collections.singletonList(goodreadsBookImport));

        // then
        int expected = 1;
        assertThat(savedBooks.size()).isEqualTo(expected);
    }
}

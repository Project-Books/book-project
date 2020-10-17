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
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@IntegrationTest
@Transactional
class BookTest {
    private final BookService bookService;
    private final TagService tagService;
    private final PredefinedShelfService predefinedShelfService;

    private Book testBook;
    private Tag testTag;

    @Autowired
    BookTest(BookService bookService, TagService tagService, PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.tagService = tagService;
        this.predefinedShelfService = predefinedShelfService;
    }

    private Book createBook(PredefinedShelf shelf) {
        Author author = new Author("Firstname", "Lastname");
        Book book = new Book("Test Title", author, shelf);

        book.setTags(Collections.singleton(testTag));

        return book;
    }

    @BeforeEach
    void setUp() {
        testTag = new Tag("Test Tag");
        tagService.deleteAll();
        tagService.save(testTag);

        testBook = createBook(predefinedShelfService.findToReadShelf());
        bookService.deleteAll();
        bookService.save(testBook);
    }

    @Test
    void testOrphanedTagsNotRemoved() {
        // given
        assumeThat(tagService.findAll().size()).isOne();

        // when
        bookService.delete(testBook);

        // then
        assertThat(tagService.findAll().size()).isOne();
    }
}

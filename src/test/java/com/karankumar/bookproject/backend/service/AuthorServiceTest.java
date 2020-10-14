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
import com.karankumar.bookproject.backend.entity.Author;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log
@IntegrationTest
class AuthorServiceTest {
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    AuthorServiceTest(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @BeforeEach
    void reset() {
        bookService.deleteAll();
        authorService.deleteAll();
    }

    @Test
    void testSaveAndConfirmDuplicateNameWithDifferentId() {
        // given
        Author author = new Author("Nyor", "Ja");
        authorService.save(author);

        Author authorCopy = author;
        authorService.save(authorCopy);

        // when
        List<Author> savedAuthors = authorService.findAll();

        // then
        assertEquals(2, savedAuthors.size());
    }

    @Transactional
    @Test
    void testSaveIntegrity() {
        // given
        Author author = new Author("daks", "oten");
        authorService.save(author);

        // when
        Author existingAuthor = authorService.findById(author.getId());
        authorService.save(existingAuthor);

        // then
        assertEquals(2, authorService.count());
    }

    @Test
    void savedAuthorCanBeFound() {
        // given
        Author author = new Author("First", "Last");

        // when
        authorService.save(author);

        // then
        assertNotNull(authorService.findById(author.getId()));
    }
}

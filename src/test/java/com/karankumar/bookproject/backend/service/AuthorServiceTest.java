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
import com.karankumar.bookproject.backend.entity.Author;

import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assumptions.assumeThat;

@Log
@IntegrationTest
@DisplayName("AuthorService should")
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
    void saveAndConfirmDuplicateNameWithDifferentId() {
        // given
        Author author = new Author("Nyor", "Ja");
        authorService.save(author);

        Author authorCopy = new Author(author.getFirstName(), author.getLastName());
        authorService.save(authorCopy);

        // when
        List<Author> savedAuthors = authorService.findAll();

        // then
        int expected = 2;
        assertThat(savedAuthors.size()).isEqualTo(expected);
    }

    @Transactional
    @Test
    void saveCorrectly() {
        // given
        Author author = new Author("daks", "oten");
        authorService.save(author);

        // when
        Author existingAuthor = authorService.findById(author.getId());
        authorService.save(existingAuthor);

        // then
        assertThat(authorService.count()).isOne();
    }

    @Test
    void savedAuthorCanBeFound() {
        // given
        Author author = new Author("First", "Last");
        authorService.save(author);

        // when
        Author actual = authorService.findById(author.getId());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("be able to delete an author without any books")
    void deleteAuthorWithoutBooks() {
        assumeThat(authorService.count()).isZero();

        // given
        Author authorWithoutBooks = new Author("First", "Last");
        authorService.save(authorWithoutBooks);

        // when
        authorService.delete(authorWithoutBooks);

        // then
        assertThat(authorService.count()).isZero();
    }

    @Test
    @DisplayName("throw error on attempt to save a null author")
    void throwErrorOnSavingNullAuthor() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> authorService.save(null));
    }

    @Test
    @DisplayName("throw error on attempt to delete a null author")
    void throwErrorOnDeletingNullAuthor() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> authorService.delete(null));
    }
}

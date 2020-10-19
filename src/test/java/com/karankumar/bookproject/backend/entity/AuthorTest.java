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
import com.karankumar.bookproject.backend.service.AuthorService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class AuthorTest {
    private final BookService bookService;
    private final AuthorService authorService;
    private final PredefinedShelfService predefinedShelfService;

    private Book testBook1;
    private Book testBook2;
    private PredefinedShelf toRead;

    @Autowired
    AuthorTest(BookService bookService, AuthorService authorService, PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void setUp() {
        toRead = predefinedShelfService.findToReadShelf();
        testBook1 = createBook("How the mind works", toRead);
        testBook2 = createBook("The better angels of our nature", toRead);
        resetBookService();
        saveBooks();
    }

    private void saveBooks() {
        bookService.save(testBook1);
        bookService.save(testBook2);
    }

    private void resetBookService() {
        bookService.deleteAll();
    }

    private static Book createBook(String title, PredefinedShelf shelf) {
        Author author = new Author("Steven", "Pinker");
        return new Book(title, author, shelf);
    }

    /**
     * Updating the author of a book should only affect that book, not any other book that
     * originally had the same author name
     */
    @Test
    @Disabled
    // TODO: fix failing test
    void updateAuthorAffectsOneRow() {
        Author newAuthor = new Author("Matthew", "Walker");
        testBook1.setAuthor(newAuthor);
        bookService.save(testBook1);

        assertThat(testBook1.getAuthor()).isNotEqualTo(testBook2.getAuthor());
    }

    @Test
    @Disabled
    // TODO: fix failing test
    void orphanAuthorsRemoved() {
        Author orphan = new Author("Jostein", "Gardner");
        Book book = new Book("Sophie's World", orphan, toRead);
        bookService.delete(book);

        assertSoftly(
                softly -> {
                    softly.assertThatThrownBy(() -> authorService.findById(orphan.getId()))
                          .isInstanceOf(RuntimeException.class);
                    softly.assertThat(authorService.findAll()).isEmpty();
                }
        );
    }
}

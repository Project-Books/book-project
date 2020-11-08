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

package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.util.SecurityTestUtils.getTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaIntegrationTest
@DisplayName("BookRepository should")
class BookRepositoryTest {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final PredefinedShelfRepository predefinedShelfRepository;

    private PredefinedShelf readShelf;
    private Author author;
    private Book book;

    @Autowired
    BookRepositoryTest(BookRepository bookRepository, AuthorRepository authorRepository,
                       UserRepository userRepository,
                       PredefinedShelfRepository predefinedShelfRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.predefinedShelfRepository = predefinedShelfRepository;
    }

    @BeforeEach
    void setUp() {
        User user = getTestUser(userRepository);
        author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
        readShelf = predefinedShelfRepository.save(new PredefinedShelf(READ, user));
        book = bookRepository.save(new Book("someTitle", author, readShelf));
    }

    @Test
    void successfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        Book book2 = bookRepository.save(new Book("someOtherTitle", author, readShelf));

        // when
        bookRepository.delete(book);

        // then
        assertSoftly(softly -> {
            softly.assertThat(bookRepository.findAll().size()).isOne();
            softly.assertThat(bookRepository.findAll().get(0))
                  .isEqualToComparingFieldByField(book2);
        });
    }

    @Test
    @DisplayName("should successfully delete a book when the author has no other books")
    void successfullyDeleteSingleAuthoredBook() {
        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("should successfully find list of books by their title")
    void findBookByTitle() {
        // when
        List<Book> actual1 = bookRepository.findByTitleContainingIgnoreCase("someTitle");
        List<Book> actual2 = bookRepository.findByTitleContainingIgnoreCase("some");
        List<Book> actual3 = bookRepository.findByTitleContainingIgnoreCase("title");

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual1.size()).isOne();
            softly.assertThat(actual2.size()).isOne();
            softly.assertThat(actual3.size()).isOne();
        });
    }
}

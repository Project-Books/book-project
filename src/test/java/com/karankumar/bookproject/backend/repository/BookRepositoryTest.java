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
    private User user;

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
    void setup() {
        bookRepository.deleteAll();
        user = getTestUser(userRepository);
        author = authorRepository.save(new Author("firstName", "lastName"));
        Author author2 = authorRepository.saveAndFlush(new Author("author", "test"));
        readShelf = predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ, user));
        book = bookRepository.save(new Book("someTitle", author, readShelf));
        PredefinedShelf toRead = predefinedShelfRepository.saveAndFlush(new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, user));
        bookRepository.save(new Book("title", author, readShelf));
        bookRepository.saveAndFlush(new Book("test", author2, toRead));
    }

    @Test
    void successfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        Book book2 = bookRepository.save(new Book("someOtherTitle", author, readShelf));

        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("should successfully delete a book when the author has no other books")
    void successfullyDeleteSingleAuthoredBook() {
        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findAll().size()).isEqualTo(2);
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
            softly.assertThat(actual3.size()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("should successfully find list of books by predefined shelf, title or author")
    void findBookByPredefinedShelfAndTitleOrAuthor() {
        PredefinedShelf readShelf = predefinedShelfRepository.findByPredefinedShelfNameAndUser(PredefinedShelf.ShelfName.READ, user);
        PredefinedShelf toReadShelf = predefinedShelfRepository.findByPredefinedShelfNameAndUser(PredefinedShelf.ShelfName.TO_READ, user);

        assertSoftly(softly -> {
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf,"%", "%").size()).isEqualTo(2);
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(toReadShelf, "%", "%").size()).isOne();
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf, "some", "%").size()).isOne();
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf, "title", "%").size()).isEqualTo(2);
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(toReadShelf, "title", "%").size()).isZero();
        });
    }

    @Test
    @DisplayName("should successfully find list of books by title or author")
    void findBookByTitleOrAuthor() {

        assertSoftly(softly -> {
            softly.assertThat(bookRepository.findByTitleOrAuthor("%", "%").size()).isEqualTo(3);
            softly.assertThat(bookRepository.findByTitleOrAuthor("title", "%").size()).isEqualTo(2);
            softly.assertThat(bookRepository.findByTitleOrAuthor("%", "firstName").size()).isEqualTo(2);
            softly.assertThat(bookRepository.findByTitleOrAuthor("%", "lastName").size()).isEqualTo(2);
            softly.assertThat(bookRepository.findByTitleOrAuthor("%", "author").size()).isOne();
            softly.assertThat(bookRepository.findByTitleOrAuthor("title", "firstName").size()).isEqualTo(2);
            softly.assertThat(bookRepository.findByTitleOrAuthor("te", "au").size()).isOne();
        });
    }

}


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
    private Author author;
    private PredefinedShelf read;
    private final String WILDCARD = "%";

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
    void init() {
        bookRepository.deleteAll();
        user = getTestUser(userRepository);
        author = authorRepository.save(new Author("firstName", "lastName"));
        read = predefinedShelfRepository.save(
                new PredefinedShelf(PredefinedShelf.ShelfName.READ, user)
        );
        bookRepository.save(new Book("title", author, read));
    }

    @Test
    @DisplayName("should successfully delete a book when the author has more than one book")
    void successfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        bookRepository.saveAndFlush(new Book("Book2", author, read));
        Book book = bookRepository.findByTitleOrAuthor("title", WILDCARD).get(0);

        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findByTitleOrAuthor(WILDCARD, "firstName").size())
                .isOne();
    }

    @Test
    @DisplayName("should successfully delete a book when the author has no other books")
    void successfullyDeleteSingleAuthoredBook() {
        //given
        Book book = bookRepository.findByTitleContainingIgnoreCase("title").get(0);

        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findByTitleOrAuthor(WILDCARD, "firstName").size())
                .isZero();
    }

    @Test
    @DisplayName("should successfully find list of books by their title")
    void findBookByTitle() {
        //given
        bookRepository.saveAndFlush(new Book("someTitle", author, read));

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
    @DisplayName("should successfully find list of books for any shelf no other filter")
    void findBookByShelf_withoutParameters() {
        int allBooks = bookRepository.findAll().size();
        int radBooks = bookRepository.findByShelfAndTitleOrAuthor(read, WILDCARD, WILDCARD).size();

        assertThat(allBooks).isEqualTo(radBooks);
    }

    @Test
    @DisplayName("should successfully find list of books for any shelf and title")
    void findBookByShelf_onlyTitle() {
        String title = "anotherBook";
        PredefinedShelf toRead = predefinedShelfRepository.saveAndFlush(
                new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, user)
        );

        bookRepository.saveAndFlush(new Book(title, author, toRead));

        assertThat(bookRepository.findByShelfAndTitleOrAuthor(toRead, title, WILDCARD).size())
                .isOne();

    }

    @Test
    @DisplayName("should successfully find books for any shelf and author")
    void findBookByShelf_onyAuthor() {
        String firstName = "firstName";
        String lastName = "lastName";

        assertSoftly(softly -> {
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(read, WILDCARD, firstName)
                                            .size()).isOne();
            softly.assertThat(bookRepository.findByShelfAndTitleOrAuthor(read, WILDCARD, lastName)
                                            .size()).isOne();
        });
    }

    @Test
    @DisplayName("should successfully find all books by not passing any filter")
    void successfullyFindAllBooksWithoutFilter() {
        // given
        int allBooks = bookRepository.findAll().size();

        // when
        int actual = bookRepository.findByTitleOrAuthor(WILDCARD, WILDCARD)
                                 .size();

        // then
        assertThat(actual).isEqualTo(allBooks);
    }

    @Test
    @DisplayName("should successfully find book by title without author name")
    void successfullyFindBookWithOnlyTitle() {
        // given
        String title = "title";

        // when
        bookRepository.saveAndFlush(new Book("anotherBook", author, read));

        // then
        assertThat(bookRepository.findByTitleOrAuthor(title, WILDCARD).size()).isOne();
    }

    @Test
    @DisplayName("should successfully find book by author without title")
    void successfullyFindBookWithOnlyAuthor() {
        String firstName = "firstName";
        String lastName = "lastName";

        assertSoftly(softly -> {
            softly.assertThat(bookRepository.findByTitleOrAuthor(WILDCARD, firstName).size())
                  .isOne();
            softly.assertThat(bookRepository.findByTitleOrAuthor(WILDCARD, lastName).size()).isOne();
        });
    }
}

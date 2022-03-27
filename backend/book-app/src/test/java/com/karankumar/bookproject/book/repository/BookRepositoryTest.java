/*
* The book project lets a user keep track of different books they would like to read, are currently
* reading, have read or did not finish.
* Copyright (C) 2021  Karan Kumar

* This program is free software: you can redistribute it and/or modify it under the terms of the
* GNU General Public License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE.  See the GNU General Public License for more details.

* You should have received a copy of the GNU General Public License along with this program.
* If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.book.repository;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.repository.UserRepository;
import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.account.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
  private Author author;
  private PredefinedShelf read;

  @Autowired
  BookRepositoryTest(
      BookRepository bookRepository,
      AuthorRepository authorRepository,
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
    User user = getTestUser(userRepository);
    author = authorRepository.save(new Author("firstName lastName"));
    read =
        predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ, user));
    bookRepository.save(new Book("title", author, read));
  }

  @Test
  void successfullyDeleteABook_whenAuthorHasOtherBooks() {
    // given
    Book book = new Book("Book2", author, read);
    bookRepository.saveAndFlush(book);
    Long id = book.getId();

    // when
    bookRepository.delete(book);

    // then
    assertThat(bookRepository.findBookById(id)).isEmpty();
  }

  @Test
  void successfullyDeleteSingleAuthoredBook() {
    // given
    Book book = bookRepository.findByTitleContainingIgnoreCase("title").get(0);

    // when
    bookRepository.delete(book);

    // then
    assertThat(bookRepository.findByTitleOrAuthor("firstName").size()).isZero();
  }

  @Test
  void canFindBookByTitle() {
    // given
    bookRepository.saveAndFlush(new Book("someTitle", author, read));

    // when
    List<Book> actual1 = bookRepository.findByTitleContainingIgnoreCase("someTitle");
    List<Book> actual2 = bookRepository.findByTitleContainingIgnoreCase("some");
    List<Book> actual3 = bookRepository.findByTitleContainingIgnoreCase("title");

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(actual1.size()).isOne();
          softly.assertThat(actual2.size()).isOne();
          softly.assertThat(actual3.size()).isEqualTo(2);
        });
  }

  @Test
  @Disabled // TODO: re-enable. This is disabled until implemented
  void allBooksFoundWhenNoFilterPassed() {
    // given
    int allBooks = bookRepository.findAll().size();

    // when
    String WILDCARD = "%";
    int actual = bookRepository.findByTitleOrAuthor(WILDCARD).size();

    // then
    assertThat(actual).isEqualTo(allBooks);
  }

  @Test
  @Disabled // TODO: re-enable. This is disabled until implemented
  void canFindBookByTitleOrAuthor() {
    // given
    String title = "title";

    // when
    bookRepository.saveAndFlush(new Book("anotherBook", author, read));

    // then
    assertThat(bookRepository.findByTitleOrAuthor(title).size()).isOne();
  }

  @Test
  @Disabled // TODO: re-enable. This is disabled until implemented
  void canFindBookByAuthor() {
    String firstName = "firstName";
    String lastName = "lastName";

    assertSoftly(
        softly -> {
          softly.assertThat(bookRepository.findByTitleOrAuthor(firstName).size()).isOne();
          softly.assertThat(bookRepository.findByTitleOrAuthor(lastName).size()).isOne();
        });
  }

  @Test
  void canGetAllBooksForUser() {
    // given
    User user = getTestUser(userRepository);

    // when
    List<Book> books = bookRepository.findAllBooksForUser(user);

    // then
    assertSoftly(
        softly -> {
          assertThat(books).isNotEmpty();
          assertThat(books.size()).isOne();
        });
  }
}

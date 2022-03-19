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

package com.karankumar.bookproject.util;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.shelf.service.UserCreatedShelfService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("CustomShelfUtils should")
class UserCreatedShelfUtilsTest {
  private final BookService bookService;
  private final UserCreatedShelfService userCreatedShelfService;
  private final PredefinedShelfService predefinedShelfService;

  private UserCreatedShelf userCreatedShelf1;
  private UserCreatedShelf userCreatedShelf2;
  private UserCreatedShelf userCreatedShelfWithNoBooks;

  private Set<Book> booksInCustomShelf1;

    @BeforeAll
    static void dbSetup() {
        BookPostgreSQLContainer.getInstance().start();
    }

    @Autowired
    UserCreatedShelfUtilsTest(BookService bookService, UserCreatedShelfService userCreatedShelfService,
                              PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.userCreatedShelfService = userCreatedShelfService;
        this.predefinedShelfService = predefinedShelfService;
    }
  }

  @Test
  void returnBooksSuccessfully() {
    Set<Book> actual =
        userCreatedShelfService.getBooksInCustomShelf(userCreatedShelf1.getShelfName());
    booksInCustomShelf1.forEach(book -> assertThat(actual.toString()).contains(book.toString()));
  }

  @Test
  void returnNoBooksIfNoBooksInCustomShelf() {
    // given
    String customShelfWithoutBooks = userCreatedShelfWithNoBooks.getShelfName();

    // when
    Set<Book> actual = userCreatedShelfService.getBooksInCustomShelf(customShelfWithoutBooks);

    // then
    assertThat(actual).isEmpty();
  }

  @Test
  void returnNoBooksForNonExistentCustomShelf() {
    // given
    String nonExistentShelf = "InvalidShelf";

    // when
    Set<Book> actual = userCreatedShelfService.getBooksInCustomShelf(nonExistentShelf);

    // then
    assertThat(actual).isEmpty();
  }
}

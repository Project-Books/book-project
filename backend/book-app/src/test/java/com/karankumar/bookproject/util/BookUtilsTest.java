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
import com.karankumar.bookproject.book.BookUtils;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookUtils should")
@IntegrationTest
class BookUtilsTest {
  private final String bookTitle = "Title";
  private final Book book; // = new Book(bookTitle, null, null);

  @Autowired
  BookUtilsTest(PredefinedShelfService predefinedShelfService) {
    // TODO: mock predefinedShelfService
    book = new Book(bookTitle, null, predefinedShelfService.findById(1L).get());
  }

  @Test
  void notShowSeriesPositionIfBookIsNotInSeries() {
    // given
    book.setSeriesPosition(null);

    // when
    String actual = BookUtils.combineTitleAndSeries(book);

    // then
    assertThat(actual).isEqualTo(bookTitle);
  }

  @Test
  void returnTitleWhenSeriesPositionIsZero() {
    // given
    book.setSeriesPosition(0);

    // when
    String actual = BookUtils.combineTitleAndSeries(book);

    // then
    assertThat(actual).isEqualTo(bookTitle);
  }

  @Test
  void showSeriesPositionCorrectly() {
    // given
    book.setSeriesPosition(1);

    // when
    String actual = BookUtils.combineTitleAndSeries(book);

    // then
    String expected = bookTitle + " (#1)";
    assertThat(actual).isEqualTo(expected);
  }
}

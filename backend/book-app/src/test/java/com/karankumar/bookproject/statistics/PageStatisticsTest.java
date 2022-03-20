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

package com.karankumar.bookproject.statistics;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.statistics.util.StatisticTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayName("PageStatistics should")
class PageStatisticsTest {
  private final BookService bookService;
  private final PredefinedShelfService predefinedShelfService;

  private static PageStatistics pageStatistics;

  @Autowired
  PageStatisticsTest(BookService bookService, PredefinedShelfService predefinedShelfService) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
  }

  @BeforeEach
  public void setUp() {
    bookService.deleteAll();
    StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
    PageStatisticsTest.pageStatistics = new PageStatistics(predefinedShelfService);
  }

  @Test
  void findBookWithMostPages() {
    // when
    String actual = pageStatistics.findBookWithMostPages().get().getTitle();

    // then
    String expected = StatisticTestUtils.getBookWithMostPages().getTitle();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void countOnlyReadBooksTowardsMostPagesStatistics() {
    // given
    PredefinedShelf readingShelf = predefinedShelfService.findReadingShelf();

    Book readingBook =
        new Book("More pages than any read book", new Author("Joe Bloggs"), readingShelf);
    readingBook.setNumberOfPages(StatisticTestUtils.getBookWithMostPages().getNumberOfPages() + 50);
    bookService.save(readingBook);

    // when
    String actual = pageStatistics.findBookWithMostPages().get().getTitle();

    // then
    assertThat(actual).isEqualTo(StatisticTestUtils.getBookWithMostPages().getTitle());
  }

  @Test
  void notDivideAveragePageLengthByZero() {
    resetPageStatistics();
    assertThat(pageStatistics.calculateAveragePageLength()).isEmpty();
  }

  @Test
  void calculateAveragePageLengthCorrectly() {
    int averagePageLength =
        StatisticTestUtils.getTotalNumberOfPages() / StatisticTestUtils.getNumberOfBooks();
    assertThat(pageStatistics.calculateAveragePageLength())
        .contains(Double.valueOf(averagePageLength));
  }

  @Test
  void calculateAveragePageLengthWithFloatPointCalculationCorrectly() {
    // given
    StatisticTestUtils.deleteBook(StatisticTestUtils.getBookWithHighestRating());
    pageStatistics = new PageStatistics(predefinedShelfService);

    // when
    Double actual = pageStatistics.calculateAveragePageLength().get();

    // then
    Double averagePageLength = 274.0;
    assertThat(actual).isEqualTo(averagePageLength);
  }

  private void resetPageStatistics() {
    bookService.deleteAll();
    pageStatistics = new PageStatistics(predefinedShelfService);
  }
}

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

package com.karankumar.bookproject.statistics;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.statistics.util.StatisticTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("YearStatistics should")
class YearStatisticsTest {
  private final BookService bookService;
  private final PredefinedShelfService predefinedShelfService;

  private YearStatistics yearStatistic;
  private Book bookWithLowestRatingThisYear;
  private Book bookWithHighestRatingThisYear;

  @Autowired
  YearStatisticsTest(BookService bookService, PredefinedShelfService predefinedShelfService) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
  }

  @BeforeEach
  public void setUp() {
    bookService.deleteAll(); // reset
    StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
    bookWithLowestRatingThisYear = StatisticTestUtils.getBookWithLowestRatingThisYear();
    bookWithHighestRatingThisYear = StatisticTestUtils.getBookWithHighestRatingThisYear();

    yearStatistic = new YearStatistics(predefinedShelfService);
  }

  @Test
  void findLowestRatedBookThisYear() {
    String actualTitle = yearStatistic.findLeastLikedBookThisYear().get().getTitle();
    String expectedTitle = bookWithLowestRatingThisYear.getTitle();
    assertThat(actualTitle).isEqualTo(expectedTitle);
  }

  @Test
  void notFindNonExistentLowestRatedBook() {
    resetRatingStatistics();
    assertThat(yearStatistic.findLeastLikedBookThisYear()).isNotPresent();
  }

  @Test
  void highestRatedBookExistsAndIsFound() {
    String actualTitle = yearStatistic.findMostLikedBookThisYear().get().getTitle();
    String expectedTitle = bookWithHighestRatingThisYear.getTitle();
    assertThat(actualTitle).isEqualTo(expectedTitle);
  }

  @Test
  void testNonExistentHighestRatedBook() {
    resetRatingStatistics();
    assertThat(yearStatistic.findMostLikedBookThisYear()).isEmpty();
  }

  @Test
  void TestNumberOfBooksThisYear() {
    int numberOfBooks = StatisticTestUtils.getNumberOfBooksThisYear();
    assertThat(numberOfBooks).isEqualTo(3);
  }

  @Test
  void averageRatingExistsAndIsCorrect() {
    // given
    int numberOfBooks = StatisticTestUtils.getNumberOfBooksThisYear();
    double totalRating = StatisticTestUtils.thisYearRating;
    double average = totalRating / numberOfBooks;

    // when
    double expected = yearStatistic.calculateAverageRatingGivenThisYear().get();

    // then
    assertThat(average).isEqualTo(expected);
  }

  @Test
  void haveNoAverageRatingStatisticsWhenThereAreNoStatistics() {
    resetRatingStatistics();
    assertThat(yearStatistic.calculateAverageRatingGivenThisYear()).isEmpty();
  }

  private void resetRatingStatistics() {
    bookService.deleteAll();
    this.yearStatistic = new YearStatistics(predefinedShelfService);
  }
}

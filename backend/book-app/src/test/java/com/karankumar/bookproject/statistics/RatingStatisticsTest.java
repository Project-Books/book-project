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
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.statistics.util.StatisticTestUtils;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayName("RatingStatistics should")
class RatingStatisticsTest {
  private final BookService bookService;
  private final PredefinedShelfService predefinedShelfService;

  private static RatingStatistics ratingStatistics;

  private Book bookWithNoRating;
  private Book bookWithHighestRating;

  @Autowired
  RatingStatisticsTest(BookService bookService, PredefinedShelfService predefinedShelfService) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
  }

  @BeforeEach
  public void setUp() {
    bookService.deleteAll(); // reset
    StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
    bookWithNoRating = StatisticTestUtils.getBookWithLowestRating();
    bookWithHighestRating = StatisticTestUtils.getBookWithHighestRating();

    ratingStatistics = new RatingStatistics(predefinedShelfService);
  }

  @Test
  void findLowestRatedBookExists() {
    String actualTitle = ratingStatistics.findLeastLikedBook().map(Book::getTitle).orElse("");
    String expectedTitle = bookWithNoRating.getTitle();
    assertThat(actualTitle).isEqualTo(expectedTitle);
  }

  @Test
  void notFindNonExistentLowestRatedBook() {
    resetRatingStatistics();
    assertThat(ratingStatistics.findLeastLikedBook()).isEmpty();
  }

  @Test
  void findHighestRatedBook() {
    String actualTitle = ratingStatistics.findMostLikedBook().map(Book::getTitle).orElse("");
    String expectedTitle = bookWithHighestRating.getTitle();
    assertThat(actualTitle).isEqualTo(expectedTitle);
  }

  @Test
  void notFindNonExistentHighestRatedBook() {
    resetRatingStatistics();
    assertThat(ratingStatistics.findMostLikedBook()).isEmpty();
  }

  @Test
  void findAverageRating() {
    // given
    int numberOfBooks = StatisticTestUtils.getNumberOfBooks();
    double totalRating = StatisticTestUtils.totalRating;

    // when
    Double actual = ratingStatistics.calculateAverageRatingGiven().get();

    // then
    double average = totalRating / numberOfBooks;
    assertThat(actual).isEqualTo(average);
  }

  @Test
  void notDivideAverageRatingByZero() {
    resetRatingStatistics();
    assertThat(ratingStatistics.calculateAverageRatingGiven()).isEmpty();
  }

  private void resetRatingStatistics() {
    bookService.deleteAll();
    ratingStatistics = new RatingStatistics(predefinedShelfService);
  }
}

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
import com.karankumar.bookproject.book.model.BookGenre;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.statistics.util.StatisticTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("GenreStatistics should")
@Transactional
class GenreStatisticsTest {
  private final BookService bookService;
  private final PredefinedShelfService predefinedShelfService;

  private static GenreStatistics genreStatistics;

  @Autowired
  GenreStatisticsTest(BookService bookService, PredefinedShelfService predefinedShelfService) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
  }

  @BeforeEach
  public void setUp() {
    resetBookService();
    StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
    genreStatistics = new GenreStatistics(predefinedShelfService);
  }

  private void resetBookService() {
    bookService.deleteAll();
  }

  @Test
  @Transactional
  void findMostReadGenre() {
    // given
    BookGenre expected = StatisticTestUtils.MOST_READ_BOOK_GENRE;

    // when
    genreStatistics.findMostReadGenre();
    BookGenre actual = genreStatistics.findMostReadGenre().get();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findMostLikedGenre() {
    // given
    BookGenre expected = StatisticTestUtils.MOST_LIKED_BOOK_GENRE;

    // when
    BookGenre actual = genreStatistics.findMostLikedGenre().get();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findLeastLikedGenre() {
    // given
    BookGenre expected = StatisticTestUtils.LEAST_LIKED_BOOK_GENRE;

    // when
    BookGenre actual = genreStatistics.findLeastLikedGenre().get();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DisplayName("not show the most liked genre statistic if there is only one read genre")
  @Disabled
  // TODO: this test has been disabled as it needs to be rewritten to make it easier to follow
  void notShowMostLikedGenreWhenOnlyOneReadGenreExists() {
    // given
    resetBookService();
    saveBook();
    genreStatistics = new GenreStatistics(predefinedShelfService);

    // when
    Optional<BookGenre> mostLiked = genreStatistics.findMostLikedGenre();

    // then
    assertThat(mostLiked).isEmpty();
  }

  private void saveBook() {
    StatisticTestUtils.addReadBook(bookService, predefinedShelfService);
  }

  @Test
  @DisplayName("not show the least liked genre statistic if there is only one read genre")
  @Disabled
  // TODO: this test has been disabled as it needs to be rewritten to make it easier to follow
  void notShowLeastLikedGenreWhenOnlyOneReadGenreExists() {
    // given
    resetBookService();
    saveBook();
    genreStatistics = new GenreStatistics(predefinedShelfService);

    // when
    Optional<BookGenre> leastLiked = genreStatistics.findLeastLikedGenre();

    // then
    assertThat(leastLiked).isEmpty();
  }
}

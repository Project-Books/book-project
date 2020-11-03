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

package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.util.StatisticTestUtils;
import com.karankumar.bookproject.annotations.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@DisplayName("GenreStatistics should")
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
    void findMostReadGenre() {
        BookGenre expected = StatisticTestUtils.MOST_READ_BOOK_GENRE;
        BookGenre actual = genreStatistics.findMostReadGenre();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findMostLikedGenre() {
        BookGenre expected = StatisticTestUtils.MOST_LIKED_BOOK_GENRE;
        BookGenre actual = genreStatistics.findMostLikedGenre();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findLeastLikedGenre() {
        BookGenre expected = StatisticTestUtils.LEAST_LIKED_BOOK_GENRE;
        BookGenre actual = genreStatistics.findLeastLikedGenre();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("not show the most liked genre statistic if there is only one read genre")
    void notShowMostLikedGenreWhenOnlyOneReadGenreExists() {
        // given
        resetBookService();
        saveBook();
        genreStatistics = new GenreStatistics(predefinedShelfService);

        // when
        BookGenre mostLiked = genreStatistics.findMostLikedGenre();

        // then
        assertThat(mostLiked).isNull();
    }

    private void saveBook() {
        StatisticTestUtils.addReadBook(bookService, predefinedShelfService);
    }

    @Test
    @DisplayName("not show the least liked genre statistic if there is only one read genre")
    void notShowLeastLikedGenreWhenOnlyOneReadGenreExists() {
        // given
        resetBookService();
        saveBook();
        genreStatistics = new GenreStatistics(predefinedShelfService);

        // when
        BookGenre leastLiked = genreStatistics.findLeastLikedGenre();

        // then
        assertThat(leastLiked).isNull();
    }
}

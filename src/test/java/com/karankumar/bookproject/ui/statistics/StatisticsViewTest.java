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

package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils;

import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksInDifferentGenres;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksDifferentGenresWithoutPageCount;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.AVERAGE_PAGE_LENGTH;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.AVERAGE_RATING;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.LEAST_LIKED_BOOK;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.LEAST_LIKED_GENRE;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.LONGEST_BOOK;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.MOST_LIKED_BOOK;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.MOST_LIKED_GENRE;
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType.MOST_READ_GENRE;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.StatisticNotFound;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBook;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutGenre;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutRatings;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithOnlyOneBook;

@IntegrationTest
@WebAppConfiguration
@DisplayName("StatisticsView should")
class StatisticsViewTest {

    @Autowired private PredefinedShelfService predefinedShelfService;
    @Autowired private BookService bookService;

    private StatisticsView statisticsView;

    @BeforeEach
    public void setUp() {
        bookService.deleteAll();
    }

    @Test
    void createCompleteStatisticsView() {
        // given
        populateDataWithBooksInDifferentGenres(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        allStatisticsAreShown();
        thereAreNotOtherStatistics();
    }

    @Test
    void shouldShowGenreAndRatingStatisticsWhenMoreThanOneGenre() {
        // given
        populateDataWithBooksInDifferentGenres(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        ratingStatisticsArePresent();
    }

    @Test
    void shouldNotShowGenreAndRatingStatisticsWhenLessThanOneGenre() {
        // given
        populateDataWithBook(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        genreAndRatingStatisticsAreAbsent();
    }

    private void allStatisticsAreShown() {
        Arrays.asList(StatisticType.values())
              .forEach(statisticType -> valueIsPresent(getStatistic(statisticType)));
    }

    private void valueIsPresent(StatisticsViewTestUtils.Statistic currentStatistic) {
        assertThat(currentStatistic instanceof StatisticNotFound).isFalse();
    }

    private void thereAreNotOtherStatistics() {
        assertThat(statisticsView.getComponentCount()).isEqualTo(StatisticType.values().length);
    }

    @Test
    void haveAValueForEachStatistic() {
        // given
        populateDataWithBooksInDifferentGenres(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        eachStatisticHasAValue();
    }

    private void eachStatisticHasAValue() {
        Arrays.asList(StatisticType.values())
              .forEach(statisticType -> valueIsPresent(getStatistic(statisticType)));
    }

    @Test
    void showStillOtherStatisticsWithoutPageCount() {
        // given
        populateDataWithBooksDifferentGenresWithoutPageCount(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        pageStatisticsAreAbsent();
        ratingStatisticsArePresent();
        genreStatisticIsPresent();
        genreAndRatingStatisticsArePresent();
    }

    @Test
    void showStillOtherStatisticsWithoutGenreInformation() {
        // given
        populateDataWithBooksWithoutGenre(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        genreStatisticIsAbsent();
        genreAndRatingStatisticsAreAbsent();
        ratingStatisticsArePresent();
        pageStatisticsArePresent();
    }

    @Test
    void notShowTheMostLikedAndLeastLikedBookWithOnlyOneBook() {
        // given
        populateDataWithOnlyOneBook(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        ratingStatisticsAreAbsent();
    }

    @Test
    void showOtherStatisticsWithoutRatingInformation() {
        // given
        populateDataWithBooksWithoutRatings(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        ratingStatisticsAreAbsent();
        genreAndRatingStatisticsAreAbsent();
        genreStatisticIsPresent();
        pageStatisticsArePresent();
    }

    private void pageStatisticsArePresent() {
        statisticsArePresent(LONGEST_BOOK, AVERAGE_PAGE_LENGTH);
    }

    private void pageStatisticsAreAbsent() {
        statisticsAreAbsent(LONGEST_BOOK, AVERAGE_PAGE_LENGTH);
    }

    private void ratingStatisticsArePresent() {
        statisticsArePresent(AVERAGE_RATING, MOST_LIKED_BOOK, LEAST_LIKED_BOOK);
    }

    private void ratingStatisticsAreAbsent() {
        statisticsAreAbsent(StatisticType.AVERAGE_RATING, MOST_LIKED_BOOK, LEAST_LIKED_BOOK);
    }

    private void genreStatisticIsPresent() {
        statisticsArePresent(MOST_READ_GENRE);
    }

    private void genreStatisticIsAbsent() {
        statisticsAreAbsent(MOST_READ_GENRE);
    }

    private void genreAndRatingStatisticsArePresent() {
        statisticsArePresent(MOST_LIKED_GENRE, LEAST_LIKED_GENRE);
    }

    private void genreAndRatingStatisticsAreAbsent() {
        statisticsAreAbsent(MOST_LIKED_GENRE, LEAST_LIKED_GENRE);
    }

    private void statisticsAreAbsent(StatisticType... statisticTypes) {
        for (StatisticType statisticType : statisticTypes) {
            assertThat(getStatistic(statisticType)).isInstanceOf(StatisticNotFound.class);
        }
    }

    private void statisticsArePresent(StatisticType... statisticTypes) {
        for (StatisticType statisticType : statisticTypes) {
            assertThat(getStatistic(statisticType).getCaption())
                    .isEqualTo(statisticType.getCaption());
        }
    }

    private StatisticsViewTestUtils.Statistic getStatistic(StatisticType statisticType) {
        return StatisticsViewTestUtils.getStatistic(statisticType, statisticsView);
    }

    @Test
    void onlyDisplayRatingUnitOnce() {
        // given
        String title = "Harry Potter and the Chamber of Secrets";
        RatingScale rating = RatingScale.NO_RATING;

        // when
        String actual = StatisticsView.formatStatistic(title, rating.toString(), "rating");

        // then
        String expected = String.format("%s (%s)", title, rating.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void includePagesUnitWhenFormatted() {
        // given
        String title = "Harry Potter and the Chamber of Secrets";
        int pages = 500;
        String unit = "pages";

        // when
        String actual = StatisticsView.formatStatistic(title, String.valueOf(pages), unit);

        // then
        String expected = String.format("%s (%s %s)", title, pages, unit);
        assertThat(actual).isEqualTo(expected);
    }
}

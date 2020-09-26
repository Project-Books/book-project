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

package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils;
import org.junit.jupiter.api.BeforeEach;
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
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooks;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutGenre;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutPageCount;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutRatings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
@WebAppConfiguration
class StatisticsViewTest {

    @Autowired private PredefinedShelfService predefinedShelfService;
    @Autowired private BookService bookService;

    private StatisticsView statisticsView;

    @BeforeEach
    public void setup() {
        bookService.deleteAll();
    }

    @Test
    void shouldCreateCompleteStatisticsView() {
        // given
        populateDataWithBooks(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        allStatisticsAreShown();
        thereAreNotOtherStatistics();
    }

    private void allStatisticsAreShown() {
        Arrays.asList(StatisticType.values())
              .forEach(statisticType -> valueIsPresent(getStatistic(statisticType)));
    }

    private void valueIsPresent(StatisticsViewTestUtils.Statistic currentStatistic) {
        assertFalse(currentStatistic instanceof StatisticNotFound);
    }
    
    private void thereAreNotOtherStatistics() {
        assertEquals(StatisticType.values().length, statisticsView.getComponentCount());
    }

    @Test
    void eachStatisticShouldHaveAValue() {
        // given
        populateDataWithBooks(bookService, predefinedShelfService);

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
    void withoutPageCountTheViewShouldShowOtherStatistics() {
        // given
        populateDataWithBooksWithoutPageCount(bookService, predefinedShelfService);

        // when
        statisticsView = new StatisticsView(predefinedShelfService);

        // then
        pageStatisticsAreAbsent();
        ratingStatisticsArePresent();
        genreStatisticIsPresent();
        genreAndRatingStatisticsArePresent();
    }

    @Test
    void withoutGenreInformationTheViewShouldShowOtherStatistics() {
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
    void withoutRatingInformationTheViewShouldShowOtherStatistics() {
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
            assertTrue(getStatistic(statisticType) instanceof StatisticNotFound);
        }
    }

    private void statisticsArePresent(StatisticType... statisticTypes) {
        for (StatisticType statisticType : statisticTypes) {
            assertEquals(statisticType.getCaption(), getStatistic(statisticType).getCaption());
        }
    }

    private StatisticsViewTestUtils.Statistic getStatistic(StatisticType statisticType) {
        return StatisticsViewTestUtils.getStatistic(statisticType, statisticsView);
    }
}

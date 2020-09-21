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
import static com.karankumar.bookproject.ui.statistics.StatisticsView.StatisticType;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooks;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutRatings;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutGenre;
import static com.karankumar.bookproject.ui.statistics.util.StatisticsViewTestUtils.populateDataWithBooksWithoutPageCount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

@IntegrationTest
@WebAppConfiguration
public class StatisticsViewTest {

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

    private void valueIsPresent(Statistic currentStatistic) {
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
        statisticIsPresent(StatisticType.LONGEST_BOOK);
        statisticIsPresent(StatisticType.AVERAGE_PAGE_LENGTH);
    }

    private void pageStatisticsAreAbsent() {
        statisticIsAbsent(StatisticType.LONGEST_BOOK);
        statisticIsAbsent(StatisticType.AVERAGE_PAGE_LENGTH);
    }

    private void ratingStatisticsArePresent() {
        statisticIsPresent(StatisticType.AVERAGE_RATING);
        statisticIsPresent(StatisticType.MOST_LIKED_BOOK);
        statisticIsPresent(StatisticType.LEAST_LIKED_BOOK);
    }

    private void ratingStatisticsAreAbsent() {
        statisticIsAbsent(StatisticType.AVERAGE_RATING);
        statisticIsAbsent(StatisticType.MOST_LIKED_BOOK);
        statisticIsAbsent(StatisticType.LEAST_LIKED_BOOK);
    }

    private void genreStatisticIsPresent() {
        statisticIsPresent(StatisticType.MOST_READ_GENRE);
    }

    private void genreStatisticIsAbsent() {
        statisticIsAbsent(StatisticType.MOST_READ_GENRE);

    }

    private void genreAndRatingStatisticsArePresent() {
        statisticIsPresent(StatisticType.MOST_LIKED_GENRE);
        statisticIsPresent(StatisticType.LEAST_LIKED_GENRE);
    }

    private void genreAndRatingStatisticsAreAbsent() {
        statisticIsAbsent(StatisticType.MOST_LIKED_GENRE);
        statisticIsAbsent(StatisticType.LEAST_LIKED_GENRE);
    }

    private void statisticIsAbsent(StatisticType statisticType) {
        assertTrue(getStatistic(statisticType) instanceof StatisticNotFound);
    }

    private void statisticIsPresent(StatisticType statisticType) {
        assertEquals(statisticType.getCaption(), getStatistic(statisticType).getCaption());
    }

    private Statistic getStatistic(StatisticType statisticType) {
        return StatisticsViewTestUtils.getStatistic(statisticType, statisticsView);
    }

    @AllArgsConstructor
    public static class Statistic {
        @Getter private final String caption;
        @Getter private final String value;
    }

    public static class StatisticNotFound extends Statistic {
        public StatisticNotFound() {
            super("statistic", "not found");
        }
    }
}

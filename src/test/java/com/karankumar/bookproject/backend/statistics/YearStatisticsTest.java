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

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.util.StatisticTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@IntegrationTest
@DisplayName("YearStatistics should")
public class YearStatisticsTest {
    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;

    private static YearStatistics yearStatistic;
    private static Book bookWithLowestRatingThisYear;
    private static Book bookWithHighestRatingThisYear;

    @Autowired
    YearStatisticsTest(BookService bookService, PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void beforeEachSetup() {
        bookService.deleteAll(); // reset
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        bookWithLowestRatingThisYear = StatisticTestUtils.getBookWithLowestRatingThisYear();
        bookWithHighestRatingThisYear = StatisticTestUtils.getBookWithHighestRatingThisYear();

        yearStatistic = new YearStatistics(predefinedShelfService);
    }

    @Test
    void lowestRatedBookThisYearIsFound() {
        assertThat(yearStatistic.findLeastLikedBookThisYear().get().getTitle())
                .isEqualTo(bookWithLowestRatingThisYear.getTitle());
    }

    @Test
    void testNonExistentLowestRatedBook() {
        resetRatingStatistics();
        assertThat(yearStatistic.findLeastLikedBookThisYear()).isEqualTo(Optional.empty());
    }

    @Test
    void highestRatedBookExistsAndIsFound() {
        assertThat(yearStatistic.findMostLikedBookThisYear().get().getTitle()).isEqualTo(bookWithHighestRatingThisYear.getTitle());
    }

    @Test
    void testNonExistentHighestRatedBook() {
        resetRatingStatistics();
        assertThat(yearStatistic.findMostLikedBookThisYear()).isEqualTo(Optional.empty());
    }

    @Test
    void TestNumberOfBooksThisYear() {
        int numberOfBooks = StatisticTestUtils.getNumberOfBooksThisYear();
        assertThat(numberOfBooks).isEqualTo(3);
    }

    @Test
    void averageRatingExistsAndIsCorrect() {
        int numberOfBooks = StatisticTestUtils.getNumberOfBooksThisYear();
        System.out.print(StatisticTestUtils.getNumberOfBooksThisYear());
        double totalRating = StatisticTestUtils.thisYearRating;
        double average = totalRating / numberOfBooks;
        assertThat(average).isEqualTo( yearStatistic.calculateAverageRatingGivenThisYear().get());
    }

    @Test
    void testAverageRatingDivideByZero() {
        resetRatingStatistics();
        assertThat(yearStatistic.calculateAverageRatingGivenThisYear()).isEqualTo(Optional.empty());
    }

    private void resetRatingStatistics() {
        bookService.deleteAll();
        this.yearStatistic = new YearStatistics(predefinedShelfService);
    }
}

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@IntegrationTest
public class YearStatisticTest {
    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;

    private static YearStatistic yearStatistic;
    private static Book bookWithLowestRatingThisYear;
    private static Book bookWithHighestRatingThisYear;

    @BeforeAll
    public static void setup(@Autowired BookService bookService,
                             @Autowired PredefinedShelfService predefinedShelfService) {
        YearStatisticTest.bookService = bookService;
        YearStatisticTest.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void beforeEachSetup() {
        bookService.deleteAll(); // reset
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        bookWithLowestRatingThisYear = StatisticTestUtils.getBookWithLowestRatingThisYear();
        bookWithHighestRatingThisYear = StatisticTestUtils.getBookWithHighestRatingThisYear();

        yearStatistic = new YearStatistic(predefinedShelfService);
    }

    @Test
    void lowestRatedThisYearBookExistsAndIsFound() {
        assertEquals(bookWithLowestRatingThisYear.getTitle(), yearStatistic.findLeastLikedBookThisYear().getTitle());
    }

    @Test
    void testNonExistentLowestRatedBook() {
        resetRatingStatistics();
        assertNull(yearStatistic.findLeastLikedBookThisYear());
    }

    @Test
    void highestRatedBookExistsAndIsFound() {
        assertEquals(bookWithHighestRatingThisYear.getTitle(),
                yearStatistic.findMostLikedBookThisYear().getTitle());
    }

    @Test
    void testNonExistentHighestRatedBook() {
        resetRatingStatistics();
        assertNull(yearStatistic.findMostLikedBookThisYear());
    }

    @Test
    void TestnumberOfBooksThisYear() {
        int numberOfBooks = StatisticTestUtils.getNumberOfBooksThisYear();
        assertEquals(3, numberOfBooks);
    }

    @Test
    void averageRatingExistsAndIsCorrect() {
        int numberOfBooks = StatisticTestUtils.getNumberOfBooksThisYear();
        double totalRating = StatisticTestUtils.thisYearRating;
        double average = totalRating / numberOfBooks;
        assertEquals(average, yearStatistic.calculateAverageRatingGivenThisYear());
    }

    @Test
    void testAverageRatingDivideByZero() {
        resetRatingStatistics();
        assertNull(yearStatistic.calculateAverageRatingGivenThisYear());
    }

    private void resetRatingStatistics() {
        bookService.deleteAll();
        yearStatistic = new YearStatistic(predefinedShelfService);
    }
}

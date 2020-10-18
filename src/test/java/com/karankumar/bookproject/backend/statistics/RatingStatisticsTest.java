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
import com.karankumar.bookproject.backend.statistics.utils.StatisticTestUtils;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
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
    void lowestRatedBookExistsAndIsFound() {
        String actualTitle = ratingStatistics.findLeastLikedBook().getTitle();
        String expectedTitle = bookWithNoRating.getTitle();
        assertThat(actualTitle).isEqualTo(expectedTitle);
    }

    @Test
    void testNonExistentLowestRatedBook() {
        resetRatingStatistics();
        assertThat(ratingStatistics.findLeastLikedBook()).isNull();
    }

    @Test
    void highestRatedBookExistsAndIsFound() {
        String actualTitle = ratingStatistics.findMostLikedBook().getTitle();
        String expectedTitle = bookWithHighestRating.getTitle();
        assertThat(actualTitle).isEqualTo(expectedTitle);
    }

    @Test
    void testNonExistentHighestRatedBook() {
        resetRatingStatistics();
        assertThat(ratingStatistics.findMostLikedBook()).isNull();
    }

    @Test
    void averageRatingExistsAndIsCorrect() {
        int numberOfBooks = StatisticTestUtils.getNumberOfBooks();
        double totalRating = StatisticTestUtils.totalRating;
        double average = totalRating / numberOfBooks;
        assertThat(ratingStatistics.calculateAverageRatingGiven()).isEqualTo(average);
    }

    @Test
    void testAverageRatingDivideByZero() {
        resetRatingStatistics();
        assertThat(ratingStatistics.calculateAverageRatingGiven()).isNull();
    }

    private void resetRatingStatistics() {
        bookService.deleteAll();
        ratingStatistics = new RatingStatistics(predefinedShelfService);
    }
}

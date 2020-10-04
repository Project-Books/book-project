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

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.utils.StatisticTestUtils;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@IntegrationTest
@DisplayName("PageStatistics should")
class PageStatisticsTest {
    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;
    private static PageStatistics pageStatistics;

    @BeforeAll
    public static void setupBeforeAll(@Autowired BookService bookService,
                                      @Autowired PredefinedShelfService predefinedShelfService) {
        PageStatisticsTest.bookService = bookService;
        PageStatisticsTest.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void beforeEachSetup() {
        bookService.deleteAll();
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        PageStatisticsTest.pageStatistics = new PageStatistics(predefinedShelfService);
    }

    @Test
    void findBookWithMostPages() {
        assertEquals(
                StatisticTestUtils.getBookWithMostPages().getTitle(),
                pageStatistics.findBookWithMostPages().getTitle()
        );
    }

    @Test
    void onlyReadBooksCountTowardsMostPagesStatistics() {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        PredefinedShelf readingShelf = predefinedShelfUtils.findReadingShelf();

        Book readingBook = new Book("More pages than any read book",
                new Author("Joe", "Bloggs"), readingShelf);
        readingBook.setNumberOfPages(StatisticTestUtils.getBookWithMostPages()
                                                       .getNumberOfPages() + 50);
        bookService.save(readingBook);

        assertEquals(StatisticTestUtils.getBookWithMostPages().getTitle(),
                pageStatistics.findBookWithMostPages().getTitle());
    }

    @Test
    void calculateAveragePageLengthDivideByZero() {
        resetPageStatistics();
        assertNull(pageStatistics.calculateAveragePageLength());
    }

    @Test
    void calculateAveragePageLength() {
        int averagePageLength =
                StatisticTestUtils.getTotalNumberOfPages() / StatisticTestUtils.getNumberOfBooks();
        assertEquals(averagePageLength, pageStatistics.calculateAveragePageLength());
    }

    private void resetPageStatistics() {
        bookService.deleteAll();
        pageStatistics = new PageStatistics(predefinedShelfService);
    }
}

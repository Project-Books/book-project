package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.utils.StatisticTestUtils;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class PageStatisticsTest {
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
    public void bookWithMostPagesExistsAndIsFound() {
        Assertions.assertEquals(
                StatisticTestUtils.getBookWithMostPages().getTitle(),
                pageStatistics.findBookWithMostPages().getTitle()
        );
    }

    @Test
    public void onlyReadBooksCountTowardsMostPagesStatistics() {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        PredefinedShelf readingShelf = predefinedShelfUtils.findReadingShelf();

        Book readingBook = new Book("More pages than any read book", new Author("Joe", "Bloggs"), readingShelf);
        readingBook.setNumberOfPages(StatisticTestUtils.getBookWithMostPages().getNumberOfPages() + 50);
        bookService.save(readingBook);

        Assertions.assertEquals(StatisticTestUtils.getBookWithMostPages().getTitle(),
                pageStatistics.findBookWithMostPages().getTitle());
    }

    @Test
    public void testAveragePageLengthDivideByZero() {
        resetPageStatistics();
        Assertions.assertNull(pageStatistics.calculateAveragePageLength());
    }

    @Test
    public void averagePageLengthExistsAndIsCorrect() {
        int averagePageLength = StatisticTestUtils.getTotalNumberOfPages() / StatisticTestUtils.getNumberOfBooks();
        Assertions.assertEquals(averagePageLength, pageStatistics.calculateAveragePageLength());
    }

    private void resetPageStatistics() {
        bookService.deleteAll();
        pageStatistics = new PageStatistics(predefinedShelfService);
    }
}

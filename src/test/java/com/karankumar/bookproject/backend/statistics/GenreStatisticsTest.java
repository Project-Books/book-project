package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.utils.StatisticTestUtils;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class GenreStatisticsTest {

    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;
    private static GenreStatistics genreStatistics;

    private Book highestRatedBook;
    private Book lowestRatedBook;

    @BeforeAll
    public static void setupBeforeAll(@Autowired BookService bookService,
                                      @Autowired PredefinedShelfService predefinedShelfService) {
        GenreStatisticsTest.bookService = bookService;
        GenreStatisticsTest.predefinedShelfService = predefinedShelfService;

        GenreStatisticsTest.genreStatistics = new GenreStatistics(predefinedShelfService);
    }

    @BeforeEach
    public void setupBeforeEach() {
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        highestRatedBook = StatisticTestUtils.getBookWithHighestRating();
        lowestRatedBook = StatisticTestUtils.getBookWithLowestRating();
    }

    @Test
    public void mostReadGenreExistsAndIsFound() {
        // TODO
    }

    @Test
    public void mostLikedGenreExistsAndIsFound() {
        // TODO
    }

    @Test
    public void leastLikedGenreExistsAndIsFound() {
        // TODO
    }

}

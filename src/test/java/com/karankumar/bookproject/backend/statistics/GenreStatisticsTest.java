package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.utils.StatisticTestUtils;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class GenreStatisticsTest {

    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;
    private static GenreStatistics genreStatistics;

    @BeforeAll
    public static void setupBeforeAll(@Autowired BookService bookService,
                                      @Autowired PredefinedShelfService predefinedShelfService) {
        GenreStatisticsTest.bookService = bookService;
        GenreStatisticsTest.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void setupBeforeEach() {
        bookService.deleteAll();
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        GenreStatisticsTest.genreStatistics = new GenreStatistics(predefinedShelfService);
    }

    @Test
    public void mostReadGenreExistsAndIsFound() {
        Assertions.assertEquals(genreStatistics.findMostReadGenre(), StatisticTestUtils.mostPopularGenre);
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

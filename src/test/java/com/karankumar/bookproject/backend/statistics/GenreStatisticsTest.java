package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.utils.StatisticTestUtils;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class GenreStatisticsTest {
    private static GenreStatistics genreStatistics;

    @BeforeAll
    public static void setup(@Autowired BookService bookService,
                             @Autowired PredefinedShelfService predefinedShelfService) {
        bookService.deleteAll();
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        GenreStatisticsTest.genreStatistics = new GenreStatistics(predefinedShelfService);
    }

    @Test
    public void mostReadGenreExistsAndIsFound() {
        Assertions.assertEquals(genreStatistics.findMostReadGenre(), StatisticTestUtils.mostReadGenre);
    }

    @Test
    public void mostLikedGenreExistsAndIsFound() {
        Assertions.assertEquals(genreStatistics.findMostLikedGenre(), StatisticTestUtils.mostLikedGenre);
    }

    @Test
    public void leastLikedGenreExistsAndIsFound() {
        Assertions.assertEquals(genreStatistics.findLeastLikedGenre(), StatisticTestUtils.leastLikedGenre);
    }
}

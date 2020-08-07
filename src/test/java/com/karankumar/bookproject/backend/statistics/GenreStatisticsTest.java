package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class GenreStatisticsTest {

    private static BookService bookStatistics;
    private static GenreStatistics genreStatistics;

    @BeforeAll
    public static void setup(@Autowired BookService bookService, @Autowired PredefinedShelfService predefinedShelfService) {
        GenreStatisticsTest.bookStatistics = bookService;
        GenreStatisticsTest.genreStatistics = new GenreStatistics(predefinedShelfService);
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

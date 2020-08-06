package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class RatingStatisticsTest {
    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;

    private static RatingStatistics ratingStatistics;
    private static Book bookWithNoRating;
    private static Book bookWithHighestRating;

    @BeforeAll
    public static void setup(@Autowired BookService bookService,
                             @Autowired PredefinedShelfService predefinedShelfService) {
        RatingStatisticsTest.bookService = bookService;
        RatingStatisticsTest.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void beforeEachSetup() {
        populateReadBooks();
        ratingStatistics = new RatingStatistics(predefinedShelfService);
    }

    private void populateReadBooks() {
        bookService.deleteAll();

        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();

        Author author = new Author("Joe", "Bloggs");
        bookWithNoRating = new Book("Book1", author);
        bookWithNoRating.setRating(RatingScale.NO_RATING);
        bookWithNoRating.setShelf(readShelf);
        bookService.save(bookWithNoRating);

        bookWithHighestRating = new Book("Book2", author);
        bookWithHighestRating.setRating(RatingScale.NINE_POINT_FIVE);
        bookWithHighestRating.setShelf(readShelf);
        bookService.save(bookWithHighestRating);

        Book book3 = new Book("Book3", author);
        book3.setRating(RatingScale.SIX);
        book3.setShelf(readShelf);
        bookService.save(book3);
    }

    @Test
    public void lowestRatedBookExistsAndIsFound() {
        Assertions.assertEquals(bookWithNoRating.getTitle(), ratingStatistics.findLeastLikedBook().getTitle());
    }

    @Test
    public void highestRatedBookExistsAndIsFound() {
        Assertions.assertEquals(bookWithHighestRating.getTitle(), ratingStatistics.findMostLikedBook().getTitle());
    }

    @Test
    public void testAverageRatingDivideByZero() {
        resetRatingStatistics();
        Assertions.assertNull(ratingStatistics.calculateAverageRatingGiven());

    }

    private void resetRatingStatistics() {
        bookService.deleteAll();
        ratingStatistics = new RatingStatistics(predefinedShelfService);
    }
}

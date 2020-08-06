package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.AuthorService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RatingStatisticsTest {

    private static RatingStatistics ratingStatistics;
    private static Book bookWithNoRating;
    private static Book bookWithHighestRating;

    @BeforeAll
    public static void setup(@Autowired BookService bookService,
                             @Autowired AuthorService authorService,
                             @Autowired PredefinedShelfService predefinedShelfService) {
        bookService.deleteAll();

        Author author = new Author("Joe", "Bloggs");

        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();

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

        ratingStatistics = new RatingStatistics(predefinedShelfService);
    }

    @Test
    public void lowestRatedBookExistsAndIsFound() {
        Assertions.assertEquals(bookWithNoRating.getId(), ratingStatistics.findLeastLikedBook().getId());
    }

    @Test
    public void highestRatedBookExistsAndIsFound() {
        Assertions.assertEquals(bookWithHighestRating.getId(), ratingStatistics.findMostLikedBook().getId());
    }

    @Test
    public void testAverageRatingDivideByZero() {
        // TODO
    }
}

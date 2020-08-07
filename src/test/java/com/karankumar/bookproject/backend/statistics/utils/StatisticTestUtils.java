package com.karankumar.bookproject.backend.statistics.utils;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;

public class StatisticTestUtils {

    private static Book bookWithNoRating;
    private static Book bookWithHighestRating;

    private StatisticTestUtils() {}

    public static void populateReadBooks(BookService bookService, PredefinedShelfService predefinedShelfService) {
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

    public static Book getBookWithNoRating() {
        return bookWithNoRating;
    }

    public static Book getBookWithHighestRating() {
        return bookWithHighestRating;
    }
}

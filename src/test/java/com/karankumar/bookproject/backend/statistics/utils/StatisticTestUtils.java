package com.karankumar.bookproject.backend.statistics.utils;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;

public class StatisticTestUtils {

    private static PredefinedShelfService predefinedShelfService;
    private static Book bookWithLowestRating;
    private static Book bookWithHighestRating;

    public static final Genre mostReadGenre = Genre.ADVENTURE;
    public static final Genre mostLikedGenre = Genre.SCIENCE;
    public static final Genre leastLikedGenre = Genre.YOUNG_ADULT;

    private StatisticTestUtils() {}

    public static void populateReadBooks(BookService bookService, PredefinedShelfService predefinedShelfService) {
        bookService.deleteAll();
        StatisticTestUtils.predefinedShelfService = predefinedShelfService;

        bookWithLowestRating = createReadBook("Book1", RatingScale.NO_RATING, Genre.BUSINESS);
        bookService.save(bookWithLowestRating);
        bookWithHighestRating = createReadBook("Book2", RatingScale.NINE_POINT_FIVE, mostReadGenre);
        bookService.save(bookWithHighestRating);
        bookService.save(createReadBook("Book3", RatingScale.SIX, mostReadGenre));
        bookService.save(createReadBook("Book4", RatingScale.ONE, mostReadGenre));
        bookService.save(createReadBook("Book5", RatingScale.NINE, mostLikedGenre));
        bookService.save(createReadBook("Book6", RatingScale.EIGHT_POINT_FIVE, mostLikedGenre));
        bookService.save(createReadBook("Book7", RatingScale.ZERO, leastLikedGenre));
    }

    private static Book createReadBook(String bookTitle, RatingScale rating, Genre genre) {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();

        Author author = new Author("Joe", "Bloggs");

        Book book = new Book(bookTitle, author);
        book.setRating(rating);
        book.setShelf(readShelf);
        book.setGenre(genre);
        return book;
    }

    public static Book getBookWithLowestRating() {
        return bookWithLowestRating;
    }

    public static Book getBookWithHighestRating() {
        return bookWithHighestRating;
    }
}

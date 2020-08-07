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

    public static final Genre mostReadGenre = Genre.ADVENTURE;
    public static final Genre mostLikedGenre = Genre.SCIENCE;
    public static final Genre leastLikedGenre = Genre.YOUNG_ADULT;

    private static Book bookWithLowestRating;
    private static Book bookWithHighestRating;
    private static Book bookWithMostPages;

    private static PredefinedShelfUtils predefinedShelfUtils;

    private StatisticTestUtils() {}

    public static void populateReadBooks(BookService bookService, PredefinedShelfService predefinedShelfService) {
        bookService.deleteAll();
        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);

        bookWithLowestRating = createReadBook("Book1", RatingScale.NO_RATING, Genre.BUSINESS, 100);
        bookService.save(bookWithLowestRating);
        bookWithHighestRating = createReadBook("Book2", RatingScale.NINE_POINT_FIVE, mostReadGenre, 150);
        bookService.save(bookWithHighestRating);
        bookService.save(createReadBook("Book3", RatingScale.SIX, mostReadGenre, 200));
        bookService.save(createReadBook("Book4", RatingScale.ONE, mostReadGenre, 250));
        bookService.save(createReadBook("Book5", RatingScale.NINE, mostLikedGenre,300));
        bookService.save(createReadBook("Book6", RatingScale.EIGHT_POINT_FIVE, mostLikedGenre, 350));
        bookWithMostPages = createReadBook("Book7", RatingScale.ZERO, leastLikedGenre, 400);
        bookService.save(bookWithMostPages);
    }

    private static Book createReadBook(String bookTitle, RatingScale rating, Genre genre, int pages) {
        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();

        Book book = createBook(bookTitle, readShelf, genre, pages);
        book.setRating(rating);
        return book;
    }

    private static Book createBook(String bookTitle, PredefinedShelf shelf, Genre genre, int pages) {
        Author author = new Author("Joe", "Bloggs");
        Book book = new Book(bookTitle, author);
        book.setShelf(shelf);
        book.setGenre(genre);
        book.setNumberOfPages(pages);
        return book;
    }

    public static Book getBookWithLowestRating() {
        return bookWithLowestRating;
    }

    public static Book getBookWithHighestRating() {
        return bookWithHighestRating;
    }

    public static Book getBookWithMostPages() {
        return bookWithMostPages;
    }
}

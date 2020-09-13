/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.statistics.utils;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;

import java.util.ArrayList;

public class StatisticTestUtils {

    public static final Genre mostReadGenre = Genre.ADVENTURE;
    public static final Genre mostLikedGenre = Genre.SCIENCE;
    public static final Genre leastLikedGenre = Genre.YOUNG_ADULT;

    private static Book bookWithLowestRating;
    private static Book bookWithHighestRating;
    private static Book bookWithMostPages;
    private static ArrayList<Book> savedBooks = new ArrayList<>();

    private static BookService bookService;
    private static PredefinedShelfUtils predefinedShelfUtils;

    public static double totalRating = 0.0;

    private StatisticTestUtils() {}

    public static void populateReadBooks(BookService bookService,
                                         PredefinedShelfService predefinedShelfService) {
        StatisticTestUtils.bookService = bookService;
        bookService.deleteAll();
        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);

        bookWithLowestRating =
                createReadBook("Book1", RatingScale.NO_RATING, Genre.BUSINESS, 100);
        bookWithHighestRating =
                createReadBook("Book2", RatingScale.NINE_POINT_FIVE, mostReadGenre, 150);
        createReadBook("Book3", RatingScale.SIX, mostReadGenre, 200);
        createReadBook("Book4", RatingScale.ONE, mostReadGenre, 250);
        createReadBook("Book5", RatingScale.NINE, mostLikedGenre, 300);
        createReadBook("Book6", RatingScale.EIGHT_POINT_FIVE, mostLikedGenre, 350);
        bookWithMostPages = createReadBook("Book7", RatingScale.ZERO, leastLikedGenre, 400);
    }

    private static Book createReadBook(String bookTitle, RatingScale rating, Genre genre, int pages) {
        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();
        Book book = createBook(bookTitle, readShelf, genre, pages);
        book.setRating(rating);

        saveBook(book); // this should be called here & not in createBook()
        updateTotalRating(rating);

        return book;
    }

    private static Book createBook(String bookTitle, PredefinedShelf shelf, Genre genre, int pages) {
        Author author = new Author("Joe", "Bloggs");
        Book book = new Book(bookTitle, author, shelf);
        book.setGenre(genre);
        book.setNumberOfPages(pages);
        return book;
    }

    private static void saveBook(Book bookToSave) {
        bookService.save(bookToSave);
        savedBooks.add(bookToSave);
    }

    private static void updateTotalRating(RatingScale ratingScale) {
        Double rating = RatingScale.toDouble(ratingScale);
        if (rating != null) {
            totalRating += rating;
        }
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

    public static int getNumberOfBooks() {
        return savedBooks.size();
    }

    public static int getTotalNumberOfPages() {
        int pages = 0;
        for (Book book : savedBooks) {
            pages += book.getNumberOfPages();
        }
        return pages;
    }
}

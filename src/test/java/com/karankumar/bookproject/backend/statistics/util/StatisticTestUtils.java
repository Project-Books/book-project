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

package com.karankumar.bookproject.backend.statistics.util;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import java.util.ArrayList;

public class StatisticTestUtils {

    public static final BookGenre MOST_READ_BOOK_GENRE = BookGenre.ADVENTURE;
    public static final BookGenre MOST_LIKED_BOOK_GENRE = BookGenre.SCIENCE;
    public static final BookGenre LEAST_LIKED_BOOK_GENRE = BookGenre.YOUNG_ADULT;

    private static Book bookWithLowestRating;
    private static Book bookWithHighestRating;
    private static Book bookWithMostPages;
    private static final ArrayList<Book> savedBooks = new ArrayList<>();

    private static BookService bookService;
    private static PredefinedShelfService predefinedShelfService;

    public static double totalRating = 0.0;

    private StatisticTestUtils() {}

    public static void populateReadBooks(BookService bookService,
                                         PredefinedShelfService predefinedShelfService) {
        StatisticTestUtils.bookService = bookService;
        bookService.deleteAll();
        savedBooks.clear();
        totalRating = 0.0;
        StatisticTestUtils.predefinedShelfService = predefinedShelfService;

        bookWithLowestRating =
                createReadBook("Book1", RatingScale.NO_RATING, BookGenre.BUSINESS, 100);
        bookWithHighestRating =
                createReadBook("Book2", RatingScale.NINE_POINT_FIVE, MOST_READ_BOOK_GENRE, 150);
        createReadBook("Book3", RatingScale.SIX, MOST_READ_BOOK_GENRE, 200);
        createReadBook("Book4", RatingScale.ONE, MOST_READ_BOOK_GENRE, 250);
        createReadBook("Book5", RatingScale.NINE, MOST_LIKED_BOOK_GENRE, 300);
        createReadBook("Book6", RatingScale.EIGHT_POINT_FIVE, MOST_LIKED_BOOK_GENRE, 350);
        bookWithMostPages = createReadBook("Book7", RatingScale.ZERO, LEAST_LIKED_BOOK_GENRE, 400);
    }

    private static Book createReadBook(String bookTitle, RatingScale rating, BookGenre bookGenre, int pages) {
        PredefinedShelf readShelf = predefinedShelfService.findReadShelf();
        Book book = createBook(bookTitle, readShelf, bookGenre, pages);
        book.setRating(rating);

        saveBook(book); // this should be called here & not in createBook()
        updateTotalRating(rating);

        return book;
    }

    private static Book createBook(String bookTitle, PredefinedShelf shelf, BookGenre bookGenre, int pages) {
        Author author = new Author("Joe", "Bloggs");
        Book book = new Book(bookTitle, author, shelf);
        book.setBookGenre(bookGenre);
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

    private static void reduceTotalRating(RatingScale ratingScale) {
        Double rating = RatingScale.toDouble(ratingScale);
        if (rating != null) {
            totalRating -= rating;
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

    public static void deleteBook(Book bookToDelete) {
        bookService.delete(bookToDelete);
        savedBooks.remove(bookToDelete);
        reduceTotalRating(bookToDelete.getRating());
    }
}

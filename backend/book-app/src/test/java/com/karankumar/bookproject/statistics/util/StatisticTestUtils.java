/*
* The book project lets a user keep track of different books they would like to read, are currently
* reading, have read or did not finish.
* Copyright (C) 2020  Karan Kumar

* This program is free software: you can redistribute it and/or modify it under the terms of the
* GNU General Public License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE.  See the GNU General Public License for more details.

* You should have received a copy of the GNU General Public License along with this program.
* If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.statistics.util;

import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.model.BookGenre;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.book.model.RatingScale;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class StatisticTestUtils {

  public static final BookGenre MOST_READ_BOOK_GENRE = BookGenre.ADVENTURE;
  public static final BookGenre MOST_LIKED_BOOK_GENRE = BookGenre.SCIENCE;
  public static final BookGenre LEAST_LIKED_BOOK_GENRE = BookGenre.YOUNG_ADULT;

  private static Book bookWithLowestRating;
  private static Book bookWithHighestRating;
  private static Book bookWithMostPages;
  private static Book bookWithLowestRatingThisYear;
  private static Book BookWithHighestRatingThisYear;
  private static final ArrayList<Book> savedBooks = new ArrayList<>();

  private static BookService bookService;
  private static PredefinedShelfService predefinedShelfService;

  public static double totalRating = 0.0;
  public static double thisYearRating = 0.0;

  private StatisticTestUtils() {}

  public static void populateReadBooks(
      BookService bookService, PredefinedShelfService predefinedShelfService) {
    init(bookService, predefinedShelfService);
    LocalDate currentDate = LocalDate.now();
    LocalDate oldDate = currentDate.minus(Period.ofDays(366));
    bookWithLowestRating =
        createReadBook("Book1", RatingScale.NO_RATING, BookGenre.BUSINESS, 100, oldDate);
    bookWithHighestRating =
        createReadBook("Book2", RatingScale.NINE_POINT_FIVE, MOST_READ_BOOK_GENRE, 150, oldDate);
    createReadBook("Book3", RatingScale.SIX, MOST_READ_BOOK_GENRE, 200, currentDate);
    createReadBook("Book4", RatingScale.ONE, MOST_READ_BOOK_GENRE, 250, oldDate);
    createReadBook("Book5", RatingScale.NINE, MOST_LIKED_BOOK_GENRE, 300, oldDate);
    createReadBook("Book6", RatingScale.EIGHT_POINT_FIVE, MOST_LIKED_BOOK_GENRE, 350, oldDate);
    bookWithMostPages =
        createReadBook("Book7", RatingScale.ZERO, LEAST_LIKED_BOOK_GENRE, 400, oldDate);
    bookWithLowestRatingThisYear =
        createReadBook("Book8", RatingScale.ONE, LEAST_LIKED_BOOK_GENRE, 345, currentDate);
    BookWithHighestRatingThisYear =
        createReadBook(
            "Book9", RatingScale.EIGHT_POINT_FIVE, LEAST_LIKED_BOOK_GENRE, 245, currentDate);
  }

  private static void init(BookService bookService, PredefinedShelfService predefinedShelfService) {
    StatisticTestUtils.bookService = bookService;
    bookService.deleteAll();
    savedBooks.clear();
    totalRating = 0.0;
    thisYearRating = 0.0;
    StatisticTestUtils.predefinedShelfService = predefinedShelfService;
  }

  public static void addReadBook(
      BookService bookService, PredefinedShelfService predefinedShelfService) {
    init(bookService, predefinedShelfService);
    createReadBook("Book", RatingScale.EIGHT, BookGenre.ANTHOLOGY, 200, LocalDate.now());
  }

  private static Book createReadBook(
      String bookTitle,
      RatingScale rating,
      BookGenre bookGenre,
      int pages,
      LocalDate startedReading) {
    PredefinedShelf readShelf = predefinedShelfService.findReadShelf();
    Book book = createBook(bookTitle, readShelf, bookGenre, pages, startedReading);
    book.setRating(rating);

    saveBook(book); // this should be called here & not in createBook()
    updateTotalRating(rating);
    if (startedReading.getYear() == LocalDate.now().getYear()) {
      updateThisYearRating(rating);
    }

    return book;
  }

  private static Book createBook(
      String bookTitle,
      PredefinedShelf shelf,
      BookGenre bookGenre,
      int pages,
      LocalDate startedReading) {
    Author author = new Author("Joe Bloggs");
    Book book = new Book(bookTitle, author, shelf);
    book.setBookGenre(Collections.singleton(bookGenre));
    book.setNumberOfPages(pages);
    book.setDateStartedReading(startedReading);
    return book;
  }

  private static void saveBook(Book bookToSave) {
    bookService.save(bookToSave);
    savedBooks.add(bookToSave);
  }

  private static void updateTotalRating(RatingScale ratingScale) {
    totalRating += RatingScale.toDouble(ratingScale).orElse(0.0);
  }

  private static void reduceTotalRating(RatingScale ratingScale) {
    totalRating -= RatingScale.toDouble(ratingScale).orElse(0.0);
  }

  private static void updateThisYearRating(RatingScale ratingScale) {
    Optional<Double> rating = RatingScale.toDouble(ratingScale);
    rating.ifPresent(x -> thisYearRating += x);
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

  public static Book getBookWithLowestRatingThisYear() {
    return bookWithLowestRatingThisYear;
  }

  public static Book getBookWithHighestRatingThisYear() {
    return BookWithHighestRatingThisYear;
  }

  public static int getNumberOfBooks() {
    return savedBooks.size();
  }

  public static int getNumberOfBooksThisYear() {
    LocalDate dt = LocalDate.now();
    int numberOfBooksThisYear = 0;
    for (Book book : savedBooks) {
      if (book.getDateStartedReading().getYear() == dt.getYear()) {
        numberOfBooksThisYear++;
      }
    }
    return numberOfBooksThisYear;
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

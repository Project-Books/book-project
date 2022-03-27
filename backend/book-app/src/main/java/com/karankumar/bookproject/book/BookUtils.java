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

package com.karankumar.bookproject.book;

import com.karankumar.bookproject.book.model.Book;

import java.util.Optional;

import lombok.NonNull;

public final class BookUtils {
  private BookUtils() {}

  public static String combineTitleAndSeries(Book book) {
    Optional<String> titleWithSeries = addSeriesToTitle(book);
    return (bookHasSeriesPosition(book) && titleWithSeries.isPresent())
        ? titleWithSeries.get()
        : book.getTitle();
  }

  private static boolean bookHasSeriesPosition(Book book) {
    return book.getSeriesPosition() != null && book.getSeriesPosition() > 0;
  }

  private static Optional<String> addSeriesToTitle(@NonNull Book book) {
    String bookTitle = book.getTitle();
    Integer bookSeriesPosition = book.getSeriesPosition();
    if (bookTitle == null || bookSeriesPosition == null) {
      return Optional.empty();
    }
    return Optional.of(String.format("%s (#%d)", bookTitle, bookSeriesPosition));
  }
}

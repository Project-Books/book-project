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

package com.karankumar.bookproject.statistics;

import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PageStatistics extends Statistics {
  private final List<Book> booksWithPageCount;

  public PageStatistics(PredefinedShelfService predefinedShelfService) {
    super(predefinedShelfService);
    booksWithPageCount = findBooksWithPageCountSpecified();
  }

  /**
   * @return the Book in the 'read' shelf with the highest number of pages
   */
  public Optional<Book> findBookWithMostPages() {
    Book bookWithMostPages = null;
    if (!booksWithPageCount.isEmpty()) {
      booksWithPageCount.sort(Comparator.comparing(Book::getNumberOfPages));
      bookWithMostPages = booksWithPageCount.get(booksWithPageCount.size() - 1);
    }
    return Optional.ofNullable(bookWithMostPages);
  }

  private List<Book> findBooksWithPageCountSpecified() {
    List<Book> booksWithNonEmptyPageCount = new ArrayList<>();
    for (Book book : readShelfBooks) {
      if (book.getNumberOfPages() != null) {
        booksWithNonEmptyPageCount.add(book);
      }
    }
    return booksWithNonEmptyPageCount;
  }

  /**
   * @return the average page length for all books in the 'read' shelf This average only includes
   *     books that have a page length specified
   */
  public Optional<Double> calculateAveragePageLength() {
    int totalNumberOfPages = booksWithPageCount.stream().mapToInt(Book::getNumberOfPages).sum();
    int booksWithPagesSpecified = booksWithPageCount.size();
    return (booksWithPagesSpecified == 0)
        ? Optional.empty()
        : Optional.of(Math.ceil(totalNumberOfPages / (float) booksWithPagesSpecified));
  }
}

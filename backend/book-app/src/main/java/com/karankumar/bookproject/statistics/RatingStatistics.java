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
import com.karankumar.bookproject.book.model.RatingScale;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Log
public class RatingStatistics extends Statistics {
  private List<Book> readBooksRated = new ArrayList<>();

  public RatingStatistics(PredefinedShelfService predefinedShelfService) {
    super(predefinedShelfService);
    readBooksRated = findReadBooksWithRatings();
  }

  /**
   * @return the Book in the 'read' shelf with the highest rating If there are multiple books with
   *     the same highest rating, the first one found will be returned
   */
  public Optional<Book> findMostLikedBook() {
    if (readBooksRated.size() <= 1) {
      return Optional.empty();
    }
    readBooksRated.sort(Comparator.comparing(Book::getRating));
    return Optional.of(readBooksRated.get(readBooksRated.size() - 1));
  }

  private List<Book> findReadBooksWithRatings() {
    for (Book book : readShelfBooks) {
      if (book.getRating() != null) {
        readBooksRated.add(book);
      }
    }
    return readBooksRated;
  }

  /**
   * @return the Book in the 'read' shelf with the lowest rating If there are multiple books with
   *     the same lowest rating, the first one found will be returned
   */
  public Optional<Book> findLeastLikedBook() {
    if (readBooksRated.size() <= 1) {
      return Optional.empty();
    }
    readBooksRated.sort(Comparator.comparing(Book::getRating));
    return Optional.of(readBooksRated.get(0));
  }

  /**
   * @return the average rating given to all books in the 'read' If a book in the 'read' shelf does
   *     not have a rating, it is not included in the sum
   */
  public Optional<Double> calculateAverageRatingGiven() {
    int numberOfRatings = readBooksRated.size();
    return (numberOfRatings <= 1)
        ? Optional.empty()
        : Optional.of(calculateTotalRating() / numberOfRatings);
  }

  private double calculateTotalRating() {
    return readBooksRated.stream()
        .mapToDouble(book -> RatingScale.toDouble(book.getRating()).orElse(0.0))
        .sum();
  }
}

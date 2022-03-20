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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class YearStatistics extends Statistics {
  private List<Book> readBooksThisYear = new ArrayList<>();

  public YearStatistics(PredefinedShelfService predefinedShelfService) {
    super(predefinedShelfService);
    readBooksThisYear = findReadBooksAddedThisYear();
  }

  private List<Book> findReadBooksAddedThisYear() {
    for (Book book : readShelfBooks) {
      if (book.getDateStartedReading() != null) {
        LocalDate dt = book.getDateStartedReading();
        LocalDate today = LocalDate.now();
        if ((book.getRating() != null) && (dt.getYear() == today.getYear())) {
          readBooksThisYear.add(book);
        }
      }
    }
    return readBooksThisYear;
  }

  public Optional<Book> findLeastLikedBookThisYear() {
    if (readBooksThisYear.isEmpty()) {
      return Optional.empty();
    }
    readBooksThisYear.sort(Comparator.comparing(Book::getRating));
    return Optional.of(readBooksThisYear.get(0));
  }

  public Optional<Book> findMostLikedBookThisYear() {
    if (readBooksThisYear.isEmpty()) {
      return Optional.empty();
    }
    readBooksThisYear.sort(Comparator.comparing(Book::getRating));
    return Optional.of(readBooksThisYear.get(readBooksThisYear.size() - 1));
  }

  public Optional<Double> calculateAverageRatingGivenThisYear() {
    int numberOfRatings = readBooksThisYear.size();
    if (readBooksThisYear.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of((calculateTotalRating() / numberOfRatings));
  }

  private double calculateTotalRating() {
    return readBooksThisYear.stream()
        .mapToDouble(
            book -> {
              return (RatingScale.toDouble(book.getRating()).isEmpty())
                  ? 0.0
                  : RatingScale.toDouble(book.getRating()).get();
            })
        .sum();
  }
}

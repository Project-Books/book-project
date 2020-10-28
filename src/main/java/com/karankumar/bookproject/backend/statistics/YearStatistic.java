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

package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log
public class YearStatistic extends Statistics {
    private List<Book> readBooksThisYear = new ArrayList<>();

    public YearStatistic(PredefinedShelfService predefinedShelfService) {
        super(predefinedShelfService);
        readBooksThisYear = findReadBooksAddedThisYear();

    }

    private List<Book> findReadBooksAddedThisYear() {
        for (Book book : readShelfBooks) {
            if (book.getDateStartedReading() != null) {
                LocalDate dt = book.getDateStartedReading();
                LocalDate today = LocalDate.now();
                if ((book.getRating() != null)&& (dt.getYear() == today.getYear()) ) {
                    readBooksThisYear.add(book);
                }
            }
        }
        return readBooksThisYear;
    }

    public Book findLeastLikedBookThisYear() {
        if (readBooksThisYear.isEmpty()) {
            return null;
        }
        readBooksThisYear.sort(Comparator.comparing(Book::getRating));
        return readBooksThisYear.get(0);
    }

    public Book findMostLikedBookThisYear() {
        if (readBooksThisYear.isEmpty()) {
            return null;
        }
        readBooksThisYear.sort(Comparator.comparing(Book::getRating));
        return readBooksThisYear.get(readBooksThisYear.size() - 1);
    }

    public Double calculateAverageRatingGivenThisYear() {
        int numberOfRatings = readBooksThisYear.size();
        if (numberOfRatings == 0) {
            return null;
        }
        return (calculateTotalRating() / numberOfRatings);
    }

    private double calculateTotalRating() {
        return readBooksThisYear.stream()
                .mapToDouble(book -> {
                    Double rating = RatingScale.toDouble(book.getRating());
                    rating = (rating == null) ? 0.0 : rating;
                    return rating;
                })
                .sum();
    }
}

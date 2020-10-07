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
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

public class GenreStatistics extends Statistics {
    private final List<Book> readBooksWithGenresAndRatings;

    public GenreStatistics(PredefinedShelfService predefinedShelfService) {
        super(predefinedShelfService);
        readBooksWithGenresAndRatings = findReadBooksWithGenresAndRatings();
    }

    /**
     * @return the Genre that has been read the most (of all time)
     * If no such genre exists, null is returned
     */
    public BookGenre findMostReadGenre() {
        EnumMap<BookGenre, Integer> genresReadCount = countGenreReadOccurrences();
        BookGenre mostReadBookGenre = null;
        for (BookGenre bookGenre : genresReadCount.keySet()) {
            int genreCount = genresReadCount.get(bookGenre);
            if (isGenreTheMostRead(genreCount, mostReadBookGenre, genresReadCount)) {
                mostReadBookGenre = bookGenre;
            }
        }
        return mostReadBookGenre;
    }

    private boolean isGenreTheMostRead(int genreReadCount, BookGenre mostReadGenre,
                                       EnumMap<BookGenre, Integer> genresReadCount) {
        return genreReadCount != 0 &&
                (mostReadGenre == null || genresReadCount.get(mostReadGenre) < genreReadCount);
    }

    private EnumMap<BookGenre, Integer> populateEmptyGenreCount() {
        EnumMap<BookGenre, Integer> genreMap = new EnumMap<>(BookGenre.class);
        for (BookGenre bookGenre : BookGenre.values()) {
            genreMap.put(bookGenre, 0);
        }
        return genreMap;
    }

    private EnumMap<BookGenre, Integer> countGenreReadOccurrences() {
        EnumMap<BookGenre, Integer> genresReadCount = populateEmptyGenreCount();
        for (BookGenre bookGenre : genresReadCount.keySet()) {
            genresReadCount.replace(bookGenre, Collections.frequency(genresRead(), bookGenre));
        }
        return genresReadCount;
    }

    private List<BookGenre> genresRead() {
        return readShelfBooks.stream()
                             .takeWhile(book -> book.getBookGenre() != null)
                             .map(Book::getBookGenre)
                             .collect(Collectors.toList());
    }

    /**
     * @return the Genre with the highest total rating across all books in the read shelf
     * If no such genre exists, null is returned
     */
    public BookGenre findMostLikedGenre() {
        BookGenre mostLikedBookGenre = null;
        List<Map.Entry<BookGenre, Double>> genreRatings = sortGenresByRatings();
        if (!genreRatings.isEmpty()) {
            mostLikedBookGenre = genreRatings.get(genreRatings.size() - 1)
                                             .getKey();
        }
        return mostLikedBookGenre;
    }

    private List<Map.Entry<BookGenre, Double>> sortGenresByRatings() {
        return totalRatingForReadGenre().entrySet().stream()
                                        .sorted(Map.Entry.comparingByValue())
                                        .collect(Collectors.toList());
    }

    private Map<BookGenre, Double> totalRatingForReadGenre() {
        Map<BookGenre, Double> totalRatingForReadGenre = populateEmptyGenreRatings();

        for (Book book : readBooksWithGenresAndRatings) {
            BookGenre bookGenre = book.getBookGenre();
            double totalGenreRating = totalRatingForReadGenre.get(bookGenre);
            Double genreRating = RatingScale.toDouble(book.getRating());
            totalGenreRating += Double.parseDouble(String.valueOf(genreRating));
            totalRatingForReadGenre.replace(bookGenre, totalGenreRating);
        }

        return totalRatingForReadGenre;
    }

    private Map<BookGenre, Double> populateEmptyGenreRatings() {
        // we only want genres in this map that exist in the read books shelf
        Map<BookGenre, Double> genreMap = new EnumMap<>(BookGenre.class);
        for (Book book : readBooksWithGenresAndRatings) {
            genreMap.put(book.getBookGenre(), 0.0);
        }
        return genreMap;
    }

    /**
     * @return the Genre with the lowest total rating across all books in the read shelf
     * If no such genre exists, null is returned
     */
    public BookGenre findLeastLikedGenre() {
        BookGenre leastLikedBookGenre = null;
        List<Map.Entry<BookGenre, Double>> genreRatings = sortGenresByRatings();
        if (!genreRatings.isEmpty()) {
            leastLikedBookGenre = genreRatings.get(0)
                                              .getKey();
        }
        return leastLikedBookGenre;
    }

    private List<Book> findReadBooksWithGenresAndRatings() {
        List<Book> booksWithGenresAndRatings = new ArrayList<>();
        for (Book book : readShelfBooks) {
            if (bookHasGenreAndRating(book)) {
                booksWithGenresAndRatings.add(book);
            }
        }
        return booksWithGenresAndRatings;
    }

    private boolean bookHasGenreAndRating(Book book) {
        return book.getBookGenre() != null && book.getRating() != null &&
                book.getRating() != RatingScale.NO_RATING;
    }
}

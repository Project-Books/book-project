package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GenreStatistics {
    private final Set<Book> readShelfBooks;
    private final DoubleToRatingScaleConverter converter;
    private final List<Book> readBooksWithGenresAndRatings;

    public GenreStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();

        converter = new DoubleToRatingScaleConverter();
        readBooksWithGenresAndRatings = findReadBooksWithGenresAndRatings();
    }

    public Genre findMostReadGenre() {
        HashMap<Genre, Integer> genresCount = populateEmptyGenreCount();
        for (Genre genre : genresCount.keySet()) {
            genresCount.replace(genre, Collections.frequency(readShelfBooks, genre));
        }

        Genre mostReadGenre = null;
        for (Genre genre : genresCount.keySet()) {
            int genreCount = genresCount.get(genre);
            if (genreCount != 0) {
                if (mostReadGenre == null || genresCount.get(mostReadGenre) < genreCount) {
                    mostReadGenre = genre;
                }
            }
        }
        return mostReadGenre;
    }

    private HashMap<Genre, Integer> populateEmptyGenreCount() {
        HashMap<Genre, Integer> genreMap = new HashMap<>();
        for (Genre genre : Genre.values()) {
            genreMap.put(genre, 0);
        }
        return genreMap;
    }

    public Genre findMostLikedGenre() {
        Genre mostLikedGenre = null;
        double mostLikedGenreRating = 0.0;
        for (Book book : readBooksWithGenresAndRatings) {
            double rating = converter.convertToPresentation(book.getRating(), null);
            if (mostLikedGenre == null) {
                mostLikedGenre = book.getGenre();
                mostLikedGenreRating = rating;
            } else if (rating > mostLikedGenreRating) {
                mostLikedGenre = book.getGenre();
                mostLikedGenreRating = rating;
            }
        }
        return mostLikedGenre;
    }

    public Genre findLeastLikedGenre() {
        Genre leastLikedGenre = null;
        double leastLikedGenreRating = 0.0;
        for (Book book : readBooksWithGenresAndRatings) {
            double rating = converter.convertToPresentation(book.getRating(), null);
            if (leastLikedGenre == null) {
                leastLikedGenre = book.getGenre();
                leastLikedGenreRating = rating;
            } else if (rating < leastLikedGenreRating) {
                leastLikedGenre = book.getGenre();
                leastLikedGenreRating = rating;
            }
        }
        return leastLikedGenre;
    }

    private List<Book> findReadBooksWithGenresAndRatings() {
        List<Book> booksWithGenres = new ArrayList<>();
        for (Book book : readShelfBooks) {
            if (book.getGenre() != null && book.getRating() != null) {
                booksWithGenres.add(book);
            }
        }
        return booksWithGenres;
    }
}

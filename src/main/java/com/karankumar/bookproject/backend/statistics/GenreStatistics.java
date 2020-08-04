package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Set;

@Log
public class GenreStatistics {
    private final Set<Book> readShelfBooks;
    private final DoubleToRatingScaleConverter converter;

    public GenreStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();

        converter = new DoubleToRatingScaleConverter();
    }

    public Genre findMostReadGenre() {
        HashMap<Genre, Integer> genresCount = populateEmptyGenreCount();
        for (Book book : readShelfBooks) {
            if (book.getGenre() != null) {
                int previousCount = genresCount.get(book.getGenre());
                genresCount.replace(book.getGenre(), previousCount + 1);
            }
        }

        Genre mostReadGenre = null;
        for (Genre genre : genresCount.keySet()) {
            double genreCount = genresCount.get(genre);
            if (mostReadGenre == null || genresCount.get(mostReadGenre) < genreCount) {
                mostReadGenre = genre;
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
        HashMap<Genre, Double> genresTotalRating = populateEmptyGenreRatings();
        for (Book book : readShelfBooks) {
            if (book.getGenre() != null && book.getRating() != null) {
                double totalRatingForGenre = genresTotalRating.get(book.getGenre()) +
                        converter.convertToPresentation(book.getRating(), null);
                genresTotalRating.replace(book.getGenre(), totalRatingForGenre);
            }
        }

        Genre mostLikedGenre = null;
        double mostLikedGenreRating = 0.0;
        for (Genre genre : genresTotalRating.keySet()) {
            double genreRating = genresTotalRating.get(genre);
            if (mostLikedGenre == null) {
                mostLikedGenre = genre;
                mostLikedGenreRating = genreRating;
            } else if (mostLikedGenreRating < genreRating) {
                mostLikedGenre = genre;
                mostLikedGenreRating = genreRating;
            }
        }
        return mostLikedGenre;
    }

    private HashMap<Genre, Double> populateEmptyGenreRatings() {
        HashMap<Genre, Double> genreMap = new HashMap<>();
        for (Genre genre : Genre.values()) {
            genreMap.put(genre, 0.0);
        }
        return genreMap;
    }

    public Genre findLeastLikedGenre() {
        HashMap<Genre, Double> genresTotalRating = populateEmptyGenreRatings();
        for (Book book : readShelfBooks) {
            if (book.getGenre() != null && book.getRating() != null) {
                double totalRatingForGenre = genresTotalRating.get(book.getGenre()) +
                        converter.convertToPresentation(book.getRating(), null);
                genresTotalRating.replace(book.getGenre(), totalRatingForGenre);
            }
        }

        Genre leastLikedGenre = null;
        double leastLikedGenreRating = 0.0;
        for (Genre genre : genresTotalRating.keySet()) {
            double genreRating = genresTotalRating.get(genre);
            if (leastLikedGenre == null) {
                leastLikedGenre = genre;
                leastLikedGenreRating = genreRating;
            } else if (leastLikedGenreRating > genreRating) {
                leastLikedGenre = genre;
                leastLikedGenreRating = genreRating;
            }
        }
        return leastLikedGenre;
    }
}

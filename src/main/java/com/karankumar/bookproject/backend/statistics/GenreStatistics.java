package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * @return the Genre that has been read the most (of all time)
     * If no such genre exists, null is returned
     */
    public Genre findMostReadGenre() {
        HashMap<Genre, Integer> genresReadCount = countGenreReadOccurrences();
        Genre mostReadGenre = null;
        for (Genre genre : genresReadCount.keySet()) {
            int genreCount = genresReadCount.get(genre);
            if (genreCount != 0) {
                if (mostReadGenre == null || genresReadCount.get(mostReadGenre) < genreCount) {
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

    private HashMap<Genre, Integer> countGenreReadOccurrences() {
        HashMap<Genre, Integer> genresReadCount = populateEmptyGenreCount();
        for (Genre genre : genresReadCount.keySet()) {
            genresReadCount.replace(genre, Collections.frequency(genresRead(), genre));
        }
        return genresReadCount;
    }

    private List<Genre> genresRead() {
        return readShelfBooks.stream()
                             .takeWhile(book -> book.getGenre() != null)
                             .map(Book::getGenre)
                             .collect(Collectors.toList());
    }

    /**
     * @return the Genre with the highest total rating across all books in the read shelf
     * If no such genre exists, null is returned
     */
    public Genre findMostLikedGenre() {
        Genre mostLikedGenre = null;
        List<Map.Entry<Genre, Double>> genreRatings = totalRatingForReadGenre().entrySet().stream()
                                                                               .sorted(Map.Entry.comparingByValue())
                                                                               .collect(Collectors.toList());
        if (genreRatings.size() > 0) {
            mostLikedGenre = genreRatings.get(genreRatings.size() - 1)
                                         .getKey();
        }
        return mostLikedGenre;
    }

    private Map<Genre, Double> totalRatingForReadGenre() {
        Map<Genre, Double> totalRatingForReadGenre = populateEmptyGenreRatings();

        for (Book book : readBooksWithGenresAndRatings) {
            Genre genre = book.getGenre();
            double totalGenreRating = totalRatingForReadGenre.get(genre);
            double genreRating = converter.convertToPresentation(book.getRating(), null);
            totalGenreRating += genreRating;
            totalRatingForReadGenre.replace(genre, totalGenreRating);
        }

        return totalRatingForReadGenre;
    }

    private HashMap<Genre, Double> populateEmptyGenreRatings() {
        HashMap<Genre, Double> genreMap = new HashMap<>();
        for (Genre genre : Genre.values()) {
            genreMap.put(genre, 0.0);
        }
        return genreMap;
    }

    /**
     * @return the Genre with the lowest total rating across all books in the read shelf
     * If no such genre exists, null is returned
     */
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
        List<Book> booksWithGenresAndRatings = new ArrayList<>();
        for (Book book : readShelfBooks) {
            if (book.getGenre() != null && book.getRating() != null && book.getRating() != RatingScale.NO_RATING) {
                booksWithGenresAndRatings.add(book);
            }
        }
        return booksWithGenresAndRatings;
    }
}

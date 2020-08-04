package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@Log
public class CalculateBookStatistics {
    private final Set<Book> readShelfBooks;
    private final DoubleToRatingScaleConverter converter;

    public CalculateBookStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();

        converter = new DoubleToRatingScaleConverter();

        LOGGER.log(Level.INFO, "Most liked book: " + findMostLikedBook());
        LOGGER.log(Level.INFO, "Least liked book: " + findLeastLikedBook());
    }

    public Book findBookWithMostPages() {
        Book bookWithMostPages = null;
        for (Book book : readShelfBooks) {
            if (book.getNumberOfPages() != null) {
                if (bookWithMostPages == null) {
                    bookWithMostPages = book;
                } else {
                    bookWithMostPages =
                            (book.getNumberOfPages() > bookWithMostPages.getNumberOfPages()) ? book : bookWithMostPages;
                }
            }
        }
        return bookWithMostPages;
    }

    // This average only includes books that have a page length specified
    public int calculateAveragePageLength() {
        int totalNumberOfPages = 0;
        int booksWithPagesSpecified = 0;
        for (Book book : readShelfBooks) {
            if (book.getNumberOfPages() != null) {
                totalNumberOfPages += book.getNumberOfPages();
                booksWithPagesSpecified++;
            }
        }
        return (booksWithPagesSpecified == 0) ? 0 : (int) Math.ceil(totalNumberOfPages / booksWithPagesSpecified);
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

    public double calculateAverageRatingGiven() {
        double totalRating = 0;
        int numberOfRatings = 0;
        for (Book book : readShelfBooks) {
            if (book.getRating() != null) {
                totalRating += converter.convertToPresentation(book.getRating(), null);
                numberOfRatings++;
            }
        }

        return (totalRating / numberOfRatings);
    }

    public Book findMostLikedBook() {
        List<Book> readBooksRated = findReadBooksWithRatings();
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(readBooksRated.size() - 1);
    }

    private List<Book> findReadBooksWithRatings() {
        List<Book> readBooksRated = new ArrayList<>();
        for (Book book : readShelfBooks) {
            if (book.getRating() != null) {
                readBooksRated.add(book);
            }
        }
        return readBooksRated;
    }

    public Book findLeastLikedBook() {
        List<Book> readBooksRated = findReadBooksWithRatings();
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(0);
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

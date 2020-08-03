package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@Log
public class CalculateBookStatistics {
    private final Set<Book> readShelfBooks;
    private final DoubleToRatingScaleConverter converter;

    public CalculateBookStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();

        converter = new DoubleToRatingScaleConverter();
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

    public void calculateMostReadGenres() {
        // TODO
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

        Book mostLikedBook = null;

        for (Book book : readShelfBooks) {
            if (book.getRating() != null) {
                if (mostLikedBook == null) {
                    mostLikedBook = book;
                } else {
                    mostLikedBook =
                            getRatingEnumPosition(book.getRating()) > getRatingEnumPosition(mostLikedBook.getRating()) ?
                                    book : mostLikedBook;
                }
            }
        }

        return mostLikedBook;
    }

    private int getRatingEnumPosition(RatingScale rating) {
        ArrayList<RatingScale> ratings = new ArrayList<>(Arrays.asList(RatingScale.values()));
        return ratings.indexOf(rating);
    }

    public void findLeastLikedBook() {
        // TODO
    }

    public void findMostLikedGenres() {
        // TODO
    }

    public void findLeastLikedGenres() {
        // TODO
    }
}

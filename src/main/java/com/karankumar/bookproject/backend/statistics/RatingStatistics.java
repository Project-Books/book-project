package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log
public class RatingStatistics extends Statistics {
    private static final DoubleToRatingScaleConverter converter = new DoubleToRatingScaleConverter();
    private List<Book> readBooksRated = new ArrayList<>();

    public RatingStatistics(PredefinedShelfService predefinedShelfService) {
        super(predefinedShelfService);
        readBooksRated = findReadBooksWithRatings();
    }

    /**
     * @return the Book in the 'read' shelf with the highest rating
     * If there are multiple books with the same highest rating, the first one found will be returned
     */
    public Book findMostLikedBook() {
        if (readBooksRated.size() == 0) {
            return null;
        }
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(readBooksRated.size() - 1);
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
     * @return the Book in the 'read' shelf with the lowest rating
     * If there are multiple books with the same lowest rating, the first one found will be returned
     */
    public Book findLeastLikedBook() {
        if (readBooksRated.size() == 0) {
            return null;
        }
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(0);
    }

    /**
     * @return the average rating given to all books in the 'read'
     * If a book in the 'read' shelf does not have a rating, it is not included in the sum
     */
    public Double calculateAverageRatingGiven() {
        int numberOfRatings = readBooksRated.size();
        if (numberOfRatings == 0) {
            return null;
        }
        return (calculateTotalRating() / numberOfRatings);
    }

    private double calculateTotalRating() {
        return readBooksRated.stream()
                             .mapToDouble(book -> {
                                 Double rating = converter.convertToPresentation(book.getRating(), null);
                                 rating = (rating == null) ? 0.0 : rating;
                                 return rating;
                             })
                             .sum();
    }
}

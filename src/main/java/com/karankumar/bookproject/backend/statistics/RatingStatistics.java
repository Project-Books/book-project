package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class RatingStatistics {

    private static final DoubleToRatingScaleConverter converter = new DoubleToRatingScaleConverter();
    private final Set<Book> readShelfBooks;
    private List<Book> readBooksRated;

    public RatingStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();
        readBooksRated = findReadBooksWithRatings();
    }

    /**
     * @return the Book in the 'read' shelf with the highest rating
     * If there are multiple books with the same highest rating, the first one found will be returned
     */
    public Book findMostLikedBook() {
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
        readBooksRated.sort(Comparator.comparing(Book::getRating));
        return readBooksRated.get(0);
    }

    /**
     * @return the average rating given to all books in the 'read'
     * If a book in the 'read' shelf does not have a rating, it is not included in the sum
     */
    public double calculateAverageRatingGiven() {
        int numberOfRatings = readBooksRated.size();
        double totalRating = readBooksRated.stream()
                                           .mapToDouble(book -> converter.convertToPresentation(book.getRating(),null))
                                           .sum();
        return (totalRating / numberOfRatings);
    }
}

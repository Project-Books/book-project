package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class RatingStatistics {

    private static final DoubleToRatingScaleConverter converter = new DoubleToRatingScaleConverter();
    private final Set<Book> readShelfBooks;

    public RatingStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();
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
}

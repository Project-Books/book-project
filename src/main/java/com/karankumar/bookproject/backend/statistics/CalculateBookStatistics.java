package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import lombok.extern.java.Log;

import java.util.Set;
import java.util.logging.Level;

@Log
public class CalculateBookStatistics {
    private final Set<Book> readShelfBooks;

    public CalculateBookStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();
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
        int numberOfBooks = 0;
        for (Book book : readShelfBooks) {
            if (book.getNumberOfPages() != null) {
                totalNumberOfPages += book.getNumberOfPages();
                numberOfBooks++;
            }
        }
        LOGGER.log(Level.INFO, "Pages: " + totalNumberOfPages);
        LOGGER.log(Level.INFO, "Books: " + numberOfBooks);
        return (int) Math.ceil(totalNumberOfPages / numberOfBooks);
    }

    public void calculateMostReadGenres() {
        // TODO
    }

    public void calculateAverageRatingGiven() {
        // TODO
    }

    public void findMostLikedBook() {
        // TODO
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

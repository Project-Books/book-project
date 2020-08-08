package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PageStatistics extends Statistics {
    private final List<Book> booksWithPageCount;

    public PageStatistics(PredefinedShelfService predefinedShelfService) {
        super(predefinedShelfService);
        booksWithPageCount = findBooksWithPageCountSpecified();
    }

    /**
     * @return the Book in the 'read' shelf with the highest number of pages
     */
    public Book findBookWithMostPages() {
        Book bookWithMostPages = null;
        if (!booksWithPageCount.isEmpty()) {
            booksWithPageCount.sort(Comparator.comparing(Book::getNumberOfPages));
            bookWithMostPages = booksWithPageCount.get(booksWithPageCount.size() - 1);
        }
        return bookWithMostPages;
    }

    private List<Book> findBooksWithPageCountSpecified() {
        List<Book> booksWithPageCount = new ArrayList<>();
        for (Book book : readShelfBooks) {
            if (book.getNumberOfPages() != null) {
                booksWithPageCount.add(book);
            }
        }
        return booksWithPageCount;
    }

    /**
     * @return the average page length for all books in the 'read' shelf
     * This average only includes books that have a page length specified
     */
    public Integer calculateAveragePageLength() {
        int totalNumberOfPages = booksWithPageCount.stream()
                                                   .mapToInt(Book::getNumberOfPages)
                                                   .sum();
        int booksWithPagesSpecified = booksWithPageCount.size();
        if (booksWithPagesSpecified == 0) {
            return null;
        }
        return (booksWithPagesSpecified == 0) ? 0 : (int) Math.ceil(totalNumberOfPages / booksWithPagesSpecified);
    }
}

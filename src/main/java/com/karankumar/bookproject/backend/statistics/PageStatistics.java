package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class PageStatistics {

    private final Set<Book> readShelfBooks;
    private final List<Book> booksWithPageCount;

    public PageStatistics(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();
        booksWithPageCount = findBooksWithPageCountSpecified();
    }

    /**
     * @return the Book in the 'read' shelf with the highest number of pages
     */
    public Book findBookWithMostPages() {
        booksWithPageCount.sort(Comparator.comparing(Book::getNumberOfPages));
        return booksWithPageCount.get(booksWithPageCount.size() - 1);
    }

    private List<Book> findBooksWithPageCountSpecified() {
        List<Book> booksWithPageCount = new ArrayList<>();
        for (Book book : readShelfBooks) {
            if (book.getPagesRead() != null) {
                booksWithPageCount.add(book);
            }
        }
        return booksWithPageCount;
    }

    /**
     * @return the average page length for all books in the 'read' shelf
     * This average only includes books that have a page length specified
     */
    public int calculateAveragePageLength() {
        int totalNumberOfPages = booksWithPageCount.stream()
                                                   .mapToInt(Book::getNumberOfPages)
                                                   .sum();
        int booksWithPagesSpecified = booksWithPageCount.size();
        return (booksWithPagesSpecified == 0) ? 0 : (int) Math.ceil(totalNumberOfPages / booksWithPagesSpecified);
    }
}

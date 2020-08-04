package com.karankumar.bookproject.backend.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;

import java.util.Set;

public class PageStatistics {

    private final Set<Book> readShelfBooks;

    public PageStatistics(PredefinedShelfService predefinedShelfService) {
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
        int booksWithPagesSpecified = 0;
        for (Book book : readShelfBooks) {
            if (book.getNumberOfPages() != null) {
                totalNumberOfPages += book.getNumberOfPages();
                booksWithPagesSpecified++;
            }
        }
        return (booksWithPagesSpecified == 0) ? 0 : (int) Math.ceil(totalNumberOfPages / booksWithPagesSpecified);
    }
}

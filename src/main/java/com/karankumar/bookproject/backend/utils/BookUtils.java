package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.Book;

public class BookUtils {
    private BookUtils() {}

    public static String combineTitleAndSeries(Book book) {
        String result;
        if (book.getSeriesPosition() != null && book.getSeriesPosition() > 0) {
            result = String.format("%s (#%d)", book.getTitle(), book.getSeriesPosition());
        } else {
            result = book.getTitle();
        }
        return result;
    }
}

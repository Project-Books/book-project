package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookUtilsTest {
    private final String bookTitle = "Title";
    private final Book book = new Book(bookTitle, null, null);

    @Test
    void testSeriesPositionNotShownWhenBookIsNotInSeries() {
        book.setSeriesPosition(null);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(bookTitle, actual);
    }

    @Test
    void testTitleReturnedWhenSeriesPositionIsZero() {
        book.setSeriesPosition(0);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(bookTitle, actual);
    }

    @Test
    void testSeriesPositionCorrectlyShown() {
        book.setSeriesPosition(1);
        String expected = bookTitle + " (#1)";
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(expected, actual);
    }
}

package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookUtilsTest {
    private Book book = new Book("Title", null, null);

    @Test
    void testSeriesPositionNotShownWhenBookIsNotInSeries() {
        book.setSeriesPosition(0);
        String expected = "Title";
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(expected, actual);
    }

    @Test
    void testSeriesPositionCorrectlyShown() {
        book.setSeriesPosition(1);
        String expected = "Title (#1)";
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(expected, actual);
    }
}

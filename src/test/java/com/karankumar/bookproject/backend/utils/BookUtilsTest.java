package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BookUtils should")
class BookUtilsTest {
    private final String bookTitle = "Title";
    private final Book book = new Book(bookTitle, null, null);

    @Test
    void notAddSeriesPositionWhenBookIsNotInSeries() {
        book.setSeriesPosition(null);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(bookTitle, actual);
    }

    @Test
    void notAddSeriesPositionWhenSeriesPositionIsZero() {
        book.setSeriesPosition(0);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(bookTitle, actual);
    }

    @Test
    void addSeriesPositionWhenBookAsSeriesPosition() {
        book.setSeriesPosition(1);
        String expected = bookTitle + " (#1)";
        String actual = BookUtils.combineTitleAndSeries(book);
        assertEquals(expected, actual);
    }
}

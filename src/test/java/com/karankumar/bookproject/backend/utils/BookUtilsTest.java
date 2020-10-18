package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.book.Book;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class BookUtilsTest {
    private final String bookTitle = "Title";
    private final Book book = new Book(bookTitle, null, null);

    @Test
    void testSeriesPositionNotShownWhenBookIsNotInSeries() {
        book.setSeriesPosition(null);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertThat(actual).isEqualTo(bookTitle);
    }

    @Test
    void testTitleReturnedWhenSeriesPositionIsZero() {
        book.setSeriesPosition(0);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertThat(actual).isEqualTo(bookTitle);
    }

    @Test
    void testSeriesPositionCorrectlyShown() {
        book.setSeriesPosition(1);
        String expected = bookTitle + " (#1)";
        String actual = BookUtils.combineTitleAndSeries(book);
        assertThat(actual).isEqualTo(expected);
    }
}

package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.Book;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
@DisplayName("BookUtils should")
class BookUtilsTest {
    private final String bookTitle = "Title";
    private final Book book = new Book(bookTitle, null, null);

    @Test
    void notShowSeriesPositionIfBookIsNotInSeries() {
        book.setSeriesPosition(null);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertThat(actual).isEqualTo(bookTitle);
    }

    @Test
    void returnTitleWhenSeriesPositionIsZero() {
        book.setSeriesPosition(0);
        String actual = BookUtils.combineTitleAndSeries(book);
        assertThat(actual).isEqualTo(bookTitle);
    }

    @Test
    void showSeriesPositionCorrectly() {
        book.setSeriesPosition(1);
        String expected = bookTitle + " (#1)";
        String actual = BookUtils.combineTitleAndSeries(book);
        assertThat(actual).isEqualTo(expected);
    }
}

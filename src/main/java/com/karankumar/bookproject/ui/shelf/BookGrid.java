package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.shelf.listener.BookGridListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import lombok.extern.java.Log;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.logging.Level;

import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.*;


@Log
public class BookGrid {
    private final Grid<Book> bookGrid;

    BookGrid(Grid<Book> bookGrid) {
        this.bookGrid = bookGrid;
    }

    public void configure(BookGridListener listener) {
        addListener(listener);

        resetGridColumns();

        addTitleColumn();
        addAuthorColumn();
        bookGrid.addColumn(GENRE_KEY);
        addDateStartedColumn();
        addDateFinishedColumn();
        bookGrid.addColumn(RATING_KEY);
        bookGrid.addColumn(PAGES_KEY);
        bookGrid.addColumn(PAGES_READ_KEY);
    }

    private void addListener(BookGridListener listener) {
        bookGrid.asSingleSelect().addValueChangeListener(listener.getListener());
    }

    private void resetGridColumns() {
        bookGrid.setColumns();
    }

    private void addDateFinishedColumn() {
        bookGrid.addColumn(new LocalDateRenderer<>(
                Book::getDateFinishedReading, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Date finished reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setSortable(true)
                .setKey(DATE_FINISHED_KEY);
    }

    private void addDateStartedColumn() {
        bookGrid.addColumn(new LocalDateRenderer<>(
                Book::getDateStartedReading, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Date started reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setKey(DATE_STARTED_KEY);
    }

    private void addAuthorColumn() {
        bookGrid.addColumn(AUTHOR_KEY)
                .setComparator(Comparator.comparing(author -> author.getAuthor().toString()))
                .setSortable(true);
    }

    private void addTitleColumn() {
        bookGrid.addColumn(this::combineTitleAndSeries) // we want to display the series only if it is bigger than 0
                .setHeader("Title")
                .setKey(TITLE_KEY)
                .setSortable(true);
    }

    private String combineTitleAndSeries(Book book) {
        if (book.existsSeriesPosition()) {
            return String.format("%s (#%d)", book.getTitle(), book.getSeriesPosition());
        }

        return book.getTitle();
    }


    public void toggleColumnVisibility(String columnKey, boolean showColumn) {
        if (bookGrid.getColumnByKey(columnKey) == null) {
            LOGGER.log(Level.SEVERE, "Key is null:" + columnKey);
        } else {
            bookGrid.getColumnByKey(columnKey).setVisible(showColumn);
        }
    }

    public Grid<Book> get() {
        return bookGrid;
    }
}
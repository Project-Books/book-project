package com.karankumar.bookproject.ui.shelf.component;

import com.karankumar.bookproject.backend.entity.Book;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;

public class BookGridColumn {
    public static final String TITLE_KEY = "title";
    public static final String AUTHOR_KEY = "author";
    public static final String GENRE_KEY = "genre";
    public static final String PAGES_KEY = "numberOfPages";
    public static final String PAGES_READ_KEY = "pagesRead";
    public static final String RATING_KEY = "rating";
    public static final String DATE_STARTED_KEY = "dateStartedReading";
    public static final String DATE_FINISHED_KEY = "dateFinishedReading";

    private final Grid<Book> bookGrid;

    public BookGridColumn(Grid<Book> bookGrid) {
        this.bookGrid = bookGrid;
    }

    public void addColumns() {
        resetGridColumns();
        addTitleColumn();
        addAuthorColumn();
        addGenreColumn();
        addDateStartedColumn();
        addDateFinishedColumn();
        addRatingColumn();
        addPagesColumn();
        addPagesReadColumn();
    }

    private void resetGridColumns() {
        bookGrid.setColumns();
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

    private void addAuthorColumn() {
        bookGrid.addColumn(AUTHOR_KEY)
                .setComparator(Comparator.comparing(author -> author.getAuthor().toString()))
                .setSortable(true);
    }

    private void addGenreColumn() {
        bookGrid.addColumn(GENRE_KEY);
    }

    private void addDateStartedColumn() {
        bookGrid.addColumn(new LocalDateRenderer<>(
                Book::getDateStartedReading, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Date started reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setKey(DATE_STARTED_KEY);
    }

    private void addDateFinishedColumn() {
        bookGrid.addColumn(new LocalDateRenderer<>(Book::getDateFinishedReading, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Date finished reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setSortable(true)
                .setKey(DATE_FINISHED_KEY);
    }

    private void addRatingColumn() {
        bookGrid.addColumn(RATING_KEY);
    }

    private void addPagesColumn() {
        bookGrid.addColumn(PAGES_KEY);
    }

    private void addPagesReadColumn() {
        bookGrid.addColumn(PAGES_READ_KEY);
    }
}
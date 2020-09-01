/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.shelf.component;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.utils.BookUtils;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;

import java.time.LocalDate;
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
        // we want to display the series only if it is bigger than 0
        bookGrid.addColumn(BookUtils::combineTitleAndSeries)
                .setHeader("Title")
                .setKey(TITLE_KEY)
                .setSortable(true);
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
        bookGrid.addColumn(createLocalDateRenderer(Book::getDateStartedReading))
                .setHeader("Date started reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setKey(DATE_STARTED_KEY);
    }

    private void addDateFinishedColumn() {
        bookGrid.addColumn(createLocalDateRenderer(Book::getDateFinishedReading))
                .setHeader("Date finished reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setSortable(true)
                .setKey(DATE_FINISHED_KEY);
    }

    private LocalDateRenderer<Book> createLocalDateRenderer(ValueProvider<Book, LocalDate> provider) {
        return new LocalDateRenderer(provider, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
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

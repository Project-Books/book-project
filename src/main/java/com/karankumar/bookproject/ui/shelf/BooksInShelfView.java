/*
   The book project lets a user keep track of different books they've read, are currently reading or would like to read
   Copyright (C) 2020  Karan Kumar

   This program is free software: you can redistribute it and/or modify it under the terms of the
   GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
   warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

   You should have received a copy of the GNU General Public License along with this program.
   If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.book.BookForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Contains a @see BookForm and a grid containing a list of books in a given shelf.
 */
@Route(value = "", layout = MainView.class)
@RouteAlias(value = "myBooks", layout = MainView.class)
@PageTitle("My Books | Book Project")
@Log
public class BooksInShelfView extends VerticalLayout {
    // TODO: 3.08.2020 Encapsulate these variables. visibility should be package-access
    public static final String TITLE_KEY = "title";
    public static final String AUTHOR_KEY = "author";
    public static final String GENRE_KEY = "genre";
    public static final String PAGES_KEY = "numberOfPages";
    public static final String PAGES_READ_KEY = "pagesRead";
    public static final String RATING_KEY = "rating";
    public static final String DATE_STARTED_KEY = "dateStartedReading";
    public static final String DATE_FINISHED_KEY = "dateFinishedReading";

    public final Grid<Book> bookGrid;
    public final ComboBox<PredefinedShelf.ShelfName> whichShelf;

    private final BookForm bookForm;
    private final BookService bookService;
    private final PredefinedShelfService shelfService;
    private final TextField filterByTitle;
    private final TextField filterByAuthorName;

    private PredefinedShelf.ShelfName chosenShelf;
    private String bookTitle; // the book to filter by (if specified)
    private String authorName;

    public BooksInShelfView(BookService bookService, PredefinedShelfService shelfService) {
        this.bookService = bookService;
        this.shelfService = shelfService;

        bookGrid = new Grid<>(Book.class);

        whichShelf = new ComboBox<>();
        configureChosenShelf();

        filterByTitle = new TextField();
        filterByAuthorName = new TextField();
        configureFilters();

        bookForm = new BookForm(shelfService);

        Button addBook = new Button("Add book");
        addBook.addClickListener(e -> bookForm.addBook());
        HorizontalLayout horizontalLayout =
                new HorizontalLayout(whichShelf, filterByTitle, filterByAuthorName, addBook);
        horizontalLayout.setAlignItems(Alignment.END);

        configureBookGrid();
        add(horizontalLayout, bookGrid);

        add(bookForm);

        new BookSaveListener(bookService, this).bind(bookForm);
        new BookDeleteListener(bookService, this).bind(bookForm);
    }

    private void configureChosenShelf() {
        whichShelf.setPlaceholder("Select shelf");
        whichShelf.setItems(PredefinedShelf.ShelfName.values());
        whichShelf.setRequired(true);
        whichShelf.addValueChangeListener(
                event -> {
                    if (event.getValue() == null) {
                        LOGGER.log(Level.FINE, "No choice selected");
                    } else {
                        chosenShelf = event.getValue();
                        updateGrid();
                        try {
                            showOrHideGridColumns(chosenShelf);
                        } catch (NotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @throws NotSupportedException if a shelf is not supported.
     */
    // TODO: 3.08.2020 Burayı strategy pattern ile çözelim
    void showOrHideGridColumns(PredefinedShelf.ShelfName shelfName) throws NotSupportedException {
        BookGrid bookGrid = new BookGrid(this.bookGrid);

        switch (shelfName) {
            case TO_READ:
                bookGrid.toggleColumnVisibility(RATING_KEY, false);
                bookGrid.toggleColumnVisibility(DATE_STARTED_KEY, false);
                bookGrid.toggleColumnVisibility(DATE_FINISHED_KEY, false);
                bookGrid.toggleColumnVisibility(PAGES_READ_KEY, false);
                return;
            case READING:
                bookGrid.toggleColumnVisibility(RATING_KEY, false);
                bookGrid.toggleColumnVisibility(DATE_STARTED_KEY, true);
                bookGrid.toggleColumnVisibility(DATE_FINISHED_KEY, false);
                bookGrid.toggleColumnVisibility(PAGES_READ_KEY, false);
                return;
            case DID_NOT_FINISH:
                bookGrid.toggleColumnVisibility(RATING_KEY, false);
                bookGrid.toggleColumnVisibility(DATE_STARTED_KEY, true);
                bookGrid.toggleColumnVisibility(DATE_FINISHED_KEY, false);
                bookGrid.toggleColumnVisibility(PAGES_READ_KEY, true);
                return;
            case READ:
                bookGrid.toggleColumnVisibility(RATING_KEY, true);
                bookGrid.toggleColumnVisibility(DATE_STARTED_KEY, true);
                bookGrid.toggleColumnVisibility(DATE_FINISHED_KEY, true);
                bookGrid.toggleColumnVisibility(PAGES_READ_KEY, false);
                return;
        }
        throw new NotSupportedException("Shelf " + shelfName + " has not been added as a case in switch statement.");
    }

    private void configureBookGrid() {
        new BookGrid(this.bookGrid).configure(new BookGridListener(bookForm));
    }

    public void updateGrid() {
        if (chosenShelf == null) {
            LOGGER.log(Level.FINEST, "Chosen shelf is null");
            return;
        }

        // Find the shelf that matches the chosen shelf's name
        List<PredefinedShelf> matchingShelves = shelfService.findAll(chosenShelf);

        if (!matchingShelves.isEmpty()) {
            if (matchingShelves.size() == 1) {// TODO: 3.08.2020 what the fucking is going on here !?
                LOGGER.log(Level.INFO, "Found 1 shelf: " + matchingShelves.get(0));
                PredefinedShelf selectedShelf = matchingShelves.get(0);
                Predicate<Book> caseInsensitiveBookTitleFilter = book -> bookTitle == null
                        || book.getTitle().toLowerCase().contains(bookTitle.toLowerCase());
                Predicate<Book> caseInsensitiveAuthorFilter =
                        authorNameFilter ->
                                authorName == null
                                        || authorNameFilter
                                        .getAuthor()
                                        .toString()
                                        .toLowerCase()
                                        .contains(authorName.toLowerCase());
                bookGrid.setItems(
                        selectedShelf.getBooks().stream()
                                .filter(caseInsensitiveBookTitleFilter)
                                .filter(caseInsensitiveAuthorFilter)
                                .collect(Collectors.toList()));
            } else {
                LOGGER.log(
                        Level.SEVERE, matchingShelves.size() + " matching shelves found for " + chosenShelf);
            }
        } else {
            LOGGER.log(Level.SEVERE, "No matching shelves found for " + chosenShelf);
        }
    }

    private void configureFilters() {
        filterByAuthorName();
        filterByBookTitle();
    }

    private void filterByAuthorName() {
        filterByAuthorName.setPlaceholder("Filter by Author Name");
        filterByAuthorName.setClearButtonVisible(true);
        filterByAuthorName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByAuthorName.addValueChangeListener(eventFilterAuthorName -> {
            if (eventFilterAuthorName.getValue() != null) {
                authorName = eventFilterAuthorName.getValue();
            }
            updateGrid();
        });
    }

    private void filterByBookTitle() {
        filterByTitle.setPlaceholder("Filter by book title");
        filterByTitle.setClearButtonVisible(true);
        filterByTitle.setValueChangeMode(ValueChangeMode.LAZY);
        filterByTitle.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                bookTitle = event.getValue();
            }
            updateGrid();
        });
    }
}

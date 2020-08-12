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

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.backend.utils.ShelfUtils;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.book.BookForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public final Grid<Book> bookGrid;
    public final ComboBox<String> whichShelf;

    private final BookForm bookForm;

    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;

    private final TextField filterByTitleField;
    private final TextField filterByAuthorNameField;

    private String chosenShelf;

    private String bookTitleToFilterBy;
    private String authorNameToFilterBy;

    private final PredefinedShelfUtils predefinedShelfUtils;

    public BooksInShelfView(BookService bookService, PredefinedShelfService predefinedShelfService,
                            CustomShelfService customShelfService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;
        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);

        bookGrid = new Grid<>(Book.class);

        whichShelf = new ComboBox<>();
        configureChosenShelf();

        filterByTitleField = new TextField();
        filterByAuthorNameField = new TextField();
        configureFilters();

        bookForm = createBookForm();
        Button addBook = new Button("Add book");
        addBook.addClickListener(e -> bookForm.addBook());

        CustomShelfForm customShelfForm = createCustomShelfForm();
        Button addShelf = new Button("Add shelf");
        addShelf.addClickListener(e -> customShelfForm.addShelf());

        HorizontalLayout horizontalLayout =
                new HorizontalLayout(whichShelf, filterByTitleField, filterByAuthorNameField, addShelf, addBook);
        horizontalLayout.setAlignItems(Alignment.END);

        configureBookGrid();
        add(horizontalLayout, bookGrid);

        add(customShelfForm);
        add(bookForm);
    }

    private BookForm createBookForm() {
        BookForm bookForm = new BookForm(predefinedShelfService, customShelfService);
        bookForm.addListener(BookForm.SaveEvent.class, this::saveBook);
        bookForm.addListener(BookForm.DeleteEvent.class, this::deleteBook);
        return bookForm;
    }

    private CustomShelfForm createCustomShelfForm() {
        CustomShelfForm customShelfForm = new CustomShelfForm(customShelfService, predefinedShelfService);
        customShelfForm.addListener(CustomShelfForm.SaveEvent.class, this::saveCustomShelf);
        return customShelfForm;
    }

    private void configureChosenShelf() {
        whichShelf.setPlaceholder("Select shelf");
        whichShelf.setItems(ShelfUtils.findAllShelfNames(customShelfService.findAll()));
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

    @VisibleForTesting
    void showOrHideGridColumns(String shelfName) throws NotSupportedException {
        PredefinedShelf.ShelfName predefinedShelfName;
        if (PredefinedShelfUtils.isPredefinedShelf(shelfName)) {
            predefinedShelfName = predefinedShelfUtils.getPredefinedShelfName(shelfName);
        } else {
            return;
        }
        switch (predefinedShelfName) {
            case TO_READ:
                toggleColumnVisibility(BookGridKeys.RATING_KEY, false);
                toggleColumnVisibility(BookGridKeys.DATE_STARTED_KEY, false);
                toggleColumnVisibility(BookGridKeys.DATE_FINISHED_KEY, false);
                toggleColumnVisibility(BookGridKeys.PAGES_READ_KEY, false);
                return;
            case READING:
                toggleColumnVisibility(BookGridKeys.RATING_KEY, false);
                toggleColumnVisibility(BookGridKeys.DATE_STARTED_KEY, true);
                toggleColumnVisibility(BookGridKeys.DATE_FINISHED_KEY, false);
                toggleColumnVisibility(BookGridKeys.PAGES_READ_KEY, false);
                return;
            case DID_NOT_FINISH:
                toggleColumnVisibility(BookGridKeys.RATING_KEY, false);
                toggleColumnVisibility(BookGridKeys.DATE_STARTED_KEY, true);
                toggleColumnVisibility(BookGridKeys.DATE_FINISHED_KEY, false);
                toggleColumnVisibility(BookGridKeys.PAGES_READ_KEY, true);
                return;
            case READ:
                toggleColumnVisibility(BookGridKeys.RATING_KEY, true);
                toggleColumnVisibility(BookGridKeys.DATE_STARTED_KEY, true);
                toggleColumnVisibility(BookGridKeys.DATE_FINISHED_KEY, true);
                toggleColumnVisibility(BookGridKeys.PAGES_READ_KEY, false);
                return;
            default:
                break;
        }
        throw new NotSupportedException("Shelf " + shelfName + " has not been added as a case in switch statement.");
    }

    private void toggleColumnVisibility(String columnKey, boolean showColumn) {
        if (bookGrid.getColumnByKey(columnKey) == null) {
            LOGGER.log(Level.SEVERE, "Key is null:" + columnKey);
        } else {
            bookGrid.getColumnByKey(columnKey).setVisible(showColumn);
        }
    }

    private String combineTitleAndSeries(Book book) {
        String result;
        if (book.getSeriesPosition() != null && book.getSeriesPosition() > 0) {
            result = String.format("%s (#%d)", book.getTitle(), book.getSeriesPosition());
        } else {
            result = book.getTitle();
        }
        return result;
    }

    private void configureBookGrid() {
        bookGrid.asSingleSelect()
                .addValueChangeListener(event -> {
                    if (event.getValue() != null) {
                        editBook(event.getValue());
                    }
                });

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

    private void addPagesColumn() {
        bookGrid.addColumn(BookGridKeys.PAGES_KEY);
    }

    private void addPagesReadColumn() {
        bookGrid.addColumn(BookGridKeys.PAGES_READ_KEY);
    }

    private void addRatingColumn() {
        bookGrid.addColumn(BookGridKeys.RATING_KEY);
    }

    private void addDateFinishedColumn() {
        bookGrid.addColumn(new LocalDateRenderer<>(
                Book::getDateFinishedReading, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Date finished reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setSortable(true)
                .setKey(BookGridKeys.DATE_FINISHED_KEY);
    }

    private void addDateStartedColumn() {
        bookGrid.addColumn(new LocalDateRenderer<>(
                Book::getDateStartedReading, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                .setHeader("Date started reading")
                .setComparator(Comparator.comparing(Book::getDateStartedReading))
                .setKey(BookGridKeys.DATE_STARTED_KEY);
    }

    private void addGenreColumn() {
        bookGrid.addColumn(BookGridKeys.GENRE_KEY);
    }

    private void addAuthorColumn() {
        bookGrid.addColumn(BookGridKeys.AUTHOR_KEY)
                .setComparator(Comparator.comparing(author -> author.getAuthor().toString()))
                .setSortable(true);
    }

    private void addTitleColumn() {
        bookGrid.addColumn(this::combineTitleAndSeries) // we want to display the series only if it is bigger than 0
                .setHeader("Title")
                .setKey(BookGridKeys.TITLE_KEY)
                .setSortable(true);
    }

    private void resetGridColumns() {
        bookGrid.setColumns();
    }

    private void updateGrid() {
        if (chosenShelf == null) {
            LOGGER.log(Level.FINEST, "Chosen shelf is null");
            return;
        }

        Set<Book> books;
        if (PredefinedShelfUtils.isPredefinedShelf(chosenShelf)) {
            books = getBooksInChosenPredefinedShelf();
        } else {
            books = getBooksInChosenCustomShelf();
        }
        populateGridWithBooks(books);
    }

    private Set<Book> getBooksInChosenPredefinedShelf() {
        PredefinedShelf.ShelfName predefinedShelfName = predefinedShelfUtils.getPredefinedShelfName(chosenShelf);
        Set<Book> books;
        List<PredefinedShelf> predefinedShelves = predefinedShelfService.findAll(predefinedShelfName);
        if (predefinedShelves.isEmpty()) {
            books = new HashSet<>();
        } else {
            PredefinedShelf customShelf = predefinedShelves.get(0);
            books = customShelf.getBooks();
        }
        return books;
    }

    private Set<Book> getBooksInChosenCustomShelf() {
        Set<Book> books;
        List<CustomShelf> customShelves = customShelfService.findAll(chosenShelf);
        if (customShelves.isEmpty()) {
            books = new HashSet<>();
        } else {
            CustomShelf customShelf = customShelves.get(0);
            books = customShelf.getBooks();
        }
        return books;
    }

    private void populateGridWithBooks(Set<Book> books) {
        bookGrid.setItems(books.stream()
                               .filter(caseInsensitiveBookTitleFilter())
                               .filter(caseInsensitiveAuthorFilter())
                               .collect(Collectors.toList()));
    }

    private Predicate<Book> caseInsensitiveBookTitleFilter() {
        return book -> bookTitleToFilterBy == null
                || book.getTitle().toLowerCase().contains(bookTitleToFilterBy.toLowerCase());
    }

    private Predicate<Book> caseInsensitiveAuthorFilter() {
        return authorNameFilter -> authorNameToFilterBy == null ||
                authorNameFilter.getAuthor()
                                .toString()
                                .toLowerCase()
                                .contains(authorNameToFilterBy.toLowerCase());
    }

    private void configureFilters() {
        filterByAuthorName();
        filterByBookTitle();
    }

    private void filterByAuthorName() {
        filterByAuthorNameField.setPlaceholder("Filter by Author Name");
        filterByAuthorNameField.setClearButtonVisible(true);
        filterByAuthorNameField.setValueChangeMode(ValueChangeMode.LAZY);
        filterByAuthorNameField.addValueChangeListener(eventFilterAuthorName -> {
            if (eventFilterAuthorName.getValue() != null) {
                authorNameToFilterBy = eventFilterAuthorName.getValue();
            }
            updateGrid();
        });
    }

    private void filterByBookTitle() {
        filterByTitleField.setPlaceholder("Filter by book title");
        filterByTitleField.setClearButtonVisible(true);
        filterByTitleField.setValueChangeMode(ValueChangeMode.LAZY);
        filterByTitleField.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                bookTitleToFilterBy = event.getValue();
            }
            updateGrid();
        });
    }

    private void editBook(Book book) {
        if (book != null && bookForm != null) {
            bookForm.setBook(book);
            bookForm.openForm();
        }
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        LOGGER.log(Level.INFO, "Deleting book...");
        bookService.delete(event.getBook());
        updateGrid();
    }

    private void saveBook(BookForm.SaveEvent event) {
        LOGGER.log(Level.INFO, "Saving book...");
        if (event.getBook() == null) {
            LOGGER.log(Level.SEVERE, "Retrieved book from event is null");
        } else {
            LOGGER.log(Level.INFO, "Book is not null");
            bookService.save(event.getBook());
            updateGrid();
        }
    }

    private void saveCustomShelf(CustomShelfForm.SaveEvent event) {
        if (event.getCustomShelf() != null) {
            customShelfService.save(event.getCustomShelf());
            LOGGER.log(Level.INFO, "Custom shelf saved");
        } else {
            LOGGER.log(Level.SEVERE, "Custom shelf value is null");
        }
    }
}

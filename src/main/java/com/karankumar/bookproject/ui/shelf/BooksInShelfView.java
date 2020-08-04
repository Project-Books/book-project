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
import com.karankumar.bookproject.ui.shelf.listener.*;
import com.karankumar.bookproject.ui.shelf.visibility.*;
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
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

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

    private final EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> visibilityStrategies;

    private final BookForm bookForm;
    private final BookService bookService;
    private final PredefinedShelfService shelfService;
    private final TextField filterByTitle;
    private final TextField filterByAuthorName;

    private PredefinedShelf.ShelfName chosenShelf;
    private String bookTitle; // the book to filter by (if specified)
    private String authorName;
    private final BookFilters bookFilters;


    public BooksInShelfView(BookService bookService, PredefinedShelfService shelfService) {
        this.bookService = bookService;
        this.shelfService = shelfService;
        this.visibilityStrategies = initVisibilityStrategies();
        this.bookGrid = new Grid<>(Book.class);
        this.bookFilters = new BookFilters();

        this.whichShelf = initializeChosenShelf();
        this.filterByTitle = initializeFilterByTitle();
        this.filterByAuthorName = initializeFilterByAuthorName();
        HorizontalLayout layout = initializeLayout(whichShelf, filterByTitle, filterByAuthorName);
        add(layout, bookGrid);

        bookForm = new BookForm(shelfService);

        configureBookGrid();

        add(bookForm);

        bindListeners(bookService);
    }

    private void bindListeners(BookService bookService) {
        new BookSaveListener(bookService, this).bind(bookForm);
        new BookDeleteListener(bookService, this).bind(bookForm);
        new BookShelfListener(this).bind(whichShelf);
        new BookFilterListener(this).bind(filterByTitle, filterByAuthorName);
    }

    private EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> initVisibilityStrategies() {
        EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> m = new EnumMap<>(PredefinedShelf.ShelfName.class);
        m.put(TO_READ, new ToReadBookVisibility());
        m.put(READING, new ReadingBookVisibility());
        m.put(DID_NOT_FINISH, new DidntFinishBookVisibility());
        m.put(READ, new ReadBookVisibility());

        return m;
    }

    private TextField initializeFilterByAuthorName() {
        TextField filterByAuthorName = new TextField();

        filterByAuthorName.setPlaceholder("Filter by Author Name");
        filterByAuthorName.setClearButtonVisible(true);
        filterByAuthorName.setValueChangeMode(ValueChangeMode.LAZY);

        return filterByAuthorName;
    }

    private TextField initializeFilterByTitle() {
        TextField filterByTitle = new TextField();

        filterByTitle.setPlaceholder("Filter by book title");
        filterByTitle.setClearButtonVisible(true);
        filterByTitle.setValueChangeMode(ValueChangeMode.LAZY);

        return filterByTitle;
    }

    private HorizontalLayout initializeLayout(ComboBox<PredefinedShelf.ShelfName> whichShelf, TextField filterByTitle, TextField filterByAuthorName) {
        Button addBook = new Button("Add book");
        addBook.addClickListener(e -> bookForm.addBook());

        HorizontalLayout horizontalLayout = new HorizontalLayout(whichShelf, filterByTitle, filterByAuthorName, addBook);
        horizontalLayout.setAlignItems(Alignment.END);

        return horizontalLayout;
    }

    private ComboBox<PredefinedShelf.ShelfName> initializeChosenShelf() {
        ComboBox<PredefinedShelf.ShelfName> whichShelf = new ComboBox<>();

        whichShelf.setPlaceholder("Select shelf");
        whichShelf.setItems(PredefinedShelf.ShelfName.values());
        whichShelf.setRequired(true);

        return whichShelf;
    }

    /**
     * @throws NotSupportedException if a shelf is not supported.
     */
    // TODO: 3.08.2020 this should be moved BookShelfListener. But it's also invoked in the test.
    public void showOrHideGridColumns(PredefinedShelf.ShelfName shelfName) throws NotSupportedException {
        BookGrid bookGrid = new BookGrid(this.bookGrid);

        if (visibilityStrategies.containsKey(shelfName)) {
            throw new NotSupportedException("Shelf " + shelfName + " has not been added as a case in switch statement.");
        }

        visibilityStrategies.get(shelfName).toggleColumnVisibility(bookGrid);
    }

    private void configureBookGrid() {
        new BookGrid(this.bookGrid).configure(new BookGridListener(bookForm));
    }

    public void updateGrid() {
        new BookGrid(this.bookGrid).update(chosenShelf, shelfService, bookFilters);
    }

    public void chooseShelf(PredefinedShelf.ShelfName chosenShelf) {
        this.chosenShelf = chosenShelf;
    }

    public void setBookFilterAuthor(String author) {
        bookFilters.setBookAuthor(author);
    }

    public void setBookFilterTitle(String title) {
        bookFilters.setBookTitle(title);
    }
}

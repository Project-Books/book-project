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

package com.karankumar.bookproject.ui.shelf;

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.book.form.BookForm;
import com.karankumar.bookproject.ui.shelf.component.filter.AuthorFilterText;
import com.karankumar.bookproject.ui.shelf.component.BookShelfComboBox;
import com.karankumar.bookproject.ui.shelf.component.filter.TitleFilterText;
import com.karankumar.bookproject.ui.shelf.listener.BookDeleteListener;
import com.karankumar.bookproject.ui.shelf.listener.BookSaveListener;
import com.karankumar.bookproject.ui.shelf.listener.CustomShelfListener;
import com.karankumar.bookproject.ui.shelf.visibility.BookVisibilityStrategy;
import com.karankumar.bookproject.ui.shelf.visibility.DidNotFinishBookVisibility;
import com.karankumar.bookproject.ui.shelf.visibility.ReadBookVisibility;
import com.karankumar.bookproject.ui.shelf.visibility.ReadingBookVisibility;
import com.karankumar.bookproject.ui.shelf.visibility.ToReadBookVisibility;
import com.karankumar.bookproject.ui.shelf.visibility.AllShelvesBookVisibility;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.HashMap;
import java.util.List;

import static com.karankumar.bookproject.backend.util.ShelfUtils.ALL_BOOKS_SHELF;
import static com.karankumar.bookproject.backend.util.ShelfUtils.isAllBooksShelf;

/**
 * Contains a @see BookForm and a grid containing a list of books in a given shelf.
 */
@Route(value = "", layout = MainView.class)
@RouteAlias(value = "myBooks", layout = MainView.class)
@PageTitle("My Books | Book Project")
@Log
public class BooksInShelfView extends VerticalLayout {
    public final BookGrid bookGrid;
    public final BookShelfComboBox whichShelf;

    private final HashMap<String, BookVisibilityStrategy> visibilityStrategies;

    private final BookForm bookForm;
    private final CustomShelfForm customShelfForm;

    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;
    private final TitleFilterText filterByTitle;
    private final AuthorFilterText filterByAuthorName;

    private String chosenShelf;
    private final BookFilters bookFilters;

    public BooksInShelfView(BookService bookService, PredefinedShelfService predefinedShelfService,
                            CustomShelfService customShelfService) {
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;
        this.visibilityStrategies = initVisibilityStrategies();
        this.bookGrid = new BookGrid(customShelfService, predefinedShelfService, bookService);
        this.bookFilters = new BookFilters();

        this.whichShelf = new BookShelfComboBox(customShelfService);
        this.filterByTitle = new TitleFilterText();
        this.filterByAuthorName = new AuthorFilterText();

        whichShelf.bind(this);
        filterByTitle.bind(this);
        filterByAuthorName.bind(this);

        bookForm = new BookForm(predefinedShelfService, customShelfService);

        bookGrid.bind(bookForm);
        bindListeners(bookService);

        customShelfForm = createCustomShelfForm();

        add(initializeLayout(), bookGrid.get(), customShelfForm, bookForm);
    }

    public void updateWhichShelfList() {
        whichShelf.updateShelfList();
    }

    private HashMap<String, BookVisibilityStrategy> initVisibilityStrategies() {
        HashMap<String, BookVisibilityStrategy> m = new HashMap<>();
        m.put(PredefinedShelf.ShelfName.TO_READ.toString(), new ToReadBookVisibility());
        m.put(PredefinedShelf.ShelfName.READING.toString(), new ReadingBookVisibility());
        m.put(PredefinedShelf.ShelfName.DID_NOT_FINISH.toString(), new DidNotFinishBookVisibility());
        m.put(PredefinedShelf.ShelfName.READ.toString(), new ReadBookVisibility());
        m.put(ALL_BOOKS_SHELF, new AllShelvesBookVisibility());

        return m;
    }

    private void bindListeners(BookService bookService) {
        new BookSaveListener(this, bookService).bind(bookForm);
        new BookDeleteListener(bookService, this).bind(bookForm);
    }

    private CustomShelfForm createCustomShelfForm() {
        CustomShelfForm customShelfForm =
                new CustomShelfForm(predefinedShelfService, customShelfService);
        new CustomShelfListener(this, customShelfService).bind(customShelfForm);
        return customShelfForm;
    }

    private HorizontalLayout initializeLayout() {
        Button addBook = new Button("Add book");
        addBook.addClickListener(e -> bookForm.addBook());

        Button addShelf = new Button("Add shelf");
        addShelf.addClickListener(e -> customShelfForm.addShelf());

        HorizontalLayout layout = new HorizontalLayout(addBook);

        whichShelf.addToLayout(layout);
        filterByTitle.addToLayout(layout);
        filterByAuthorName.addToLayout(layout);
        layout.add(addShelf);
        layout.add(addBook);

        layout.setAlignItems(Alignment.END);

        return layout;
    }

    // TODO: 3.08.2020 this should be moved BookShelfListener. But it's also invoked in the test.
    @VisibleForTesting
    public void showOrHideGridColumns(String shelfName) throws NotSupportedException {
        if (isAllBooksShelf(shelfName)) {
            visibilityStrategies.get(ALL_BOOKS_SHELF).toggleColumnVisibility(bookGrid);
        }

        if (!visibilityStrategies.containsKey(shelfName)) {
            throw new NotSupportedException("Shelf " + shelfName +
                    " has not been added as a case in switch statement.");
        }

        visibilityStrategies.get(shelfName).toggleColumnVisibility(bookGrid);
    }

    public void updateGrid() {
        bookGrid.update(chosenShelf, bookFilters);
    }

    public void setChosenShelf(String chosenShelf) {
        this.chosenShelf = chosenShelf;
    }

    public void setBookFilterAuthor(String author) {
        bookFilters.setBookAuthor(author);
    }

    public void setBookFilterTitle(String title) {
        bookFilters.setBookTitle(title);
    }

    @VisibleForTesting
    List<Grid.Column<Book>> getColumns() {
        return bookGrid.get()
                       .getColumns();
    }
}

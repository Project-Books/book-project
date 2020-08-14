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
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.CustomShelfUtils;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.book.BookForm;
import com.karankumar.bookproject.ui.shelf.component.AuthorFilterText;
import com.karankumar.bookproject.ui.shelf.component.BookShelfComboBox;
import com.karankumar.bookproject.ui.shelf.component.TitleFilterText;
import com.karankumar.bookproject.ui.shelf.listener.BookDeleteListener;
import com.karankumar.bookproject.ui.shelf.listener.BookSaveListener;
import com.karankumar.bookproject.ui.shelf.listener.CustomShelfListener;
import com.karankumar.bookproject.ui.shelf.visibility.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.EnumMap;
import java.util.List;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

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

    private final EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> visibilityStrategies;

    private final BookForm bookForm;
    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;
    private final TitleFilterText filterByTitle;
    private final AuthorFilterText filterByAuthorName;

    private String chosenShelf;
    private final BookFilters bookFilters;

    private final PredefinedShelfUtils predefinedShelfUtils;
    private final CustomShelfUtils customShelfUtils;

    public BooksInShelfView(BookService bookService, PredefinedShelfService predefinedShelfService,
                            CustomShelfService customShelfService) {
        this.predefinedShelfService = predefinedShelfService;
        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        this.customShelfService = customShelfService;
        customShelfUtils = new CustomShelfUtils(customShelfService);
        this.visibilityStrategies = initVisibilityStrategies();
        this.bookGrid = new BookGrid(predefinedShelfUtils, customShelfUtils);
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

        CustomShelfForm customShelfForm = createCustomShelfForm();

        add(initializeLayout(), bookGrid.get(), customShelfForm, bookForm);
    }

    private EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> initVisibilityStrategies() {
        EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> m = new EnumMap<>(PredefinedShelf.ShelfName.class);
        m.put(TO_READ, new ToReadBookVisibility());
        m.put(READING, new ReadingBookVisibility());
        m.put(DID_NOT_FINISH, new DidntFinishBookVisibility());
        m.put(READ, new ReadBookVisibility());

        return m;
    }

    private void bindListeners(BookService bookService) {
        new BookSaveListener(bookService, this).bind(bookForm);
        new BookDeleteListener(bookService, this).bind(bookForm);
    }

    private CustomShelfForm createCustomShelfForm() {
        CustomShelfForm customShelfForm = new CustomShelfForm(customShelfService, predefinedShelfService);
        new CustomShelfListener(customShelfService).bind(customShelfForm);
        return customShelfForm;
    }

    private HorizontalLayout initializeLayout() {
        Button addBook = new Button("Add book");
        addBook.addClickListener(e -> bookForm.addBook());

        CustomShelfForm customShelfForm = createCustomShelfForm();
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
        if (!PredefinedShelfUtils.isPredefinedShelf(shelfName)) {
            return;
        }

        PredefinedShelf.ShelfName predefinedShelfName = predefinedShelfUtils.getPredefinedShelfName(shelfName);

        if (!visibilityStrategies.containsKey(predefinedShelfName)) {
            throw new NotSupportedException("Shelf " + shelfName + " has not been added as a case in switch statement.");
        }

        visibilityStrategies.get(predefinedShelfName)
                            .toggleColumnVisibility(bookGrid);
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

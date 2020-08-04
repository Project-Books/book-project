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

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.book.BookForm;
import com.karankumar.bookproject.ui.shelf.component.AuthorFilterText;
import com.karankumar.bookproject.ui.shelf.component.BookShelfComboBox;
import com.karankumar.bookproject.ui.shelf.component.TitleFilterText;
import com.karankumar.bookproject.ui.shelf.listener.BookDeleteListener;
import com.karankumar.bookproject.ui.shelf.listener.BookSaveListener;
import com.karankumar.bookproject.ui.shelf.visibility.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.EnumMap;

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
    private final PredefinedShelfService shelfService;
    private final TitleFilterText filterByTitle;
    private final AuthorFilterText filterByAuthorName;

    private PredefinedShelf.ShelfName chosenShelf;
    private final BookFilters bookFilters;


    public BooksInShelfView(BookService bookService, PredefinedShelfService shelfService) {
        this.shelfService = shelfService;
        this.visibilityStrategies = initVisibilityStrategies();
        this.bookGrid = new BookGrid();
        this.bookFilters = new BookFilters();

        this.whichShelf = new BookShelfComboBox();
        this.filterByTitle = new TitleFilterText();
        this.filterByAuthorName = new AuthorFilterText();

        filterByTitle.bind(this);
        filterByAuthorName.bind(this);

        add(initializeLayout(), bookGrid.get());

        bookForm = new BookForm(shelfService);

        bookGrid.bind(bookForm);

        add(bookForm);

        bindListeners(bookService);
    }

    private void bindListeners(BookService bookService) {
        new BookSaveListener(bookService, this).bind(bookForm);
        new BookDeleteListener(bookService, this).bind(bookForm);
    }

    private EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> initVisibilityStrategies() {
        EnumMap<PredefinedShelf.ShelfName, BookVisibilityStrategy> m = new EnumMap<>(PredefinedShelf.ShelfName.class);
        m.put(TO_READ, new ToReadBookVisibility());
        m.put(READING, new ReadingBookVisibility());
        m.put(DID_NOT_FINISH, new DidntFinishBookVisibility());
        m.put(READ, new ReadBookVisibility());

        return m;
    }

    private HorizontalLayout initializeLayout() {
        Button addBook = new Button("Add book");
        addBook.addClickListener(e -> bookForm.addBook());

        HorizontalLayout layout = new HorizontalLayout(addBook);

        filterByTitle.addToLayout(layout);
        filterByAuthorName.addToLayout(layout);
        whichShelf.addToLayout(layout);

        layout.setAlignItems(Alignment.END);

        return layout;
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
        if (visibilityStrategies.containsKey(shelfName)) {
            throw new NotSupportedException("Shelf " + shelfName + " has not been added as a case in switch statement.");
        }

        visibilityStrategies.get(shelfName).toggleColumnVisibility(bookGrid);
    }

    public void updateGrid() {
        bookGrid.update(chosenShelf, shelfService, bookFilters);
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

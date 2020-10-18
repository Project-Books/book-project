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

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.util.CustomShelfUtils;
import com.karankumar.bookproject.backend.util.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.BookForm;
import com.karankumar.bookproject.ui.shelf.component.BookGridColumn;
import com.vaadin.flow.component.grid.Grid;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.karankumar.bookproject.backend.util.ShelfUtils.isAllBooksShelf;

@Log
public class BookGrid {
    private final Grid<Book> bookGrid;
    
    private final PredefinedShelfUtils predefinedShelfUtils;
    private final CustomShelfUtils customShelfUtils;

    BookGrid(PredefinedShelfUtils predefinedShelfUtils, CustomShelfUtils customShelfUtils) {
        this.bookGrid = new Grid<>(Book.class);
        this.predefinedShelfUtils = predefinedShelfUtils;
        this.customShelfUtils = customShelfUtils;
        configure();
    }

    public void configure() {
        bookGrid.setHeightByRows(true);
        new BookGridColumn(bookGrid).addColumns();
    }

    void bind(BookForm bookForm) {
        bookGrid.asSingleSelect().addValueChangeListener(event -> {
            Book book = event.getValue();

            if (book != null && bookForm != null) {
                bookForm.setBook(book);
                bookForm.openForm();
            }
        });
    }

    public void toggleColumnVisibility(String columnKey, boolean showColumn) {
        if (bookGrid.getColumnByKey(columnKey) == null) {
            LOGGER.log(Level.SEVERE, "Key is null: " + columnKey);
        } else {
            bookGrid.getColumnByKey(columnKey)
                    .setVisible(showColumn);
        }
    }

    public Grid<Book> get() {
        return bookGrid;
    }

    void update(String chosenShelf, BookFilters bookFilters) {
        if (chosenShelf == null) {
            LOGGER.log(Level.FINEST, "Chosen shelf is null");
            return;
        }

        Set<Book> books = getBooks(chosenShelf);
        populateGridWithBooks(books, bookFilters);
    }

    private Set<Book> getBooks(String chosenShelf) {
        if (isAllBooksShelf(chosenShelf)) {
            return predefinedShelfUtils.getBooksInAllPredefinedShelves();
        }

        if (PredefinedShelfUtils.isPredefinedShelf(chosenShelf)) {
            return predefinedShelfUtils.getBooksInChosenPredefinedShelf(chosenShelf);
        }

        return customShelfUtils.getBooksInCustomShelf(chosenShelf);
    }

    private void populateGridWithBooks(Set<Book> books, BookFilters bookFilters) {
        List<Book> items = filterShelf(books, bookFilters);
        bookGrid.setItems(items);
    }

    private List<Book> filterShelf(Set<Book> books, BookFilters bookFilters) {
        return books.stream()
                    .filter(bookFilters::apply)
                    .collect(Collectors.toList());
    }
}

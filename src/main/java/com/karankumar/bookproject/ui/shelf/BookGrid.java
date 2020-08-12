package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.utils.CustomShelfUtils;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.BookForm;
import com.karankumar.bookproject.ui.shelf.component.BookGridColumn;
import com.vaadin.flow.component.grid.Grid;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;


@Log
public class BookGrid {
    private final Grid<Book> bookGrid;

    BookGrid() {
        this.bookGrid = new Grid<>(Book.class);
        configure();
    }

    public void configure() {
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
            LOGGER.log(Level.SEVERE, "Key is null:" + columnKey);
        } else {
            bookGrid.getColumnByKey(columnKey).setVisible(showColumn);
        }
    }

    public Grid<Book> get() {
        return bookGrid;
    }

    void update(String chosenShelf, CustomShelfUtils customShelfUtils, BookFilters bookFilters, PredefinedShelfUtils predefinedShelfUtils) {
        if (chosenShelf == null) {
            LOGGER.log(Level.FINEST, "Chosen shelf is null");
            return;
        }

        Set<Book> books = getBooks(chosenShelf, customShelfUtils, predefinedShelfUtils);

        populateGridWithBooks(books, bookFilters);
    }

    private Set<Book> getBooks(String chosenShelf, CustomShelfUtils customShelfUtils, PredefinedShelfUtils predefinedShelfUtils) {
        if (PredefinedShelfUtils.isPredefinedShelf(chosenShelf)) {
            return predefinedShelfUtils.getBooksInChosenPredefinedShelf(chosenShelf);
        }

        return customShelfUtils.getBooksInChosenCustomShelf(chosenShelf);
    }

    private void populateGridWithBooks(Set<Book> books, BookFilters bookFilters) {
        List<Book> items = filterShelf(books, bookFilters);
        bookGrid.setItems(items);
    }

    private List<Book> filterShelf(Set<Book> books, BookFilters bookFilters) {
        return books.stream()
                .filter(book -> bookFilters.apply(book))
                .collect(Collectors.toList());
    }
}

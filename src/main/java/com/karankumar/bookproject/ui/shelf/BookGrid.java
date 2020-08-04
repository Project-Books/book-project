package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.book.BookForm;
import com.karankumar.bookproject.ui.shelf.component.BookGridColumn;
import com.vaadin.flow.component.grid.Grid;
import lombok.extern.java.Log;

import java.util.List;
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

    void update(PredefinedShelf.ShelfName chosenShelf, PredefinedShelfService shelfService, BookFilters bookFilters) {
        if (chosenShelf == null) {
            LOGGER.log(Level.FINEST, "Chosen shelf is null");
            return;
        }

        // Find the shelf that matches the chosen shelf's name
        List<PredefinedShelf> matchingShelves = shelfService.findAll(chosenShelf);

        if (matchingShelves.isEmpty()) {
            LOGGER.log(Level.SEVERE, "No matching shelves found for " + chosenShelf);
            return;
        }

        if (matchingShelves.size() != 1) {
            LOGGER.log(Level.SEVERE, matchingShelves.size() + " matching shelves found for " + chosenShelf);
            return;
        }

        LOGGER.log(Level.INFO, "Found 1 shelf: " + matchingShelves.get(0));
        bookGrid.setItems(filterShelf(matchingShelves.get(0), bookFilters));
    }

    private List<Book> filterShelf(PredefinedShelf selectedShelf, BookFilters bookFilters) {
        return selectedShelf.getBooks()
                            .stream()
                            .filter(book -> bookFilters.apply(book))
                            .collect(Collectors.toList());
    }
}
package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.Book;
import com.vaadin.flow.component.grid.Grid;
import lombok.extern.java.Log;

import java.util.logging.Level;


@Log
public class BookGrid {
    private final Grid<Book> bookGrid;

    BookGrid(Grid<Book> bookGrid) {
        this.bookGrid = bookGrid;
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
}
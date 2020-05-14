package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class BooksInShelf extends VerticalLayout {

    private final Grid<Book> bookGrid = new Grid<>(Book.class);

    public BooksInShelf(ShelfService shelfService) {
        List<Shelf> shelves = shelfService.findAll();
        ComboBox<String> shelvesCombo = new ComboBox<>();
        shelvesCombo.setPlaceholder("Select shelf");
        shelvesCombo.setItems(shelves.stream().map(Shelf::getName));
        add(shelvesCombo);

        shelvesCombo.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                System.out.println("No choice selected");
            } else {
                updateList(shelves, event.getValue());
            }
        });

        configureBookGrid();
        add(bookGrid);
//        updateList(shelves, shelvesCombo.getValue());
    }

    private void configureBookGrid() {
        addClassName("book-grid");
//        bookGrid.setColumns("title", "authors", "genre", "dateStartedReading", "dateFinishedReading", "rating");
        bookGrid.setColumns("title", "genre", "numberOfPages", "dateStartedReading", "dateFinishedReading", "rating");

    }

    private void updateList(List<Shelf> shelves, String shelfName) {
        // Find the shelf that matches the chosen shelf's name
        Shelf selectedShelf = null;
        for (Shelf shelf : shelves) {
            if (shelf.getName().equals(shelfName)) {
                System.out.println("found shelf: " + shelf.getName());
                selectedShelf = shelf;
                break;
            }
        }

        // only set the grid if the book shelf name was matched
        if (selectedShelf != null) {
            bookGrid.setItems(selectedShelf.getBooks());
        }
    }

}

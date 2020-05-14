package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

public class BooksInShelf extends VerticalLayout {

    private final Grid<Book> bookGrid = new Grid<>(Book.class);
    private final TextField filterByTitle;
    private final ComboBox<String> whichShelf;
    private final List<Shelf> shelves;
    private String chosenShelf;
    private String bookTitle; // the book to filter by (if specified)
    private BookService bookService;

    public BooksInShelf(BookService bookService, ShelfService shelfService) {
        this.bookService = bookService;
        shelves = shelfService.findAll();

        whichShelf = new ComboBox<>("Books in shelf");
        configureChosenShelf(shelves);

        filterByTitle = new TextField();
        configureFilter();

        configureBookGrid();
        add(whichShelf, filterByTitle, bookGrid);
    }

    private void configureChosenShelf(List<Shelf> shelves) {
        whichShelf.setPlaceholder("Select shelf");
        whichShelf.setItems(shelves.stream().map(Shelf::getName));
        whichShelf.setRequired(true);
        whichShelf.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                System.out.println("No choice selected");
            } else {
                chosenShelf = event.getValue();
                updateList();
            }
        });
    }

    private void configureBookGrid() {
        addClassName("book-grid");
//        bookGrid.setColumns("title", "authors", "genre", "dateStartedReading", "dateFinishedReading", "rating");
        bookGrid.setColumns("title", "genre", "numberOfPages", "dateStartedReading", "dateFinishedReading", "rating");
    }

    private void updateList() {
        if (chosenShelf == null || chosenShelf.isEmpty()) {
            return;
        }

        // Find the shelf that matches the chosen shelf's name
        Shelf selectedShelf = null;
        for (Shelf shelf : shelves) {
            if (shelf.getName().equals(chosenShelf)) {
                selectedShelf = shelf;
                break;
            }
        }

        // only set the grid if the book shelf name was matched
        if (selectedShelf != null) {
            if (bookTitle != null && !bookTitle.isEmpty()) {
                bookGrid.setItems(bookService.findAll(bookTitle));
            } else {
                bookGrid.setItems(selectedShelf.getBooks());
            }
        }
    }

    private void configureFilter() {
        filterByTitle.setPlaceholder("Filter by book title");
        filterByTitle.setClearButtonVisible(true);
        filterByTitle.setValueChangeMode(ValueChangeMode.LAZY);
        filterByTitle.addValueChangeListener(event -> {
            if (event.getValue() != null && !event.getValue().isEmpty()) {
                bookTitle = event.getValue();
            }
            updateList();
        });
    }
}

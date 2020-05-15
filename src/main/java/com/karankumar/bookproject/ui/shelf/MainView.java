package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;


@Route("")
@PageTitle("Home | Book Project")
public class MainView extends VerticalLayout {

    private BookForm bookForm;
    private BookService bookService;

    private Grid<Book> bookGrid = new Grid<>(Book.class);
    private final TextField filterByTitle;
    private final ComboBox<String> whichShelf;
    private final List<Shelf> shelves;
    private String chosenShelf;
    private String bookTitle; // the book to filter by (if specified)

    public MainView(BookService bookService, ShelfService shelfService) {
        this.bookService = bookService;
        shelves = shelfService.findAll();

        whichShelf = new ComboBox<>();
        configureChosenShelf(shelves);

        filterByTitle = new TextField();
        configureFilter();

        HorizontalLayout horizontalLayout = new HorizontalLayout(whichShelf, filterByTitle);

        configureBookGrid();
        add(horizontalLayout, bookGrid);

        bookForm = new BookForm(shelfService);
        add(bookForm);

        bookForm.addListener(BookForm.SaveEvent.class, this::saveBook);
        bookForm.addListener(BookForm.DeleteEvent.class, this::deleteBook);

        bookGrid.asSingleSelect().addValueChangeListener(event -> editBook(event.getValue()));
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

    private void editBook(Book book) {
        if (book != null) {
            bookForm.setBook(book);
        }
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        bookService.delete(event.getBook());
        updateList();
    }

    private void saveBook(BookForm.SaveEvent event) {
        bookService.save(event.getBook());
        updateList();
    }

}

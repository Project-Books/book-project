package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;

/**
 * @author karan on 10/05/2020
 */
public class BookForm extends VerticalLayout {
    private TextField bookTitle;
    private TextField bookAuthor;
    private MultiselectComboBox<String> shelf;
    private ComboBox<Genre> bookGenre;
    private IntegerField pageCount;
    private DatePicker dateStartedReading;
    private DatePicker dateFinishedReading;
    private TextArea favouriteQuote;
    private NumberField rating;

    public BookForm(ShelfService shelfService) {
        addBookTitle();
        addBookAuthor();
        addShelf(shelfService);
        addBookGenre();
        addPageCount();
        addDateStartedReading();
        addDateFinishedReading();
        addFavouriteQuote();
        addRating();

        Button addBook = new Button();
        addBook.setText("Add book");
        Button reset = new Button();
        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());

        HorizontalLayout buttons = new HorizontalLayout(addBook, reset);

        add(bookTitle, bookAuthor, shelf, bookGenre, pageCount, dateStartedReading, dateFinishedReading, favouriteQuote,
                rating, buttons);
    }



    private void addRating() {
        rating = new NumberField("Rating out of 10");
        rating.setHasControls(true);
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
        rating.setMinWidth("150px");
    }

    private void addFavouriteQuote() {
        favouriteQuote = new TextArea("Favourite quote");
        favouriteQuote.setPlaceholder("Enter favourite quote");
        favouriteQuote.setClearButtonVisible(true);
        favouriteQuote.setMinWidth("325px");
    }

    private void addDateFinishedReading() {
        dateFinishedReading = new DatePicker("Date finished");
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder("Enter date");
    }

    private void addDateStartedReading() {
        dateStartedReading = new DatePicker("Date started");
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder("Enter date");
    }

    private void addPageCount() {
        pageCount = new IntegerField("Number of pages");
        pageCount.setMin(1);
        pageCount.setHasControls(true);
        pageCount.setMinWidth("150px");
        pageCount.setClearButtonVisible(true);
    }

    private void addShelf(ShelfService shelfService) {
        shelf = new MultiselectComboBox<>();
        shelf.setLabel("Book shelf");
        shelf.setRequired(true);
        shelf.setPlaceholder("Choose a shelf");
        shelf.setClearButtonVisible(true);

        List<Shelf> shelves = shelfService.findAll();
        shelf.setItems(shelves.stream().map(Shelf::getName));
    }

    private void addBookGenre() {
        bookGenre = new ComboBox<Genre>();
        bookGenre.setItems(Genre.values());
        bookGenre.setLabel("Book genre");
        bookGenre.setPlaceholder("Choose book genre");
        bookGenre.setMinWidth("225px");
    }

    private void addBookAuthor() {
        bookAuthor = new TextField("Book author");
        bookAuthor.setPlaceholder("Enter book author");
        bookAuthor.setClearButtonVisible(true);
        bookAuthor.setRequired(true);
        bookAuthor.setRequiredIndicatorVisible(true);
    }

    private void addBookTitle() {
        bookTitle = new TextField("Book title");
        bookTitle.setPlaceholder("Enter book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
        bookTitle.setMinWidth("325px");
    }

    private void clearForm() {
        bookTitle.clear();
        bookAuthor.clear();
        shelf.clear();
        bookGenre.clear();
        pageCount.clear();
        dateStartedReading.clear();
        dateFinishedReading.clear();
        favouriteQuote.clear();
        rating.clear();
    }
}

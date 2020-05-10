package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Genre;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * @author karan on 10/05/2020
 */
public class BookForm extends VerticalLayout {
    private TextField bookTitle;
    private TextField bookAuthor;
    private ComboBox<Genre> bookGenre;
    private IntegerField pageCount;
    private DatePicker dateStartedReading;
    private DatePicker dateFinishedReading;
    private TextArea favouriteQuote;
    private NumberField rating;

    public BookForm() {
        bookTitle = new TextField("Book title");
        bookTitle.setPlaceholder("Enter book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
        bookTitle.setMinWidth("325px");

        bookAuthor = new TextField("Book author");
        bookAuthor.setPlaceholder("Enter book author");
        bookAuthor.setClearButtonVisible(true);
        bookAuthor.setRequired(true);
        bookAuthor.setRequiredIndicatorVisible(true);

//        bookGenre = new ComboBox<>(Genre.values());
        bookGenre = new ComboBox<Genre>();
        bookGenre.setItems(Genre.values());
        bookGenre.setLabel("Book genre");
        bookGenre.setPlaceholder("Choose book genre");
        bookGenre.setMinWidth("225px");

        pageCount = new IntegerField("Number of pages");
        pageCount.setMin(1);
        pageCount.setHasControls(true);
        pageCount.setMinWidth("150px");
        pageCount.setClearButtonVisible(true);

        dateStartedReading = new DatePicker("Date started");
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder("Enter date");

        dateFinishedReading = new DatePicker("Date finished");
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder("Enter date");

        favouriteQuote = new TextArea("Favourite quote");
        favouriteQuote.setPlaceholder("Enter favourite quote");
        favouriteQuote.setClearButtonVisible(true);
        favouriteQuote.setMinWidth("325px");

        rating = new NumberField("Rating out of 10");
        rating.setHasControls(true);
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
        rating.setMinWidth("150px");

        Button addBook = new Button();
        addBook.setText("Add book");
        Button reset = new Button();
        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());

        HorizontalLayout buttons = new HorizontalLayout(addBook, reset);

        add(bookTitle, bookAuthor, bookGenre, pageCount, dateStartedReading, dateFinishedReading, favouriteQuote,
                rating, buttons);

    }

    private void clearForm() {
        bookTitle.clear();
        bookAuthor.clear();
        bookGenre.clear();
        pageCount.clear();
        dateStartedReading.clear();
        dateFinishedReading.clear();
        favouriteQuote.clear();
        rating.clear();
    }
}

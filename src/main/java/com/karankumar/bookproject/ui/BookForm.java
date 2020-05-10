package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Genre;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
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
    public BookForm() {
        TextField bookTitle = new TextField("Book title");
        bookTitle.setPlaceholder("Enter book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
        bookTitle.setMinWidth("325px");

        TextField bookAuthor = new TextField("Book author");
        bookAuthor.setPlaceholder("Enter book author");
        bookAuthor.setClearButtonVisible(true);
        bookAuthor.setRequired(true);
        bookAuthor.setRequiredIndicatorVisible(true);

        Select<Genre> bookGenre = new Select(Genre.values());
        bookGenre.setLabel("Book genre");
        bookGenre.setPlaceholder("Choose book genre");
        bookGenre.setMinWidth("225px");

        IntegerField pageCount = new IntegerField("Number of pages");
        pageCount.setMin(1);
        pageCount.setHasControls(true);
        pageCount.setMinWidth("150px");
        pageCount.setClearButtonVisible(true);

        DatePicker dateStartedReading = new DatePicker("Date started");
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder("Enter date");

        DatePicker dateFinishedReading = new DatePicker("Date finished");
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder("Enter date");

        TextArea favouriteQuote = new TextArea("Favourite quote");
        favouriteQuote.setPlaceholder("Enter favourite quote");
        favouriteQuote.setClearButtonVisible(true);
        favouriteQuote.setMinWidth("325px");

        NumberField rating = new NumberField("Rating out of 10");
        rating.setHasControls(true);
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
        rating.setMinWidth("150px");

        Button addBook = new Button();
        addBook.setText("Add book");

        add(bookTitle, bookAuthor, bookGenre, pageCount, dateStartedReading, dateFinishedReading, favouriteQuote, rating, addBook);

    }
}

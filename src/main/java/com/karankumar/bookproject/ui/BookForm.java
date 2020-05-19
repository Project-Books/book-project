/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.RatingScale;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.List;

public class BookForm extends FormLayout {
    private TextField bookTitle;
    private TextField bookAuthor;
    private MultiselectComboBox<String> shelf;
    private ComboBox<Genre> bookGenre;
    private IntegerField pageCount;
    private DatePicker dateStartedReading;
    private DatePicker dateFinishedReading;
    private TextArea favouriteQuote;
    private ComboBox<RatingScale> rating;

    public static final String ENTER_DATE = "Enter date";

    public BookForm(ShelfService shelfService) {
        configureTitle();
        configureAuthor();
        configureShelf(shelfService);
        configureGenre();
        configurePageCount();
        configureDateStarted();
        configureDateFinished();
        configureQuote();
        configureRating();

        Button addBook = new Button();
        addBook.setText("Add book");
        addBook.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button reset = new Button();
        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());

        HorizontalLayout buttons = new HorizontalLayout(addBook, reset);

        HasSize[] components = {
                bookTitle,
                bookAuthor,
                dateStartedReading,
                dateFinishedReading,
                bookGenre,
                shelf,
                pageCount,
                rating,
                favouriteQuote,
        };
        setComponentMinWidth(components);

        setResponsiveSteps(new ResponsiveStep("0", 1));
        addFormItem(bookTitle, "Book title *");
        addFormItem(bookAuthor, "Book author *");
        addFormItem(shelf, "Book shelf *");
        addFormItem(dateStartedReading, ENTER_DATE);
        addFormItem(dateFinishedReading, ENTER_DATE);
        addFormItem(bookGenre, "Book genre");
        addFormItem(pageCount, "Page count");
        addFormItem(rating, "Book rating");
        addFormItem(favouriteQuote, "Favourite quote");
        add(buttons);
    }

    private void configureTitle() {
        bookTitle = new TextField();
        bookTitle.setPlaceholder("Enter a book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
    }

    private void configureAuthor() {
        bookAuthor = new TextField();
        bookAuthor.setPlaceholder("Enter a book author");
        bookAuthor.setClearButtonVisible(true);
        bookAuthor.setRequired(true);
        bookAuthor.setRequiredIndicatorVisible(true);
    }

    private void configureGenre() {
        bookGenre = new ComboBox<>();
        bookGenre.setItems(Genre.values());
        bookGenre.setPlaceholder("Choose a book genre");
    }

    private void configureShelf(ShelfService shelfService) {
        shelf = new MultiselectComboBox<>();
        shelf.setRequired(true);
        shelf.setPlaceholder("Choose a shelf");
        shelf.setClearButtonVisible(true);

        List<Shelf> shelves = shelfService.findAll();
        shelf.setItems(shelves.stream().map(Shelf::getName));
    }

    private void configureRating() {
        rating = new ComboBox<>();
        rating.setItems(RatingScale.values());
        rating.setPlaceholder("Rating");
    }

    private void configureDateStarted() {
        dateStartedReading = new DatePicker();
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder(ENTER_DATE);
    }

    private void configureDateFinished() {
        dateFinishedReading = new DatePicker();
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder(ENTER_DATE);
    }

    private void configurePageCount() {
        pageCount = new IntegerField();
        pageCount.setMin(1);
        pageCount.setHasControls(true);
        pageCount.setClearButtonVisible(true);
    }

    private void configureQuote() {
        favouriteQuote = new TextArea();
        favouriteQuote.setPlaceholder("Enter favourite quote");
        favouriteQuote.setClearButtonVisible(true);
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

    private void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("15em");
        }
    }
}

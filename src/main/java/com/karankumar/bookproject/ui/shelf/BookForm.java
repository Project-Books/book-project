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
package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Vaadin form for adding a new {@code Book}
 */
public class BookForm extends FormLayout {
    private final TextField bookTitle = new TextField();
    private final TextField authorFirstName = new TextField();
    private final TextField authorLastName = new TextField();
    private final ComboBox<PredefinedShelf.ShelfName> shelf = new ComboBox<>();
    private final ComboBox<Genre> bookGenre = new ComboBox<>();
    private final IntegerField pageCount = new IntegerField();

    private final DatePicker dateStartedReading = new DatePicker();
    private final DatePicker dateFinishedReading = new DatePicker();
    private final NumberField rating = new NumberField();

    private static final String ENTER_DATE = "Enter a date";

    Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private final Button addBook = new Button();
    private final Button reset = new Button();
    private Button delete = new Button();

    private final FormItem started;
    private final FormItem finished;
    private final FormItem ratingFormItem;

    private static final Logger logger = Logger.getLogger(BookForm.class.getName());

    public BookForm() {
        configureBinder();

        configureTitle();
        configureAuthor();
        configureShelf();
        configureGenre();
        configurePageCount();
        configureDateStarted();
        configureDateFinished();
        configureRating();

        HorizontalLayout buttons = configureButtons();

        HasSize[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                dateStartedReading,
                dateFinishedReading,
                bookGenre,
                shelf,
                pageCount,
                rating,
        };
        setComponentMinWidth(components);

        setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        addFormItem(bookTitle, "Book title *");
        addFormItem(authorFirstName, "Author's first name *");
        addFormItem(authorLastName, "Author's last name *");
        addFormItem(shelf, "Book shelf *");
        started = addFormItem(dateStartedReading, "Date started");
        finished = addFormItem(dateFinishedReading, "Date finished");
        addFormItem(bookGenre, "Book genre");
        addFormItem(pageCount, "Page count");
        ratingFormItem = addFormItem(rating, "Book rating");
        add(buttons);

        shelf.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                hideDates(shelf.getValue());
                showOrHideRating(shelf.getValue());
            }
        });
    }

    private void configureBinder() {
        binder.forField(bookTitle)
                .asRequired("Please provide a book title")
                .bind(Book::getTitle, Book::setTitle);
        binder.forField(authorFirstName)
                .bind("author.firstName");
        binder.forField(authorLastName)
                .bind("author.lastName");
        binder.forField(shelf)
                .bind("shelf.shelfName");
        binder.forField(dateStartedReading)
                .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        Binder.Binding<Book, LocalDate> bindingEndDate = binder.forField(dateFinishedReading)
                .withValidator(endDate -> !(endDate != null && dateStartedReading.getValue() != null && endDate
                                .isBefore(dateStartedReading.getValue())),
                        "Date finished cannot be earlier than the date started")
                .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        dateStartedReading.addValueChangeListener(
                event -> bindingEndDate.validate());
        binder.forField(pageCount)
                .bind(Book::getNumberOfPages, Book::setNumberOfPages);
        binder.forField(bookGenre)
                .bind(Book::getGenre, Book::setGenre);
        binder.forField(rating)
                .withConverter(new DoubleToRatingScaleConverter())
                .bind(Book::getRating, Book::setRating);
    }

    private HorizontalLayout configureButtons() {
        addBook.setText("Add book");
        addBook.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBook.addClickListener(click -> validateOnSave());

        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());

        delete = new Button();
        delete.setText("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        binder.addStatusChangeListener(event -> addBook.setEnabled(binder.isValid()));

        return new HorizontalLayout(addBook, reset, delete);
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setBook(Book book) {
        if (book == null) {
            logger.log(Level.SEVERE, "Book is null");
            System.out.println("Book is null");
        }
        if (book.getRating() == null) {
            logger.log(Level.FINE, "Rating is null");
            System.out.println("Rating is null");
        }
        if (book.getAuthor().getFirstName() == null) {
            logger.log(Level.SEVERE, "Author's first name is null");
            System.out.println("Author's first name is null");
        }
        if (book.getAuthor().getLastName() == null) {
            logger.log(Level.SEVERE, "Author's last name is null");
            System.out.println("Author's last name is null");
        }
        if (book.getDateStartedReading() == null) {
            logger.log(Level.FINE, "Date started reading is null");
            System.out.println("Date started reading is null");
        }
        if (book.getDateFinishedReading() == null) {
            logger.log(Level.FINE, "Date finished reading is null");
            System.out.println("Date finished reading is null");
        }
        if (book.getGenre() == null) {
            logger.log(Level.FINE, "Book genre is null");
            System.out.println("Book genre is null");
        }

        if (binder == null) {
            logger.log(Level.SEVERE, "Binder is null");
            System.out.println("Binder is null");
        } else {
            binder.setBean(book);
        }
    }

    private void configureTitle() {
        bookTitle.setPlaceholder("Enter a book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
    }

    private void configureAuthor() {
        authorFirstName.setPlaceholder("Enter the author's first name");
        authorFirstName.setClearButtonVisible(true);
        authorFirstName.setRequired(true);
        authorFirstName.setRequiredIndicatorVisible(true);

        authorLastName.setPlaceholder("Enter the author's last name");
        authorLastName.setClearButtonVisible(true);
        authorLastName.setRequired(true);
        authorLastName.setRequiredIndicatorVisible(true);
    }

    private void configureGenre() {
        bookGenre.setItems(Genre.values());
        bookGenre.setPlaceholder("Choose a book genre");
    }

    private void configureShelf() {
        shelf.setRequired(true);
        shelf.setPlaceholder("Choose a shelf");
        shelf.setClearButtonVisible(true);

        shelf.setItems(PredefinedShelf.ShelfName.values());
    }

    private void hideDates(PredefinedShelf.ShelfName name) {
        switch (name) {
            case TO_READ:
                started.setVisible(false);
                hideFinishDate();
                break;
            case READING:
                showStartDate();
                hideFinishDate();
                break;
            default:
                showStartDate();
                showFinishDate();
                break;
        }
    }

    private void hideFinishDate() {
        if (finished.isVisible()) {
            finished.setVisible(false);
        }
    }

    private void showStartDate() {
        if (!started.isVisible()) {
            started.setVisible(true);
        }
    }

    private void showFinishDate() {
        if (!finished.isVisible()) {
            finished.setVisible(true);
        }
    }

    private void showOrHideRating(PredefinedShelf.ShelfName name) {
        switch(name) {
            case TO_READ:
            case READING:
                ratingFormItem.setVisible(false);
                break;
            case READ:
                ratingFormItem.setVisible(true);
                break;
        }
    }

    private void configureRating() {
        rating.setHasControls(true);
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
    }

    private void configureDateStarted() {
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder(ENTER_DATE);
    }

    private void configureDateFinished() {
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder(ENTER_DATE);
    }

    private void configurePageCount() {
        pageCount.setMin(1);
        pageCount.setHasControls(true);
        pageCount.setClearButtonVisible(true);
    }

    private void clearForm() {
        HasValue[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                shelf,
                bookGenre,
                pageCount,
                dateStartedReading,
                dateFinishedReading,
                rating,
        };
        for (HasValue component : components) {
            if (component != null && !component.isEmpty()) {
                component.clear();
            }
        }
    }

    private void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("23em");
        }
    }

    // Events
    public static abstract class BookFormEvent extends ComponentEvent<BookForm> {
        private Book book;

        protected BookFormEvent(BookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    public static class SaveEvent extends BookFormEvent {
        SaveEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class DeleteEvent extends BookFormEvent {
        DeleteEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

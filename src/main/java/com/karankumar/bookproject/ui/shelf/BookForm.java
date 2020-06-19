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

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Vaadin form for adding a new {@code Book}
 */
@CssImport(
        value = "./styles/vaadin-dialog-overlay-styles.css",
        themeFor = "vaadin-dialog-overlay"
)
public class BookForm extends VerticalLayout {
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
    private static final String LABEL_ADD_BOOK = "Add book";
    private static final String LABEL_UPDATE_BOOK = "Update book";

    private final PredefinedShelfService shelfService;

    Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private final Button saveButton = new Button();
    private final Button reset = new Button();

    private final FormLayout.FormItem started;
    private final FormLayout.FormItem finished;
    private final FormLayout.FormItem ratingFormItem;

    private static final Logger LOGGER = Logger.getLogger(BookForm.class.getName());
    private final Dialog dialog;

    public BookForm(PredefinedShelfService shelfService) {
        this.shelfService = shelfService;

        dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        dialog.setMaxWidth("10px");

        FormLayout formLayout = new FormLayout();
        dialog.add(formLayout);

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

        formLayout.setResponsiveSteps();
        formLayout.addFormItem(bookTitle, "Book title *");
        formLayout.addFormItem(shelf, "Book shelf *");
        formLayout.addFormItem(authorFirstName, "Author's first name *");
        formLayout.addFormItem(authorLastName, "Author's last name *");
        started = formLayout.addFormItem(dateStartedReading, "Date started");
        finished = formLayout.addFormItem(dateFinishedReading, "Date finished");
        formLayout.addFormItem(bookGenre, "Book genre");
        formLayout.addFormItem(pageCount, "Page count");
        ratingFormItem = formLayout.addFormItem(rating, "Book rating");
        formLayout.add(buttons, 3);

        shelf.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                hideDates(shelf.getValue());
                showOrHideRating(shelf.getValue());
            }
        });

        add(dialog);
    }

    public void openForm() {
        dialog.open();
    }

    private void closeForm() {
        dialog.close();
    }


    private void configureBinder() {
        final String AFTER_TODAY_PREFIX = "The date you";
        final String AFTER_TODAY_SUFFIX = "reading the book cannot be after today's date.";

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
                .withValidator(startDate -> !(startDate != null && startDate.isAfter(LocalDate.now())),
                        AFTER_TODAY_PREFIX + " started " + AFTER_TODAY_SUFFIX)
                .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        Binder.Binding<Book, LocalDate> bindingEndDate = binder.forField(dateFinishedReading)
                .withValidator(endDate -> !(endDate != null && dateStartedReading.getValue() != null &&
                                endDate.isBefore(dateStartedReading.getValue())),
                        "The date you finished reading the book cannot be earlier than the date you started " +
                                "reading the book")
                .withValidator(endDate -> !(endDate != null && endDate.isAfter(LocalDate.now())),
                        AFTER_TODAY_PREFIX + " finished " + AFTER_TODAY_SUFFIX)
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
        saveButton.setText(LABEL_ADD_BOOK);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(click -> validateOnSave());

        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());

        Button delete = new Button();
        delete.setText("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(click -> {
            fireEvent(new DeleteEvent(this, binder.getBean()));
            closeForm();
        });
        delete.addClickListener(v -> saveButton.setText(LABEL_ADD_BOOK));

        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, reset, delete);
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            LOGGER.log(Level.INFO, "Valid binder");

            if (binder.getBean() == null) {
                LOGGER.log(Level.SEVERE, "Binder book bean is null");

                if (bookTitle.getValue() != null) {
                    String firstName = "";
                    String lastName = "";

                    if (authorFirstName.getValue() != null) {
                        firstName = authorFirstName.getValue();
                    } else {
                        LOGGER.log(Level.SEVERE, "Null first name");
                    }
                    if (authorLastName.getValue() != null) {
                        lastName = authorLastName.getValue();
                    } else {
                        LOGGER.log(Level.SEVERE, "Null last name");
                    }
                    Author author = new Author(firstName, lastName);
                    Book book = new Book(bookTitle.getValue(), author);

                    if (shelf.getValue() != null) {
                        List<PredefinedShelf> shelves = shelfService.findAll(shelf.getValue());
                        if (shelves.size() == 1) {
                            book.setShelf(shelves.get(0));
                            LOGGER.log(Level.INFO, "Shelf: " + shelves.get(0));
                        } else {
                            LOGGER.log(Level.INFO, "Shelves count = " + shelves.size());
                        }

                    } else {
                        LOGGER.log(Level.SEVERE, "Null shelf");
                    }

                    binder.setBean(book);
                    LOGGER.log(Level.INFO, "Written bean. Null? " + (binder.getBean() == null));
                    fireEvent(new SaveEvent(this, binder.getBean()));
                    LOGGER.log(Level.INFO, "Fired save event. Null? " + (binder.getBean() == null));
                    closeForm();
                } else {
                    LOGGER.log(Level.SEVERE, "Book title is null");
                }

            } else {
                LOGGER.log(Level.INFO, "Binder.getBean() is not null");
                moveBookToDifferentShelf();
                fireEvent(new SaveEvent(this, binder.getBean()));
                closeForm();
            }
        } else {
            LOGGER.log(Level.SEVERE, "Invalid binder");
        }
    }

    private void moveBookToDifferentShelf() {
        List<PredefinedShelf> shelves = shelfService.findAll(shelf.getValue());
        if (shelves.size() == 1) {
            Book book = binder.getBean();
            book.setShelf(shelves.get(0));
            LOGGER.log(Level.INFO, "2) Shelf: " + shelves.get(0));
            binder.setBean(book);
        } else {
            LOGGER.log(Level.INFO, "2) Shelves count = " + shelves.size());
        }
    }

    public void setBook(Book book) {
        if (book == null) {
            LOGGER.log(Level.SEVERE, "Book is null");
        }
        if (book.getRating() == null) {
            LOGGER.log(Level.FINE, "Rating is null");
        }
        if (book.getAuthor().getFirstName() == null) {
            LOGGER.log(Level.SEVERE, "Author's first name is null");
        }
        if (book.getAuthor().getLastName() == null) {
            LOGGER.log(Level.SEVERE, "Author's last name is null");
        }
        if (book.getDateStartedReading() == null) {
            LOGGER.log(Level.FINE, "Date started reading is null");
        }
        if (book.getDateFinishedReading() == null) {
            LOGGER.log(Level.FINE, "Date finished reading is null");
        }
        if (book.getGenre() == null) {
            LOGGER.log(Level.FINE, "Book genre is null");
        }

        if (binder == null) {
            LOGGER.log(Level.SEVERE, "Binder is null");
        } else {
            saveButton.setText(LABEL_UPDATE_BOOK);
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
            case READ:
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
        switch (name) {
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
        saveButton.setText(LABEL_ADD_BOOK);
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

    public void addBook() {
        clearForm();
        openForm();
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

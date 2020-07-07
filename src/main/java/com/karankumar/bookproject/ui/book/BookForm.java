/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.book;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
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
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * A Vaadin form for adding a new @see Book.
 */
@CssImport(
    value = "./styles/vaadin-dialog-overlay-styles.css",
    themeFor = "vaadin-dialog-overlay"
)
@Log
public class BookForm extends VerticalLayout {
    private static final String ENTER_DATE = "Enter a date";
    private static final String LABEL_ADD_BOOK = "Add book";
    private static final String LABEL_UPDATE_BOOK = "Update book";
    public final TextField bookTitle = new TextField();
    public final IntegerField seriesPosition = new IntegerField();
    public final TextField authorFirstName = new TextField();
    public final TextField authorLastName = new TextField();
    public final ComboBox<PredefinedShelf.ShelfName> shelf = new ComboBox<>();
    public final ComboBox<Genre> bookGenre = new ComboBox<>();
    public final IntegerField pageCount = new IntegerField();
    public final DatePicker dateStartedReading = new DatePicker();
    public final DatePicker dateFinishedReading = new DatePicker();
    public final NumberField rating = new NumberField();
    public final Button saveButton = new Button();
    private final PredefinedShelfService shelfService;
    private final Button reset = new Button();
    private final FormLayout.FormItem started;
    private final FormLayout.FormItem finished;
    private final FormLayout.FormItem ratingFormItem;
    private final Dialog dialog;
    public Button delete = new Button();
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    public BookForm(PredefinedShelfService shelfService) {
        this.shelfService = shelfService;

        dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);

        FormLayout formLayout = new FormLayout();
        dialog.add(formLayout);

        configureBinder();
        configureTitle();
        configureAuthor();
        configureShelf();
        configureGenre();
        configureSeriesPosition();
        configurePageCount();
        configureDateStarted();
        configureDateFinished();
        configureRating();

        HorizontalLayout buttons = configureButtons();

        HasSize[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                seriesPosition,
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
        formLayout.addFormItem(seriesPosition, "Series number");
        started = formLayout.addFormItem(dateStartedReading, "Date started");
        finished = formLayout.addFormItem(dateFinishedReading, "Date finished");
        formLayout.addFormItem(bookGenre, "Book genre");
        formLayout.addFormItem(pageCount, "Page count");
        ratingFormItem = formLayout.addFormItem(rating, "Book rating");
        formLayout.add(buttons, 3);

        shelf.addValueChangeListener(event -> {
            if (event.getValue() != null) {
            	try {
                    hideDates(shelf.getValue());
                    showOrHideRating(shelf.getValue());
                } catch (IllegalArgumentException | NotSupportedException e) {
                    e.printStackTrace();
                }
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
            .withValidator(firstName -> (firstName != null && !firstName.isEmpty()),
                    "Please enter the author's first name")
            .bind("author.firstName");
        binder.forField(authorLastName)
              .withValidator(lastName -> (lastName != null && !lastName.isEmpty()),
                "Please enter the author's last name")
              .bind("author.lastName");
        binder.forField(shelf)
              .withValidator(Objects::nonNull, "Please select a shelf")
              .bind("shelf.shelfName");
        binder.forField(seriesPosition)
              .withValidator(series -> (series == null || series > 0), "Series position must be at least 1")
              .bind(Book::getSeriesPosition, Book::setSeriesPosition);
        binder.forField(dateStartedReading)
            .withValidator(startDate -> !(startDate != null && startDate.isAfter(LocalDate.now())),
                AFTER_TODAY_PREFIX + " started " + AFTER_TODAY_SUFFIX)
            .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        binder.forField(dateFinishedReading)
            .withValidator(endDate -> !(endDate != null && dateStartedReading.getValue() != null
                    && endDate.isBefore(dateStartedReading.getValue())),
                "The date you finished reading the book cannot be earlier than the date you"
                    + " started reading the book")
            .withValidator(endDate -> !(endDate != null && endDate.isAfter(LocalDate.now())),
                AFTER_TODAY_PREFIX + " finished " + AFTER_TODAY_SUFFIX)
            .bind(Book::getDateFinishedReading, Book::setDateFinishedReading);
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


                    if (seriesPosition.getValue() != null && seriesPosition.getValue() > 0) {
                        book.setSeriesPosition(seriesPosition.getValue());
                    } else if (seriesPosition.getValue() != null) {
                        LOGGER.log(Level.SEVERE, "Negative Series value");
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
        } else {
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

    private void configureSeriesPosition(){
        seriesPosition.setPlaceholder("Enter series position");
        seriesPosition.setMin(1);
        seriesPosition.setHasControls(true);
    }

    private void configureShelf() {
        shelf.setRequired(true);
        shelf.setPlaceholder("Choose a shelf");
        shelf.setClearButtonVisible(true);

        shelf.setItems(PredefinedShelf.ShelfName.values());
    }

    /**
     * @throws NotSupportedException if an a shelf name is not yet supported
     */
    private void hideDates(PredefinedShelf.ShelfName name) throws NotSupportedException {
        switch (name) {
            case TO_READ:
                started.setVisible(false);
                hideFinishDate();
                break;
            case READING:
            case DID_NOT_FINISH:
                showStartDate();
                hideFinishDate();
                break;
            case READ:
                showStartDate();
                showFinishDate();
                break;
            default:
                throw new NotSupportedException("Shelf " + name + " not yet supported");
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

    /**
     * @throws NotSupportedException if an a shelf name is not yet supported
     */
    private void showOrHideRating(PredefinedShelf.ShelfName name) throws NotSupportedException {
        switch (name) {
            case TO_READ:
            case READING:
            case DID_NOT_FINISH:
                ratingFormItem.setVisible(false);
                break;
            case READ:
                ratingFormItem.setVisible(true);
                break;
            default:
                throw new NotSupportedException("Shelf " + name + " not yet supported");
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
                seriesPosition,
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

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
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
}

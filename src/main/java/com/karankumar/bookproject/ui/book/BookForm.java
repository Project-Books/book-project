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
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static com.karankumar.bookproject.ui.book.BookFormErrors.AFTER_TODAY_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.BOOK_TITLE_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.FINISH_DATE_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.FIRST_NAME_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.LAST_NAME_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.PAGE_NUMBER_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.SERIES_POSITION_ERROR;
import static com.karankumar.bookproject.ui.book.BookFormErrors.SHELF_ERROR;

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

    final TextField bookTitle = new TextField();
    final IntegerField seriesPosition = new IntegerField();
    final TextField authorFirstName = new TextField();
    final TextField authorLastName = new TextField();
    final ComboBox<PredefinedShelf.ShelfName> shelf = new ComboBox<>();
    final ComboBox<Genre> bookGenre = new ComboBox<>();
    final IntegerField pagesRead = new IntegerField();
    final IntegerField numberOfPages = new IntegerField();
    final DatePicker dateStartedReading = new DatePicker();
    final DatePicker dateFinishedReading = new DatePicker();
    final NumberField rating = new NumberField();
    final Button saveButton = new Button();
    final Checkbox inSeriesCheckbox = new Checkbox();

    private final PredefinedShelfService shelfService;
    final Button reset = new Button();
    private final Dialog dialog;

    FormLayout.FormItem dateStartedReadingFormItem;
    FormLayout.FormItem dateFinishedReadingFormItem;
    FormLayout.FormItem ratingFormItem;
    FormLayout.FormItem seriesPositionFormItem;
    FormLayout.FormItem pagesReadFormItem;

    Button delete = new Button();
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    public BookForm(PredefinedShelfService shelfService) {
        this.shelfService = shelfService;

        dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        FormLayout formLayout = new FormLayout();
        dialog.add(formLayout);

        bindFormFields();
        configureTitle();
        configureAuthor();
        configureShelf();
        configureGenre();
        configureSeriesPosition();
        configurePagesRead();
        configureNumberOfPages();
        configureDateStarted();
        configureDateFinished();
        configureRating();
        configureInSeriesCheckbox();
        HorizontalLayout buttons = configureFormButtons();
        HasSize[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                seriesPosition,
                dateStartedReading,
                dateFinishedReading,
                bookGenre,
                shelf,
                pagesRead,
                numberOfPages,
                rating,
        };
        setComponentMinWidth(components);
        configureFormLayout(formLayout, buttons);

        add(dialog);
    }

    private void configureInSeriesCheckbox() {
        inSeriesCheckbox.setValue(false);
        inSeriesCheckbox.addValueChangeListener(event -> {
            seriesPositionFormItem.setVisible(event.getValue());
            if (!event.getValue()) {
                seriesPosition.clear();
            }
        });
    }

    /**
     * Sets up the form layout
     *
     * @param formLayout   the form layout to configure
     * @param buttonLayout a layout consisting of buttons
     */
    private void configureFormLayout(FormLayout formLayout, HorizontalLayout buttonLayout) {
        formLayout.setResponsiveSteps();
        formLayout.addFormItem(bookTitle, "Book title *");
        formLayout.addFormItem(shelf, "Book shelf *");
        formLayout.addFormItem(authorFirstName, "Author's first name *");
        formLayout.addFormItem(authorLastName, "Author's last name *");
        dateStartedReadingFormItem = formLayout.addFormItem(dateStartedReading, "Date started");
        dateFinishedReadingFormItem = formLayout.addFormItem(dateFinishedReading, "Date finished");
        formLayout.addFormItem(bookGenre, "Book genre");
        pagesReadFormItem = formLayout.addFormItem(pagesRead, "Pages read");
        formLayout.addFormItem(numberOfPages, "Number of pages");
        ratingFormItem = formLayout.addFormItem(rating, "Book rating");
        formLayout.addFormItem(inSeriesCheckbox, "Is in series?");
        seriesPositionFormItem = formLayout.addFormItem(seriesPosition, "Series number");
        formLayout.add(buttonLayout, 3);
        seriesPositionFormItem.setVisible(false);
    }

    public void openForm() {
        dialog.open();
        showSeriesPositionFormIfSeriesPositionAvailable();
    }

    private void showSeriesPositionFormIfSeriesPositionAvailable() {
        boolean isInSeries = binder.getBean() != null && binder.getBean().getSeriesPosition() != null;
        inSeriesCheckbox.setValue(isInSeries);
        seriesPositionFormItem.setVisible(isInSeries);
    }

    private void closeForm() {
        dialog.close();
    }

    /**
     * Binds the form fields and defines custom validators where necessary
     */
    private void bindFormFields() {
        binder.forField(bookTitle)
              .asRequired(BOOK_TITLE_ERROR)
              .bind(Book::getTitle, Book::setTitle);
        binder.forField(authorFirstName)
              .withValidator(authorPredicate(), FIRST_NAME_ERROR)
              .bind("author.firstName");
        binder.forField(authorLastName)
              .withValidator(authorPredicate(), LAST_NAME_ERROR)
              .bind("author.lastName");
        binder.forField(shelf)
              .withValidator(Objects::nonNull, SHELF_ERROR)
              .bind("shelf.predefinedShelfName");
        binder.forField(seriesPosition)
              .withValidator(positiveNumberPredicate(), SERIES_POSITION_ERROR)
              .bind(Book::getSeriesPosition, Book::setSeriesPosition);
        binder.forField(dateStartedReading)
              .withValidator(datePredicate(), String.format(AFTER_TODAY_ERROR, "started"))
              .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        binder.forField(dateFinishedReading)
              .withValidator(endDatePredicate(), FINISH_DATE_ERROR)
              .withValidator(datePredicate(), String.format(AFTER_TODAY_ERROR, "finished"))
              .bind(Book::getDateFinishedReading, Book::setDateFinishedReading);
        binder.forField(numberOfPages)
              .withValidator(positiveNumberPredicate(), PAGE_NUMBER_ERROR)
              .bind(Book::getNumberOfPages, Book::setNumberOfPages);
        binder.forField(pagesRead)
              .bind(Book::getPagesRead, Book::setPagesRead);
        binder.forField(bookGenre)
              .bind(Book::getGenre, Book::setGenre);
        binder.forField(rating)
              .withConverter(new DoubleToRatingScaleConverter())
              .bind(Book::getRating, Book::setRating);
    }

    /**
     * Sets up the save, reset and delete buttons
     *
     * @return a HorizontalLayout containing the save, reset & delete buttons
     */
    private HorizontalLayout configureFormButtons() {
        configureSaveFormButton();
        configureResetFormButton();
        configureDeleteButton();

        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        return new HorizontalLayout(saveButton, reset, delete);
    }

    private void configureSaveFormButton() {
        saveButton.setText(LABEL_ADD_BOOK);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(click -> validateOnSave());
        saveButton.setDisableOnClick(true);
    }

    private void configureResetFormButton() {
        reset.setText("Reset");
        reset.addClickListener(event -> clearForm());
    }

    private void configureDeleteButton() {
        delete.setText("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(click -> {
            fireEvent(new DeleteEvent(this, binder.getBean()));
            closeForm();
        });
        delete.addClickListener(v -> saveButton.setText(LABEL_ADD_BOOK));
    }

    /**
     * Defines the behaviour once the form is in a valid state and the save button was pressed
     */
    private void validateOnSave() {
        if (binder.isValid()) {
            LOGGER.log(Level.INFO, "Valid binder");
            if (binder.getBean() == null) {
                LOGGER.log(Level.SEVERE, "Binder book bean is null");
                setBookBean();
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

    private void setBookBean() {
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
            return;
        }
        saveButton.setText(LABEL_UPDATE_BOOK);
        binder.setBean(book);
    }

    /**
     * Sets up the the book title form field
     */
    private void configureTitle() {
        bookTitle.setPlaceholder("Enter a book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
    }

    /**
     * Set up the form fields for an author's first and last name
     */
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

    /**
     * Sets up the form field for adding a book genre
     */
    private void configureGenre() {
        bookGenre.setItems(Genre.values());
        bookGenre.setPlaceholder("Choose a book genre");
    }

    /**
     * Sets up the form field for adding the position of the book in a series
     */
    private void configureSeriesPosition() {
        seriesPosition.setPlaceholder("Enter series position");
        seriesPosition.setMin(1);
        seriesPosition.setHasControls(true);
    }

    /**
     * Sets up the form field for choosing which shelf a book should go into
     */
    private void configureShelf() {
        shelf.setRequired(true);
        shelf.setPlaceholder("Choose a shelf");
        shelf.setClearButtonVisible(true);
        shelf.setItems(PredefinedShelf.ShelfName.values());
        shelf.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {
                    hideDates(shelf.getValue());
                    showOrHideRating(shelf.getValue());
                    showOrHidePagesRead(shelf.getValue());
                } catch (NotSupportedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Toggles whether the date started reading and date finished reading form fields should show
     *
     * @param name the name of the @see PredefinedShelf that was chosen in the book form
     * @throws NotSupportedException if the shelf name parameter does not match the name of a @see PredefinedShelf
     */
    private void hideDates(PredefinedShelf.ShelfName name) throws NotSupportedException {
        switch (name) {
            case TO_READ:
                dateStartedReadingFormItem.setVisible(false);
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
        if (dateFinishedReadingFormItem.isVisible()) {
            dateFinishedReadingFormItem.setVisible(false);
        }
    }

    private void showStartDate() {
        if (!dateStartedReadingFormItem.isVisible()) {
            dateStartedReadingFormItem.setVisible(true);
        }
    }

    private void showFinishDate() {
        if (!dateFinishedReadingFormItem.isVisible()) {
            dateFinishedReadingFormItem.setVisible(true);
        }
    }

    /**
     * Toggles showing the pages read depending on which shelf this new book is going into
     *
     * @param name the name of the shelf that was selected in this book form
     * @throws NotSupportedException if the shelf name parameter does not match the name of a @see PredefinedShelf
     */
    private void showOrHidePagesRead(PredefinedShelf.ShelfName name) throws NotSupportedException {
        switch (name) {
            case TO_READ:
            case READING:
            case READ:
                pagesReadFormItem.setVisible(false);
                break;
            case DID_NOT_FINISH:
                pagesReadFormItem.setVisible(true);
                break;
            default:
                throw new NotSupportedException("Shelf " + name + " not yet supported");
        }
    }

    /**
     * Toggles showing the rating depending on which shelf this new book is going into
     *
     * @param name the name of the shelf that was selected in this book form
     * @throws NotSupportedException if the shelf name parameter does not match the name of a @see PredefinedShelf
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

    /**
     * Sets up the rating form field
     */
    private void configureRating() {
        rating.setHasControls(true);
        rating.setPlaceholder("Enter a rating");
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
    }

    /**
     * Sets up the date started reading form field
     */
    private void configureDateStarted() {
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder(ENTER_DATE);
    }

    /**
     * Sets up the date finished reading form field
     */
    private void configureDateFinished() {
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder(ENTER_DATE);
    }

    /**
     * Sets up the form field for the number of pages in the book
     */
    private void configureNumberOfPages() {
        numberOfPages.setPlaceholder("Enter number of pages");
        numberOfPages.setMin(1);
        numberOfPages.setHasControls(true);
        numberOfPages.setClearButtonVisible(true);
    }

    /**
     * Sets up the form field for the number of pages read
     */
    private void configurePagesRead() {
        pagesRead.setPlaceholder("Enter number of pages read");
        pagesRead.setMin(1);
        pagesRead.setHasControls(true);
        pagesRead.setClearButtonVisible(true);
    }

    /**
     * Clears all of the form fields
     */
    private void clearForm() {
        HasValue[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                shelf,
                seriesPosition,
                bookGenre,
                pagesRead,
                numberOfPages,
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

    /**
     * Sets the minimum width for all of the form fields
     *
     * @param components all of the form fields
     */
    private void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("23em");
        }
    }

    private SerializablePredicate<LocalDate> endDatePredicate() {
        return endDate -> !(endDate != null && dateStartedReading.getValue() != null
                && endDate.isBefore(dateStartedReading.getValue()));
    }

    private SerializablePredicate<LocalDate> datePredicate() {
        return startDate -> !(startDate != null && startDate.isAfter(LocalDate.now()));
    }

    private SerializablePredicate<Integer> positiveNumberPredicate() {
        return series -> (series == null || series > 0);
    }

    private SerializablePredicate<String> authorPredicate() {
        return firstName -> (firstName != null && !firstName.isEmpty());
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

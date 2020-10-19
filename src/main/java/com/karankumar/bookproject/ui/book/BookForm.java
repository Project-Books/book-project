/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.book;

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.util.CustomShelfUtils;
import com.karankumar.bookproject.ui.components.util.ComponentUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

/**
 * A Vaadin form for adding a new @see Book.
 */
@CssImport(
        value = "./styles/vaadin-dialog-overlay-styles.css",
        themeFor = "vaadin-dialog-overlay"
)
@CssImport(
        value = "./styles/book-form-styles.css"
)
@Log
public class BookForm extends VerticalLayout {
    private static final String ENTER_DATE = "Enter a date";
    private static final String LABEL_ADD_BOOK = "Add book";
    private static final String LABEL_UPDATE_BOOK = "Update book";

    @VisibleForTesting final TextField bookTitle = new TextField();
    @VisibleForTesting final IntegerField seriesPosition = new IntegerField();
    @VisibleForTesting final TextField authorFirstName = new TextField();
    @VisibleForTesting final TextField authorLastName = new TextField();
    @VisibleForTesting final ComboBox<PredefinedShelf.ShelfName> predefinedShelfField =
            new ComboBox<>();
    @VisibleForTesting final ComboBox<String> customShelfField = new ComboBox<>();
    @VisibleForTesting final ComboBox<BookGenre> bookGenre = new ComboBox<>();
    @VisibleForTesting final IntegerField pagesRead = new IntegerField();
    @VisibleForTesting final IntegerField numberOfPages = new IntegerField();
    @VisibleForTesting final DatePicker dateStartedReading = new DatePicker();
    @VisibleForTesting final DatePicker dateFinishedReading = new DatePicker();
    @VisibleForTesting final NumberField rating = new NumberField();
    @VisibleForTesting final TextArea bookReview = new TextArea();
    @VisibleForTesting final Button saveButton = new Button();
    @VisibleForTesting final Checkbox inSeriesCheckbox = new Checkbox();
    @VisibleForTesting final Button reset = new Button();

    @VisibleForTesting FormLayout.FormItem dateStartedReadingFormItem;
    @VisibleForTesting FormLayout.FormItem dateFinishedReadingFormItem;
    @VisibleForTesting FormLayout.FormItem ratingFormItem;
    @VisibleForTesting FormLayout.FormItem bookReviewFormItem;
    @VisibleForTesting FormLayout.FormItem seriesPositionFormItem;
    @VisibleForTesting FormLayout.FormItem pagesReadFormItem;

    @VisibleForTesting HasValue[] fieldsToReset;

    @VisibleForTesting final HasValue[] fieldsToResetForToRead
            = new HasValue[]{pagesRead, dateStartedReading, dateFinishedReading, rating, bookReview};
    @VisibleForTesting final HasValue[] fieldsToResetForReading
            = new HasValue[]{pagesRead, dateFinishedReading, rating, bookReview};
    @VisibleForTesting final HasValue[] fieldsToResetForRead
            = new HasValue[]{pagesRead};
    @VisibleForTesting final HasValue[] fieldsToResetForDidNotFinish
            = new HasValue[]{dateFinishedReading, rating, bookReview};
    @VisibleForTesting final HasValue[] allFields = {
            bookTitle,
            authorFirstName,
            authorLastName,
            seriesPosition,
            dateStartedReading,
            dateFinishedReading,
            bookGenre,
            customShelfField,
            predefinedShelfField,
            pagesRead,
            numberOfPages,
            rating,
            bookReview
    };

    @VisibleForTesting Button delete = new Button();
    @VisibleForTesting Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;

    private final Dialog dialog;

    public BookForm(PredefinedShelfService predefinedShelfService,
                    CustomShelfService customShelfService) {
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;

        dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        FormLayout formLayout = new FormLayout();
        dialog.add(formLayout);

        bindFormFields();
        configureTitleFormField();
        configureAuthorFormField();
        configurePredefinedShelfField();
        configureCustomShelfField();
        configureGenreFormField();
        configureSeriesPositionFormField();
        configurePagesReadFormField();
        configureNumberOfPagesFormField();
        configureDateStartedFormField();
        configureDateFinishedFormField();
        configureRatingFormField();
        configureBookReviewFormField();
        configureInSeriesFormField();
        HorizontalLayout buttons = configureFormButtons();
        HasSize[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                seriesPosition,
                dateStartedReading,
                dateFinishedReading,
                bookGenre,
                customShelfField,
                predefinedShelfField,
                pagesRead,
                numberOfPages,
                rating,
                bookReview
        };
        ComponentUtil.setComponentClassName(components, "bookFormInputField");
        configureFormLayout(formLayout, buttons);

        add(dialog);
    }

    private void configureInSeriesFormField() {
        inSeriesCheckbox.setValue(false);
        inSeriesCheckbox.addValueChangeListener(event -> {
            seriesPositionFormItem.setVisible(event.getValue());
            if (Boolean.FALSE.equals(event.getValue())) {
                seriesPosition.clear();
            }
        });
    }

    /**
     * @param formLayout   the form layout to configure
     * @param buttonLayout a layout consisting of buttons
     */
    private void configureFormLayout(FormLayout formLayout, HorizontalLayout buttonLayout) {
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("31em", 1),
                new FormLayout.ResponsiveStep("62em", 2)
        );

        formLayout.addFormItem(bookTitle, "Book title *");
        formLayout.addFormItem(predefinedShelfField, "Book shelf *");
        formLayout.addFormItem(customShelfField, "Secondary shelf");
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
        bookReviewFormItem = formLayout.addFormItem(bookReview, "Book review");
        formLayout.add(buttonLayout, 3);
        seriesPositionFormItem.setVisible(false);
    }

    public void openForm() {
        dialog.open();
        showSeriesPositionFormIfSeriesPositionAvailable();
        addClassNameToForm();
    }

    private void addClassNameToForm() {
        UI.getCurrent().getPage()
                .executeJs("document.getElementById(\"overlay\")" +
                            ".shadowRoot" +
                            ".getElementById('overlay')" +
                            ".classList.add('bookFormOverlay');\n");
    }

    private void showSeriesPositionFormIfSeriesPositionAvailable() {
        boolean isInSeries =
                binder.getBean() != null && binder.getBean().getSeriesPosition() != null;
        inSeriesCheckbox.setValue(isInSeries);
        seriesPositionFormItem.setVisible(isInSeries);
    }

    private void closeForm() {
        dialog.close();
    }

    private void bindFormFields() {
        binder.forField(bookTitle)
              .asRequired(BookFormErrors.BOOK_TITLE_ERROR)
              .bind(Book::getTitle, Book::setTitle);
        binder.forField(authorFirstName)
              .withValidator(BookFormValidators.authorPredicate(), BookFormErrors.FIRST_NAME_ERROR)
              .bind("author.firstName");
        binder.forField(authorLastName)
              .withValidator(BookFormValidators.authorPredicate(), BookFormErrors.LAST_NAME_ERROR)
              .bind("author.lastName");
        binder.forField(predefinedShelfField)
              .withValidator(Objects::nonNull, BookFormErrors.SHELF_ERROR)
              .bind("predefinedShelf.predefinedShelfName");
        binder.forField(customShelfField)
              .bind("customShelf.shelfName");
        binder.forField(seriesPosition)
              .withValidator(BookFormValidators.positiveNumberPredicate(),
                      BookFormErrors.SERIES_POSITION_ERROR)
              .bind(Book::getSeriesPosition, Book::setSeriesPosition);
        binder.forField(dateStartedReading)
              .withValidator(BookFormValidators.datePredicate(),
                      String.format(BookFormErrors.AFTER_TODAY_ERROR, "started"))
              .bind(Book::getDateStartedReading, Book::setDateStartedReading);
        binder.forField(dateFinishedReading)
              .withValidator(isEndDateAfterStartDate(), BookFormErrors.FINISH_DATE_ERROR)
              .withValidator(BookFormValidators.datePredicate(),
                      String.format(BookFormErrors.AFTER_TODAY_ERROR, "finished"))
              .bind(Book::getDateFinishedReading, Book::setDateFinishedReading);
        binder.forField(numberOfPages)
              .withValidator(BookFormValidators.positiveNumberPredicate(),
                      BookFormErrors.PAGE_NUMBER_ERROR)
              .withValidator(BookFormValidators.maxPagesPredicate(),
                      BookFormErrors.MAX_PAGES_ERROR)
              .bind(Book::getNumberOfPages, Book::setNumberOfPages);
        binder.forField(pagesRead)
              .withValidator(BookFormValidators.maxPagesPredicate(),
                      BookFormErrors.MAX_PAGES_ERROR)
              .bind(Book::getPagesRead, Book::setPagesRead);
        binder.forField(bookGenre)
              .bind(Book::getBookGenre, Book::setBookGenre);
        binder.forField(rating)
              .withConverter(new DoubleToRatingScaleConverter())
              .bind(Book::getRating, Book::setRating);
        binder.forField(bookReview)
              .bind(Book::getBookReview, Book::setBookReview);
    }

    /**
     * @return a HorizontalLayout containing the save, reset & delete buttons
     */
    private HorizontalLayout configureFormButtons() {
        configureSaveFormButton();
        configureResetFormButton();
        configureDeleteButton();

        binder.addStatusChangeListener(event -> saveButton.setEnabled(binder.isValid()));

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, reset, delete);
        buttonLayout.addClassName("formButtonLayout");
        return buttonLayout;
    }

    private void configureSaveFormButton() {
        saveButton.setText(LABEL_ADD_BOOK);
        saveButton.setEnabled(binder.isValid());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(click -> validateOnSave());
        saveButton.setDisableOnClick(true);
    }

    private void configureResetFormButton() {
        reset.setText("Reset");
        reset.addClickListener(event -> clearFormFields());
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

    private void validateOnSave() {
        if (binder.isValid()) {
            LOGGER.log(Level.INFO, "Valid binder");
            if (binder.getBean() != null && isMovedToDifferentShelf()) {
                LOGGER.log(Level.INFO, "Binder.getBean() is not null");
                moveBookToDifferentShelf();
                ComponentUtil.clearComponentFields(fieldsToReset);
                fireEvent(new SaveEvent(this, binder.getBean()));
                closeForm();
            } else {
                setBookBean();
            }
        } else {
            LOGGER.log(Level.SEVERE, "Invalid binder");
        }
    }

    private boolean isMovedToDifferentShelf() {
        PredefinedShelf.ShelfName shelfName = predefinedShelfField.getValue();
        PredefinedShelf.ShelfName bookInShelf = binder.getBean().getPredefinedShelf().getPredefinedShelfName();
        return !shelfName.equals(bookInShelf);
    }

    private void setBookBean() {
        repopulateIfCreatingANewBook();

        if (binder.getBean() != null) {
            LOGGER.log(Level.INFO, "Written bean. Not Null.");
            ComponentUtil.clearComponentFields(fieldsToReset);
            fireEvent(new SaveEvent(this, binder.getBean()));
            showSavedConfirmation();
        } else {
            LOGGER.log(Level.SEVERE, "Could not save book");
            showErrorMessage();
        }
        closeForm();
    }

    private void repopulateIfCreatingANewBook() {
        if (binder.getBean() == null) {
            Book book = populateBookBean();
            if (book != null) {
                binder.setBean(book);
            }
        }
    }

    private Book populateBookBean() {
        String title;
        if (bookTitle.getValue() == null) {
            LOGGER.log(Level.SEVERE, "Book title from form field is null");
            return null;
        } else {
            title = bookTitle.getValue();
        }

        String firstName;
        String lastName;
        if (authorFirstName.getValue() != null) {
            firstName = authorFirstName.getValue();
        } else {
            LOGGER.log(Level.SEVERE, "Null first name");
            return null;
        }
        if (authorLastName.getValue() != null) {
            lastName = authorLastName.getValue();
        } else {
            LOGGER.log(Level.SEVERE, "Null last name");
            return null;
        }
        Author author = new Author(firstName, lastName);

        PredefinedShelf predefinedShelf;
        if (predefinedShelfField.getValue() != null) {
            predefinedShelf =
                    predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(predefinedShelfField.getValue());
        } else {
            LOGGER.log(Level.SEVERE, "Null shelf");
            return null;
        }
        Book book = new Book(title, author, predefinedShelf);

        if (customShelfField.getValue() != null && !customShelfField.getValue().isEmpty()) {
            CustomShelf shelf = customShelfService.findByShelfNameAndLoggedInUser(customShelfField.getValue());
            if (shelf != null) {
                book.setCustomShelf(shelf);
            }
        }

        if (seriesPosition.getValue() != null && seriesPosition.getValue() > 0) {
            book.setSeriesPosition(seriesPosition.getValue());
        } else if (seriesPosition.getValue() != null) {
            LOGGER.log(Level.SEVERE, "Negative Series value");
        }

        book.setBookGenre(bookGenre.getValue());
        book.setNumberOfPages(numberOfPages.getValue());
        book.setDateStartedReading(dateStartedReading.getValue());
        book.setDateFinishedReading(dateFinishedReading.getValue());

        Optional<RatingScale> ratingScale = RatingScale.of(rating.getValue());
        ratingScale.ifPresent(book::setRating);

        book.setBookReview(bookReview.getValue());
        book.setPagesRead(pagesRead.getValue());

        if (seriesPosition.getValue() != null && seriesPosition.getValue() > 0) {
            book.setSeriesPosition(seriesPosition.getValue());
        } else if (seriesPosition.getValue() != null) {
            LOGGER.log(Level.SEVERE, "Negative Series value");
        }

        return book;
    }

    private void showErrorMessage() {
        Notification.show("We could not save your book.");
    }

    private void showSavedConfirmation() {
        if (bookTitle.getValue() != null) {
            Notification.show("Saved " + bookTitle.getValue());
        }
    }

    private void moveBookToDifferentShelf() {
        PredefinedShelf shelf =
                predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(predefinedShelfField.getValue());
        if (shelf != null) {
            Book book = binder.getBean();
            book.setPredefinedShelf(shelf);
            LOGGER.log(Level.INFO, "2) Shelf: " + shelf);
            binder.setBean(book);
        }
    }

    public void setBook(Book book) {
        if (book == null) {
            LOGGER.log(Level.SEVERE, "Book is null");
            return;
        }
        saveButton.setText(LABEL_UPDATE_BOOK);
        if (binder == null) {
            LOGGER.log(Level.SEVERE, "Null binder");
            return;
        }

        // TODO: this should be removed. A custom shelf should not be mandatory, so it should be acceptable to the custom shelf to be null
        if (book.getCustomShelf() == null) {
//            book.setCustomShelf(new CustomShelf("ShelfName"));
            CustomShelf customShelf = customShelfService.createCustomShelf("ShelfName");
            book.setCustomShelf(customShelf);
        }

        binder.setBean(book);
    }

    private void configureTitleFormField() {
        bookTitle.setPlaceholder("Enter a book title");
        bookTitle.setClearButtonVisible(true);
        bookTitle.setRequired(true);
        bookTitle.setRequiredIndicatorVisible(true);
    }

    private void configureAuthorFormField() {
        authorFirstName.setPlaceholder("Enter the author's first name");
        authorFirstName.setClearButtonVisible(true);
        authorFirstName.setRequired(true);
        authorFirstName.setRequiredIndicatorVisible(true);

        authorLastName.setPlaceholder("Enter the author's last name");
        authorLastName.setClearButtonVisible(true);
        authorLastName.setRequired(true);
        authorLastName.setRequiredIndicatorVisible(true);
    }

    private void configureGenreFormField() {
        bookGenre.setItems(BookGenre.values());
        bookGenre.setPlaceholder("Choose a book genre");
    }

    private void configureSeriesPositionFormField() {
        seriesPosition.setPlaceholder("Enter series position");
        seriesPosition.setMin(1);
        seriesPosition.setHasControls(true);
    }

    private void configureCustomShelfField() {
        customShelfField.setPlaceholder("Choose a shelf");
        customShelfField.setClearButtonVisible(true);

        CustomShelfUtils customShelfUtils = new CustomShelfUtils(customShelfService);
        customShelfField.setItems(customShelfUtils.getCustomShelfNames());
    }

    private void configurePredefinedShelfField() {
        predefinedShelfField.setRequired(true);
        predefinedShelfField.setPlaceholder("Choose a shelf");
        predefinedShelfField.setClearButtonVisible(true);
        predefinedShelfField.setItems(PredefinedShelf.ShelfName.values());
        predefinedShelfField.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {
                    hideDates(predefinedShelfField.getValue());
                    showOrHideRatingAndBookReview(predefinedShelfField.getValue());
                    showOrHidePagesRead(predefinedShelfField.getValue());
                    setFieldsToReset(predefinedShelfField.getValue());
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
     * @throws NotSupportedException if the shelf name parameter does not match the name of
     *                               a @see PredefinedShelf
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
     * @throws NotSupportedException if the shelf name parameter does not match the name of
     *                               a @see PredefinedShelf
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
     * Toggles showing the rating and the bookReview depending on which shelf this new book is going into
     *
     * @param name the name of the shelf that was selected in this book form
     * @throws NotSupportedException if the shelf name parameter does not match the name of
     *                               a @see PredefinedShelf
     */
    private void showOrHideRatingAndBookReview(PredefinedShelf.ShelfName name) throws NotSupportedException {
        switch (name) {
            case TO_READ:
            case READING:
            case DID_NOT_FINISH:
                ratingFormItem.setVisible(false);
                bookReviewFormItem.setVisible(false);
                break;
            case READ:
                ratingFormItem.setVisible(true);
                bookReviewFormItem.setVisible(true);
                break;
            default:
                throw new NotSupportedException("Shelf " + name + " not yet supported");
        }
    }

    /**
     * Populates the fieldsToReset array with state-specific fields depending on which shelf the book is going into
     *
     * @param shelfName the name of the shelf that was selected in this book form
     * @throws NotSupportedException if the shelf name parameter does not match the name of
     *                               a @see PredefinedShelf
     */
    private void setFieldsToReset(PredefinedShelf.ShelfName shelfName) throws NotSupportedException {
        fieldsToReset = getFieldsToReset(shelfName);
    }

    @VisibleForTesting
    HasValue[] getFieldsToReset(PredefinedShelf.ShelfName shelfName) throws NotSupportedException {
        switch (shelfName) {
            case TO_READ:
                return fieldsToResetForToRead;
            case READING:
                return fieldsToResetForReading;
            case READ:
                return fieldsToResetForRead;
            case DID_NOT_FINISH:
                return fieldsToResetForDidNotFinish;
            default:
                throw new NotSupportedException("Shelf " + shelfName + " not yet supported");
        }
    }

    private void configureRatingFormField() {
        rating.setHasControls(true);
        rating.setPlaceholder("Enter a rating");
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
    }

    private void configureBookReviewFormField() {
        bookReview.setPlaceholder("Enter your review for the book");
        bookReview.setClearButtonVisible(true);
    }

    private void configureDateStartedFormField() {
        dateStartedReading.setClearButtonVisible(true);
        dateStartedReading.setPlaceholder(ENTER_DATE);
    }

    private void configureDateFinishedFormField() {
        dateFinishedReading.setClearButtonVisible(true);
        dateFinishedReading.setPlaceholder(ENTER_DATE);
    }

    private void configureNumberOfPagesFormField() {
        numberOfPages.setPlaceholder("Enter number of pages");
        numberOfPages.setMin(1);
        numberOfPages.setMax(Book.MAX_PAGES);
        numberOfPages.setHasControls(true);
        numberOfPages.setClearButtonVisible(true);
    }

    private void configurePagesReadFormField() {
        pagesRead.setPlaceholder("Enter number of pages read");
        pagesRead.setMin(1);
        pagesRead.setMax(Book.MAX_PAGES);
        pagesRead.setHasControls(true);
        pagesRead.setClearButtonVisible(true);
    }

    private void clearFormFields() {
        HasValue[] components = {
                bookTitle,
                authorFirstName,
                authorLastName,
                customShelfField,
                predefinedShelfField,
                inSeriesCheckbox,
                seriesPosition,
                bookGenre,
                pagesRead,
                numberOfPages,
                dateStartedReading,
                dateFinishedReading,
                rating,
                bookReview
        };
        resetSaveButtonText();
        ComponentUtil.clearComponentFields(components);
    }

    private void resetSaveButtonText() {
        saveButton.setText(LABEL_ADD_BOOK);
    }

    public void addBook() {
        clearFormFields();
        openForm();
    }

    private SerializablePredicate<LocalDate> isEndDateAfterStartDate() {
        return endDate -> {
            LocalDate dateStarted = dateStartedReading.getValue();
            if (dateStarted == null || endDate == null) {
                // allowed since these are optional fields
                return true;
            }
            return (endDate.isEqual(dateStarted) || endDate.isAfter(dateStarted));
        };
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public abstract static class BookFormEvent extends ComponentEvent<BookForm> {
        private final Book book;

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

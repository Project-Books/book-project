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

package com.karankumar.bookproject.ui.book.form;

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.book.components.author.AuthorFirstName;
import com.karankumar.bookproject.ui.book.components.author.AuthorLastName;
import com.karankumar.bookproject.ui.book.components.BookReview;
import com.karankumar.bookproject.ui.book.components.BookTitle;
import com.karankumar.bookproject.ui.book.components.CustomShelfComponent;
import com.karankumar.bookproject.ui.book.components.date.DateFinishedReading;
import com.karankumar.bookproject.ui.book.components.date.DateStartedReading;
import com.karankumar.bookproject.ui.book.components.FormDialog;
import com.karankumar.bookproject.ui.book.components.Genre;
import com.karankumar.bookproject.ui.book.components.InSeries;
import com.karankumar.bookproject.ui.book.components.pages.NumberOfPages;
import com.karankumar.bookproject.ui.book.components.pages.PagesRead;
import com.karankumar.bookproject.ui.book.components.PredefinedShelfComponent;
import com.karankumar.bookproject.ui.book.components.Rating;
import com.karankumar.bookproject.ui.book.components.SeriesPosition;
import com.karankumar.bookproject.ui.components.util.ComponentUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
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
    public static final String ENTER_DATE = "Enter a date";
    private static final String LABEL_ADD_BOOK = "Add book";
    private static final String LABEL_UPDATE_BOOK = "Update book";

    @VisibleForTesting final BookTitle bookTitle = new BookTitle();

    @VisibleForTesting final SeriesPosition seriesPosition = new SeriesPosition();
    @VisibleForTesting final AuthorFirstName authorFirstName = new AuthorFirstName();
    @VisibleForTesting final AuthorLastName authorLastName = new AuthorLastName();
    @VisibleForTesting final PredefinedShelfComponent predefinedShelfField =
            new PredefinedShelfComponent();
    @VisibleForTesting final CustomShelfComponent customShelfField = new CustomShelfComponent();
    @VisibleForTesting final Genre bookGenre = new Genre();
    @VisibleForTesting final PagesRead pagesRead = new PagesRead();
    @VisibleForTesting final NumberOfPages numberOfPages = new NumberOfPages();
    @VisibleForTesting final DateStartedReading dateStartedReading = new DateStartedReading();
    @VisibleForTesting final DateFinishedReading dateFinishedReading = new DateFinishedReading();
    @VisibleForTesting final Rating rating = new Rating();
    @VisibleForTesting final BookReview bookReview = new BookReview();
    @VisibleForTesting final Button saveButton = new Button();
    @VisibleForTesting final InSeries inSeriesCheckbox = new InSeries();
    @VisibleForTesting final Button reset = new Button();

    @VisibleForTesting FormLayout.FormItem dateStartedReadingFormItem;
    @VisibleForTesting FormLayout.FormItem dateFinishedReadingFormItem;
    @VisibleForTesting FormLayout.FormItem ratingFormItem;
    @VisibleForTesting FormLayout.FormItem bookReviewFormItem;
    @VisibleForTesting FormLayout.FormItem seriesPositionFormItem;
    @VisibleForTesting FormLayout.FormItem pagesReadFormItem;

    @VisibleForTesting HasValue[] fieldsToReset;

    @VisibleForTesting final HasValue[] fieldsToResetForToRead = new HasValue[] {
            pagesRead.getField(),
            dateStartedReading.getField(),
            dateFinishedReading.getField(),
            rating.getField(),
            bookReview.getField()
    };
    @VisibleForTesting final HasValue[] fieldsToResetForReading = new HasValue[] {
            pagesRead.getField(),
            dateFinishedReading.getField(),
            rating.getField(),
            bookReview.getField()
    };
    @VisibleForTesting final HasValue[] fieldsToResetForRead
            = new HasValue[]{pagesRead.getField()};
    @VisibleForTesting final HasValue[] fieldsToResetForDidNotFinish = new HasValue[] {
            dateFinishedReading.getField(),
            rating.getField(),
            bookReview.getField()
    };
    @VisibleForTesting final HasValue[] allFields = {
            bookTitle.getField(),
            authorFirstName.getField(),
            authorLastName.getField(),
            seriesPosition.getField(),
            dateStartedReading.getField(),
            dateFinishedReading.getField(),
            bookGenre.getField(),
            customShelfField.getField(),
            predefinedShelfField.getField(),
            pagesRead.getField(),
            numberOfPages.getField(),
            rating.getField(),
            bookReview.getField()
    };

    @VisibleForTesting Button delete = new Button();
    @VisibleForTesting Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;

    private final FormDialog formDialog;

    public BookForm(PredefinedShelfService predefinedShelfService,
                    CustomShelfService customShelfService) {
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;

        bindFormFields();
        configurePredefinedShelfField();
        configureCustomShelfField();
        configureInSeriesFormField();
        HorizontalLayout buttons = configureFormButtons();
        HasSize[] components = {
                bookTitle.getField(),
                authorFirstName.getField(),
                authorLastName.getField(),
                seriesPosition.getField(),
                dateStartedReading.getField(),
                dateFinishedReading.getField(),
                bookGenre.getField(),
                customShelfField.getField(),
                predefinedShelfField.getField(),
                pagesRead.getField(),
                numberOfPages.getField(),
                rating.getField(),
                bookReview.getField()
        };
        ComponentUtil.setComponentClassName(components, "bookFormInputField");

        FormLayout formLayout = new FormLayout();
        configureFormLayout(formLayout, buttons);

        formDialog = new FormDialog(formLayout);
        add(formDialog);
    }

    private void configureInSeriesFormField() {
        inSeriesCheckbox.getField().addValueChangeListener(event -> {
            seriesPositionFormItem.setVisible(event.getValue());
            if (Boolean.FALSE.equals(event.getValue())) {
                seriesPosition.getField().clear();
            }
        });
    }

    /**
     * @param formLayout   the form layout to configure
     * @param buttonLayout a layout consisting of buttons
     */
    private void configureFormLayout(FormLayout formLayout, HorizontalLayout buttonLayout) {
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("31em", 1),
                new FormLayout.ResponsiveStep("62em", 2)
        );

        bookTitle.add(formLayout);
        predefinedShelfField.add(formLayout);
        customShelfField.add(formLayout);
        authorFirstName.add(formLayout);
        authorLastName.add(formLayout);
        bookGenre.add(formLayout);

        dateStartedReadingFormItem = formLayout.addFormItem(
                dateStartedReading.getField(),
                dateStartedReading.getLabel()
        );
        dateFinishedReadingFormItem = formLayout.addFormItem(
                dateFinishedReading.getField(),
                dateFinishedReading.getLabel()
        );
        pagesReadFormItem = formLayout.addFormItem(pagesRead.getField(), pagesRead.getLabel());
        numberOfPages.add(formLayout);
        ratingFormItem = formLayout.addFormItem(rating.getField(), rating.getLabel());
        inSeriesCheckbox.add(formLayout);
        seriesPositionFormItem = formLayout.addFormItem(
                seriesPosition.getField(),
                seriesPosition.getLabel()
        );
        bookReviewFormItem = formLayout.addFormItem(bookReview.getField(), bookReview.getLabel());
        formLayout.add(buttonLayout, 3);
        seriesPositionFormItem.setVisible(false);
    }

    public void openForm() {
        clearErrors();
        formDialog.open();
        showSeriesPositionFormIfSeriesPositionAvailable();
        addClassNameToForm();
    }

    private void clearErrors() {
        binder = new BeanValidationBinder<>(Book.class);
        bindFormFields();
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
        inSeriesCheckbox.getField().setValue(isInSeries);
        seriesPositionFormItem.setVisible(isInSeries);
    }

    private void closeForm() {
        formDialog.close();
    }

    private void bindFormFields() {
        bookTitle.bind(binder);
        authorFirstName.bind(binder);
        authorLastName.bind(binder);
        predefinedShelfField.bind(binder);
        customShelfField.bind(binder);
        seriesPosition.bind(binder);
        dateStartedReading.bind(binder);
        dateFinishedReading.bind(binder, dateStartedReading.getValue());
        numberOfPages.bind(binder);
        pagesRead.bind(binder);
        bookGenre.bind(binder);
        rating.bind(binder);
        bookReview.bind(binder);
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
        PredefinedShelf.ShelfName bookInShelf =
                binder.getBean().getPredefinedShelf().getPredefinedShelfName();
        return !shelfName.equals(bookInShelf);
    }

    private void setBookBean() {
        repopulateIfCreatingANewBook();

        if (binder.getBean() != null) {
            populateBookShelf();
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

    /**
     * We need to fetch the complete {@link CustomShelf} entity as we store (and therefore submit)
     * only the custom shelf name. See {@see BookForm#configureCustomShelfField}
     */
    private void populateBookShelf() {
        Book book = binder.getBean();
        CustomShelf selectedCustomShelf = book.getCustomShelf();
        if (selectedCustomShelf != null) {
            String shelfName = selectedCustomShelf.getShelfName();
            CustomShelf completeCustomShelf =
                    customShelfService.findByShelfNameAndLoggedInUser(shelfName);
            if (completeCustomShelf != null) {
                book.setCustomShelf(completeCustomShelf);
            }
        }
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
        if (bookTitle.getValue() == null) {
            LOGGER.log(Level.SEVERE, "Book title from form field is null");
            return null;
        }

        if (authorFirstName.getValue() == null) {
            LOGGER.log(Level.SEVERE, "Null first name");
            return null;
        }

        if (authorLastName.getValue() == null) {
            LOGGER.log(Level.SEVERE, "Null last name");
            return null;
        }

        if (predefinedShelfField.getValue() == null) {
            LOGGER.log(Level.SEVERE, "Null predefined shelf");
            return null;
        }

        String title = bookTitle.getValue();
        String firstName = authorFirstName.getValue();
        String lastName = authorLastName.getValue();
        Author author = new Author(firstName, lastName);

        PredefinedShelf predefinedShelf =
                predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(
                        predefinedShelfField.getValue()
                );

        Book book = new Book(title, author, predefinedShelf);

        if (customShelfField.getValue() != null && !customShelfField.getValue().isEmpty()) {
            CustomShelf shelf =
                    customShelfService.findByShelfNameAndLoggedInUser(customShelfField.getValue());
            if (shelf != null) {
                book.setCustomShelf(shelf);
            }
        }

        if (seriesPosition.getValue() != null && seriesPosition.getValue() > 0) {
            book.setSeriesPosition(seriesPosition.getField().getValue());
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
        PredefinedShelf shelf = predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(
                        predefinedShelfField.getValue()
        );
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
            CustomShelf customShelf = customShelfService.createCustomShelf("ShelfName");
            book.setCustomShelf(customShelf);
        }

        binder.setBean(book);
    }

    private void configureCustomShelfField() {
        customShelfField.getField().setItems(customShelfService.getCustomShelfNames());
    }

    private void configurePredefinedShelfField() {
        predefinedShelfField.getField().addValueChangeListener(event -> {
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
                dateFinishedReading.getField().setVisible(true);
                showFinishDate();
                break;
            default:
                String message = String.format(BookFormErrors.SHELF_NOT_SUPPORTED, name);
                throw new NotSupportedException(message);
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
                String message = String.format(BookFormErrors.SHELF_NOT_SUPPORTED, name);
                throw new NotSupportedException(message);
        }
    }

    /**
     * Toggles showing the rating and the bookReview depending on which shelf this new book is
     * going into
     *
     * @param name the name of the shelf that was selected in this book form
     * @throws NotSupportedException if the shelf name parameter does not match the name of
     *                               a @see PredefinedShelf
     */
    private void showOrHideRatingAndBookReview(PredefinedShelf.ShelfName name)
            throws NotSupportedException {
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
                String message = String.format(BookFormErrors.SHELF_NOT_SUPPORTED, name);
                throw new NotSupportedException(message);
        }
    }

    /**
     * Populates the fieldsToReset array with state-specific fields depending on which shelf the
     * book is going into
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
                String message = String.format(BookFormErrors.SHELF_NOT_SUPPORTED, shelfName);
                throw new NotSupportedException(message);
        }
    }

    private void clearFormFields() {
        HasValue[] components = {
                bookTitle.getField(),
                authorFirstName.getField(),
                authorLastName.getField(),
                customShelfField.getField(),
                predefinedShelfField.getField(),
                inSeriesCheckbox.getField(),
                seriesPosition.getField(),
                bookGenre.getField(),
                pagesRead.getField(),
                numberOfPages.getField(),
                dateStartedReading.getField(),
                dateFinishedReading.getField(),
                rating.getField(),
                bookReview.getField()
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

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
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.CustomShelfUtils;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.book.components.BookGenreComboBox;
import com.karankumar.bookproject.ui.book.components.form.item.*;
import com.karankumar.bookproject.ui.book.components.form.item.visible.*;
import com.karankumar.bookproject.ui.components.utils.ComponentUtil;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.List;
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

    //TODO: Seni de FormItem olan class'ın sub'ı yapmak lazim ama visibility ozelligin yok senin
    //TODO: O yüzden seni digerlerinden ayırmak lazım yani visibility özelligi bir interface olmalu
    //TODO: Ya da decorator pattern uygulanabilir. Ya da iste bir interface yazariz bu interface gider FormItem'ı extend
    //TODO: edip yapılabilir wrap etmek yerine decoratordaki gibi ...
    @VisibleForTesting final BookTitle bookTitle = new BookTitle();
    @VisibleForTesting final SeriesPosition seriesPosition = new SeriesPosition();
    @VisibleForTesting final AuthorFirstName authorFirstName = new AuthorFirstName();

    @VisibleForTesting final AuthorLastName authorLastName = new AuthorLastName();
    @VisibleForTesting final PredefinedShelves predefinedShelf = new PredefinedShelves();
    @VisibleForTesting final ComboBox<String> customShelfField = new ComboBox<>();
    @VisibleForTesting final BookGenreComboBox bookGenre = new BookGenreComboBox();
    @VisibleForTesting final PagesRead pagesRead = new PagesRead();
    @VisibleForTesting final PageCount pageCount = new PageCount();
    @VisibleForTesting final ReadingStartDate readingStartDate = new ReadingStartDate();
    @VisibleForTesting final ReadingEndDate readingEndDate = new ReadingEndDate();
    @VisibleForTesting final Rating rating = new Rating();
    @VisibleForTesting final BookReview bookReview = new BookReview();
    @VisibleForTesting final SaveButton saveButton = new SaveButton();
    @VisibleForTesting final InSeries inSeries = new InSeries();
    @VisibleForTesting final Button reset = new Button();

    @VisibleForTesting HasValue[] fieldsToReset;

    @VisibleForTesting final HasValue[] fieldsToResetForToRead
            = new HasValue[]{pagesRead.getField(), readingStartDate.getField(), readingEndDate.getField(), rating.getField(), bookReview.getField()};
    @VisibleForTesting final HasValue[] fieldsToResetForReading
            = new HasValue[]{pagesRead.getField(), readingEndDate.getField(), rating.getField(), bookReview.getField()};
    @VisibleForTesting final HasValue[] fieldsToResetForRead
            = new HasValue[]{pagesRead.getField()};
    @VisibleForTesting final HasValue[] fieldsToResetForDidNotFinish
            = new HasValue[]{readingEndDate.getField(), rating.getField(), bookReview.getField()};

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
        bookTitle.configure();
        authorFirstName.configure();
        authorLastName.configure();
        configurePredefinedShelfField();
        configureCustomShelfField();
        bookGenre.configure();
        seriesPosition.configure();
        pagesRead.configure();
        pageCount.configure();
        readingStartDate.configure();
        configureDateFinishedFormField();
        rating.configure();
        bookReview.configure();
        inSeries.configure(seriesPosition);

        HorizontalLayout buttons = configureFormButtons();
        HasSize[] components = {
                bookTitle.getField(),
                authorFirstName.getField(),
                authorLastName.getField(),
                seriesPosition.getField(),
                readingStartDate.getField(),
                readingEndDate.getField(),
                bookGenre.getComponent(),
                customShelfField,
                predefinedShelf.getField(),
                pagesRead.getField(),
                pageCount.getField(),
                rating.getField(),
                bookReview.getField()
        };
        ComponentUtil.setComponentClassName(components, "bookFormInputField");
        configureFormLayout(formLayout, buttons);

        add(dialog);
    }

    /**
     * @param formLayout   the form layout to configure
     * @param buttonLayout a layout consisting of buttons
     */
    private void configureFormLayout(FormLayout formLayout, HorizontalLayout buttonLayout) { //TODO: Bunların her birini en sonunda FormItemlara cekmek gerekecek
        formLayout.setResponsiveSteps(                                                       //TODO: daha sonrasında inline etmek gerekecek buraları.
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("31em", 1),
                new FormLayout.ResponsiveStep("62em", 2)
        );

        bookTitle.add(formLayout);
        predefinedShelf.add(formLayout);
        formLayout.addFormItem(customShelfField, "Secondary shelf");

        authorFirstName.add(formLayout);
        authorLastName.add(formLayout);

        readingStartDate.add(formLayout);
        readingEndDate.add(formLayout);
        pagesRead.add(formLayout);
        pageCount.add(formLayout);
        rating.add(formLayout);
        inSeries.add(formLayout);
        seriesPosition.add(formLayout);
        bookReview.add(formLayout);
        formLayout.add(buttonLayout, 3);
        seriesPosition.hide();
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

        inSeries.setValue(isInSeries);

        if (isInSeries) {
            seriesPosition.show();
        } else {
            seriesPosition.hide();
        }
    }

    private void closeForm() {
        dialog.close();
    }

    private void bindFormFields() {
        bookTitle.bind(binder);
        authorFirstName.bind(binder);
        authorLastName.bind(binder);
        predefinedShelf.bind(binder);
        binder.forField(customShelfField)
              .bind("customShelf.shelfName");


        seriesPosition.bind(binder);
        readingStartDate.bind(binder);
        readingEndDate.bind(binder, readingStartDate.getField());
        pageCount.bind(binder);
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

        saveButton.bind(binder);

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton.getField(), reset, delete);
        buttonLayout.addClassName("formButtonLayout");
        return buttonLayout;
    }

    private void configureSaveFormButton() {
        saveButton.configure();
        saveButton.addListener(click -> validateOnSave());
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
        delete.addClickListener(v -> saveButton.setAddText());
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            LOGGER.log(Level.INFO, "Valid binder");
            if (binder.getBean() == null) {
                LOGGER.log(Level.SEVERE, "Binder book bean is null");
                setBookBean();
            } else {
                LOGGER.log(Level.INFO, "Binder.getBean() is not null");
                moveBookToDifferentShelf();
                ComponentUtil.clearComponentFields(fieldsToReset);
                fireEvent(new SaveEvent(this, binder.getBean()));
                closeForm();
            }
        } else {
            LOGGER.log(Level.SEVERE, "Invalid binder");
        }
    }

    private void setBookBean() {
        Book book = populateBookBean();
        if (book != null) {
            binder.setBean(book);
        }

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

    private Book populateBookBean() {
        if (bookTitle.getField().getValue() == null) {
            LOGGER.log(Level.SEVERE, "Book title from form field is null");
            return null;
        }

        if (authorFirstName.getField().getValue() == null) {
            LOGGER.log(Level.SEVERE, "Null first name");
            return null;
        }

        if (authorLastName.getField().getValue() == null) {
            LOGGER.log(Level.SEVERE, "Null last name");
            return null;
        }

        if (this.predefinedShelf.getValue() == null) {
            LOGGER.log(Level.SEVERE, "Null shelf");
            return null;
        }

        Author author = new Author(authorFirstName.getField().getValue(), authorLastName.getField().getValue());
        Book book = new Book(bookTitle.getField().getValue(), author, getPredefinedShelf());

        if (customShelfField.getValue() != null && !customShelfField.getValue().isEmpty()) {
            List<CustomShelf> shelves = customShelfService.findAll(customShelfField.getValue());
            if (shelves.size() == 1) {
                book.setCustomShelf(shelves.get(0));
            }
        }

        if (seriesPosition.getField().getValue() != null && seriesPosition.getField().getValue() > 0) {
            book.setSeriesPosition(seriesPosition.getField().getValue());
        } else if (seriesPosition.getField().getValue() != null) {
            LOGGER.log(Level.SEVERE, "Negative Series value");
        }

        book.setGenre(bookGenre.getValue());
        book.setNumberOfPages(pageCount.getField().getValue());
        book.setDateStartedReading(readingStartDate.getField().getValue());
        book.setDateFinishedReading(readingEndDate.getField().getValue());

        Result<RatingScale> result =
                new DoubleToRatingScaleConverter().convertToModel(rating.getField().getValue(), null);
        result.ifOk((SerializableConsumer<RatingScale>) book::setRating);

        book.setBookReview(bookReview.getField().getValue());
        book.setPagesRead(pagesRead.getField().getValue());

        if (seriesPosition.getField().getValue() != null && seriesPosition.getField().getValue() > 0) {
            book.setSeriesPosition(seriesPosition.getField().getValue());
        } else if (seriesPosition.getField().getValue() != null) {
            LOGGER.log(Level.SEVERE, "Negative Series value");
        }

        return book;
    }

    private PredefinedShelf getPredefinedShelf() {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        return predefinedShelfUtils.findPredefinedShelf(this.predefinedShelf.getValue());
    }

    private void showErrorMessage() {
        Notification.show("We could not save your book.");
    }

    private void showSavedConfirmation() {
        if (bookTitle.getField().getValue() != null) {
            Notification.show("Saved " + bookTitle.getField().getValue());
        }
    }

    private void moveBookToDifferentShelf() {
        List<PredefinedShelf> shelves =
                predefinedShelfService.findAll(predefinedShelf.getValue());
        if (shelves.size() == 1) {
            Book book = binder.getBean();
            book.setPredefinedShelf(shelves.get(0));
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

        saveButton.setUpdateText();

        if (binder == null) {
            LOGGER.log(Level.SEVERE, "Null binder");
            return;
        }

        // TODO: this should be removed. A custom shelf should not be mandatory, so it should
        // be acceptable to the custom shelf to be null
        if (book.getCustomShelf() == null) {
            book.setCustomShelf(new CustomShelf("ShelfName"));
        }

        binder.setBean(book);
    }

    private void configureCustomShelfField() {
        customShelfField.setPlaceholder("Choose a shelf");
        customShelfField.setClearButtonVisible(true);

        CustomShelfUtils customShelfUtils = new CustomShelfUtils(customShelfService);
        customShelfField.setItems(customShelfUtils.getCustomShelfNames());
    }

    private void configurePredefinedShelfField() {
        predefinedShelf.configure();
        predefinedShelf.getField().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {
                    hideDates(predefinedShelf.getValue());
                    showOrHideRatingAndBookReview(predefinedShelf.getValue());
                    showOrHidePagesRead(predefinedShelf.getValue());
                    setFieldsToReset(predefinedShelf.getValue());
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
        switch (name) { // TODO: 7.09.2020 Can state pattern be applied ? - kaansonmezoz
            case TO_READ:
                readingStartDate.hide();
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
        if (readingEndDate.isVisible()) {
            readingEndDate.hide();
        }
    }

    private void showStartDate() {
        if (!readingStartDate.isVisible()) {
            readingStartDate.show();
        }
    }

    private void showFinishDate() { //TODO: Buralar inline edilmeli... Hatta show her türlü çağırılsın nolcak
        if (!readingEndDate.isVisible()) {
            readingEndDate.show();
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
                pagesRead.hide();
                break;
            case DID_NOT_FINISH:
                pagesRead.show();
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
                rating.hide();
                bookReview.hide();
                break;
            case READ:
                rating.show();
                bookReview.show();
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

    private void configureDateFinishedFormField() {
        readingEndDate.configure();
    }

    private void clearFormFields() {
        HasValue[] components = {
                bookTitle.getField(),
                authorFirstName.getField(),
                authorLastName.getField(),
                customShelfField,
                predefinedShelf.getField(),
                inSeries.getField(),
                seriesPosition.getField(),
                bookGenre.getComponent(),
                pagesRead.getField(),
                pageCount.getField(),
                readingStartDate.getField(),
                readingEndDate.getField(),
                rating.getField(),
                bookReview.getField()
        };
        saveButton.resetText();
        ComponentUtil.clearComponentFields(components);
    }

    public void addBook() {
        clearFormFields();
        openForm();
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

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

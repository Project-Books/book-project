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

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

@IntegrationTest
@WebAppConfiguration
public class BookFormTest {

    private static final String firstName = "Nick";
    private static final String lastName = "Bostrom";
    private static final String bookTitle = "Superintelligence: Paths, Dangers, Strategies";
    private static final Genre genre = Genre.SCIENCE;
    private static final LocalDate dateStarted = LocalDate.now().minusDays(4);
    private static final LocalDate dateFinished = LocalDate.now();
    private static final RatingScale ratingVal = RatingScale.NINE;
    private static final DoubleToRatingScaleConverter converter = new DoubleToRatingScaleConverter();
    private static final int SERIES_POSITION = 10;
    private static int pagesRead;
    private static int numberOfPages;
    private static int seriesPosition;
    private static final CustomShelf customShelf = new CustomShelf("BookFormTestShelf");

    private static Routes routes;
    private static PredefinedShelf readShelf;
    private static BookForm bookForm;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private BookService bookService;

    @Autowired private PredefinedShelfService predefinedShelfService;
    @Autowired private CustomShelfService customShelfService;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup() {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(bookService != null);
        bookService.deleteAll();

        customShelfService.deleteAll();
        customShelfService.save(customShelf);

        Assumptions.assumeTrue(predefinedShelfService != null);
        bookForm = createBookForm(READ, true);
    }

    private BookForm createBookForm(ShelfName shelf, boolean isInSeries) {
        BookForm bookForm = new BookForm(predefinedShelfService, customShelfService);
        readShelf = predefinedShelfService.findAll().get(2);
        bookForm.setBook(createBook(shelf, isInSeries));
        return bookForm;
    }

    private Book createBook(ShelfName shelfName, boolean isInSeries) {
        // values that are present in all forms
        Author author = new Author(firstName, lastName);
        PredefinedShelf shelf = predefinedShelfService.findAll(shelfName).get(0);
        Book book = new Book(bookTitle, author, shelf);

        pagesRead = generateRandomNumberOfPages();
        numberOfPages = generateRandomNumberOfPages();
        seriesPosition = SERIES_POSITION;

        book.setGenre(genre);
        book.setNumberOfPages(numberOfPages);
        book.setCustomShelf(customShelfService.findAll().get(0));
        if (isInSeries) {
            book.setSeriesPosition(SERIES_POSITION);
        }

        // values that are only present for specific predefined shelves
        switch (shelfName) {
            case READING:
                book.setDateStartedReading(dateStarted);
                break;
            case READ:
                book.setDateStartedReading(dateStarted);
                book.setDateFinishedReading(dateFinished);
                book.setRating(ratingVal);
                break;
            case DID_NOT_FINISH:
                book.setDateStartedReading(dateStarted);
                book.setPagesRead(pagesRead);
                book.setRating(ratingVal);
                break;
        }
        return book;
    }

    private int generateRandomNumberOfPages() {
        return ThreadLocalRandom.current().nextInt(300, (2000 + 1));
    }

    /**
     * Tests whether the form fields are correctly populated
     */
    @Test
    void formFieldsPopulated() {
        Assertions.assertEquals(bookTitle, bookForm.bookTitle.getValue());
        Assertions.assertEquals(firstName, bookForm.authorFirstName.getValue());
        Assertions.assertEquals(lastName, bookForm.authorLastName.getValue());
        Assertions.assertEquals(readShelf.getPredefinedShelfName(),
                bookForm.predefinedShelfField.getValue());
        Assertions.assertEquals(genre, bookForm.bookGenre.getValue());
        Assertions.assertEquals(numberOfPages, bookForm.numberOfPages.getValue());
        Assertions.assertEquals(dateStarted, bookForm.dateStartedReading.getValue());
        Assertions.assertEquals(dateFinished, bookForm.dateFinishedReading.getValue());
        double rating = converter.convertToPresentation(ratingVal, null);
        Assertions.assertEquals(rating, bookForm.rating.getValue());
        Assertions.assertEquals(seriesPosition, bookForm.seriesPosition.getValue());
    }

    enum EventType { SAVED, DELETED }

    /**
     * Tests whether the event is populated with the values from the form
     * @param eventType represents a saved event (when the user presses the save button) or a delete event (when the
     *                  user presses the delete event)
     */
    @ParameterizedTest
    @EnumSource(EventType.class)
    void saveEventPopulated(EventType eventType) {
        // given
        populateBookForm(READ, false, null);

        // when
        AtomicReference<Book> bookReference = new AtomicReference<>(null);
        if (eventType.equals(EventType.SAVED)) {
            bookForm
                .addListener(BookForm.SaveEvent.class, event -> bookReference.set(event.getBook()));
            bookForm.saveButton.click();
        } else if (eventType.equals(EventType.DELETED)) {
            bookForm.addListener(BookForm.DeleteEvent.class,
                event -> bookReference.set(event.getBook()));
            bookForm.delete.click();
        }
        Book savedOrDeletedBook = bookReference.get();

        // then
        Assertions.assertEquals(bookTitle, savedOrDeletedBook.getTitle());
        Assertions.assertEquals(firstName, savedOrDeletedBook.getAuthor().getFirstName());
        Assertions.assertEquals(lastName, savedOrDeletedBook.getAuthor().getLastName());
        Assertions.assertEquals(readShelf.getShelfName(),
                savedOrDeletedBook.getPredefinedShelf()
                                  .getShelfName());
        Assertions.assertEquals(genre, savedOrDeletedBook.getGenre());
        Assertions.assertEquals(numberOfPages, savedOrDeletedBook.getNumberOfPages());
        Assertions.assertEquals(dateStarted, savedOrDeletedBook.getDateStartedReading());
        Assertions.assertEquals(dateFinished, savedOrDeletedBook.getDateFinishedReading());
        Assertions.assertEquals(ratingVal, savedOrDeletedBook.getRating());
        Assertions.assertEquals(seriesPosition, savedOrDeletedBook.getSeriesPosition());
    }

    private void populateBookForm(ShelfName shelfName, boolean isInSeries, Book maybePresentBook) {
        if (maybePresentBook != null) {
            bookForm.setBook(maybePresentBook);
        } else {
            bookForm.authorFirstName.setValue(firstName);
            bookForm.authorLastName.setValue(lastName);
            bookForm.bookTitle.setValue(bookTitle);
            bookForm.predefinedShelfField.setValue(readShelf.getPredefinedShelfName());
            bookForm.bookGenre.setValue(genre);
            bookForm.numberOfPages.setValue(numberOfPages);
            if (isInSeries) {
                bookForm.seriesPosition.setValue(SERIES_POSITION);
            }
        }
        switch (shelfName) {
            case TO_READ:
                bookForm.predefinedShelfField.setValue(shelfName);
                break;
            case READING:
                bookForm.predefinedShelfField.setValue(shelfName);
                bookForm.dateStartedReading.setValue(dateStarted);
                break;
            case READ:
                bookForm.predefinedShelfField.setValue(shelfName);
                bookForm.dateStartedReading.setValue(dateStarted);
                bookForm.dateFinishedReading.setValue(dateFinished);
                bookForm.rating.setValue(converter.convertToPresentation(ratingVal, null));
                break;
            case DID_NOT_FINISH:
                bookForm.predefinedShelfField.setValue(shelfName);
                bookForm.dateStartedReading.setValue(dateStarted);
                bookForm.pagesRead.setValue(pagesRead);
                bookForm.rating.setValue(converter.convertToPresentation(ratingVal, null));
                break;
        }
    }

    /**
     * Tests whether the reset button correctly clears all fields when clicked
     */
    @Test
    void formCanBeCleared() {
        // given
        populateBookForm(READ, false, null);
        assumeAllFormFieldsArePopulated();

        // when
        bookForm.reset.click();

        // then
        Assertions.assertTrue(bookForm.authorFirstName.isEmpty());
        Assertions.assertTrue(bookForm.authorLastName.isEmpty());
        Assertions.assertTrue(bookForm.bookTitle.isEmpty());
        Assertions.assertTrue(bookForm.customShelfField.isEmpty());
        Assertions.assertTrue(bookForm.predefinedShelfField.isEmpty());
        Assertions.assertTrue(bookForm.bookGenre.isEmpty());
        Assertions.assertTrue(bookForm.pagesRead.isEmpty());
        Assertions.assertTrue(bookForm.numberOfPages.isEmpty());
        Assertions.assertTrue(bookForm.dateStartedReading.isEmpty());
        Assertions.assertTrue(bookForm.dateFinishedReading.isEmpty());
        Assertions.assertTrue(bookForm.rating.isEmpty());
    }

    private void assumeAllFormFieldsArePopulated() {
        Assumptions.assumeFalse(bookForm.authorFirstName.isEmpty());
        Assumptions.assumeFalse(bookForm.authorLastName.isEmpty());
        Assumptions.assumeFalse(bookForm.bookTitle.isEmpty());
        Assumptions.assumeFalse(bookForm.predefinedShelfField.isEmpty());
        Assumptions.assumeFalse(bookForm.bookGenre.isEmpty());
        Assumptions.assumeFalse(bookForm.numberOfPages.isEmpty());
        Assumptions.assumeFalse(bookForm.dateStartedReading.isEmpty());
        Assumptions.assumeFalse(bookForm.dateFinishedReading.isEmpty());
    }

    @Test
    void correctFormFieldsShowForToReadShelf() {
        bookForm.predefinedShelfField.setValue(TO_READ);
        Assertions.assertFalse(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.ratingFormItem.isVisible());
        Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void correctFormFieldsShowForReadingShelf() {
        bookForm.predefinedShelfField.setValue(READING);
        Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.ratingFormItem.isVisible());
        Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void correctFormFieldsShowForReadShelf() {
        bookForm.predefinedShelfField.setValue(READ);
        Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertTrue(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertTrue(bookForm.ratingFormItem.isVisible());
        Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void correctFormFieldsShowForDidNotFinishShelf() {
        bookForm.predefinedShelfField.setValue(DID_NOT_FINISH);
        Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.ratingFormItem.isVisible());
        Assertions.assertTrue(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void shouldNotAllowNegativeSeriesPosition() {
        // given
        bookForm.seriesPosition.setValue(-1);

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(1, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(BookFormErrors.SERIES_POSITION_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow());
    }

    @Test
    void shouldNotAllowNegativePageNumbers() {
        // given
        bookForm.numberOfPages.setValue(-1);

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(1, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(BookFormErrors.PAGE_NUMBER_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void shouldNotAllowEmptyBookTitle() {
        // given
        bookForm.bookTitle.setValue("");

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(1, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(BookFormErrors.BOOK_TITLE_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void shouldNotAllowEmptyAuthorFirstName() {
        // given
        bookForm.authorFirstName.setValue("");

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(1, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(BookFormErrors.FIRST_NAME_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void shouldNotAllowEmptyAuthorLastName() {
        // given
        bookForm.authorLastName.setValue("");

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(1, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(BookFormErrors.LAST_NAME_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void shouldNotAllowEmptyShelf() {
        // given
        bookForm.predefinedShelfField.setValue(null);

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(1, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(BookFormErrors.SHELF_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void shouldNotAllowFutureStartDate() {
        // given
        bookForm.dateStartedReading.setValue(LocalDate.now().plusDays(5));

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        Assertions.assertTrue(validationStatus.hasErrors());
        Assertions.assertEquals(2, validationStatus.getFieldValidationErrors().size());
        Assertions.assertEquals(String.format(BookFormErrors.AFTER_TODAY_ERROR, "started"),
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
        Assertions.assertEquals(BookFormErrors.FINISH_DATE_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(1)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void whenIsInSeriesCheckedDisplaySeriesPosition() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        // then
        Assertions.assertTrue(bookForm.seriesPositionFormItem.isVisible());
        Assertions.assertTrue(bookForm.seriesPosition.isVisible());
        Assertions.assertEquals(SERIES_POSITION, bookForm.seriesPosition.getValue());
    }

    @Test
    void whenIsInSeriesUnchecked_SeriesPositionShouldNotShow() {
        // given
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @Test
    void whenSeriesPositionIsSwitchedOnAndThenOff_seriesPositionHides() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        // when
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @Test
    void shouldDisplaySeriesPosition_withSeriesPositionPopulated_whenBookHasSeriesPosition() {
        // given
        populateBookForm(READ, false, null);

        // when
        bookForm.openForm();

        // then
        Assertions.assertTrue(bookForm.seriesPositionFormItem.isVisible());
        Assertions.assertTrue(bookForm.seriesPosition.isVisible());
        Assertions.assertEquals(SERIES_POSITION, bookForm.seriesPosition.getValue());
    }

    @Test
    void shouldNotDisplaySeriesPosition_whenBookDoesNotHaveSeriesPosition() {
        // given
        bookForm = createBookForm(READ, false);

        bookForm.openForm();

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    /**
     * Tests whether a book is saved to the database when the save-event is called
     */
    @Test
    void shouldSaveBookToDatabase() {
        // given
        bookForm = createBookForm(TO_READ, false);

        // when
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();

        // then
        Assertions.assertEquals(1, bookService.count());
        Book bookInDatabase = bookService.findAll().get(0);
        assertBookInSpecificShelf(TO_READ, bookInDatabase);
    }

    /**
     * Tests whether a book is updated in the database when the save-event is called
     */
    @Test
    void shouldUpdateValuesForBook() {
        // given
        bookForm = createBookForm(TO_READ, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();

        Book savedBook = bookService.findAll().get(0);
        Assertions.assertEquals(bookTitle, savedBook.getTitle());
        Assertions.assertEquals(TO_READ, savedBook.getPredefinedShelf().getPredefinedShelfName());
        Assertions.assertEquals(firstName, savedBook.getAuthor().getFirstName());
        Assertions.assertEquals(lastName, savedBook.getAuthor().getLastName());
        Assertions.assertEquals(genre, savedBook.getGenre());
        Assertions.assertNull(savedBook.getDateStartedReading());
        Assertions.assertNull(savedBook.getDateFinishedReading());

        populateBookForm(READ, false, savedBook);
        bookForm.bookTitle.setValue("IT");
        bookForm.authorFirstName.setValue("Stephen");
        bookForm.authorLastName.setValue("King");
        bookForm.bookGenre.setValue(Genre.HORROR);

        // when
        bookForm.saveButton.click();

        // then
        Assertions.assertEquals(1, bookService.count());
        Book updatedBook = bookService.findAll().get(0);
        Assertions.assertEquals("IT", updatedBook.getTitle());
        Assertions.assertEquals(READ, updatedBook.getPredefinedShelf().getPredefinedShelfName());
        Assertions.assertEquals("Stephen", updatedBook.getAuthor().getFirstName());
        Assertions.assertEquals("King", updatedBook.getAuthor().getLastName());
        Assertions.assertEquals(Genre.HORROR, updatedBook.getGenre());
        Assertions.assertEquals(dateStarted, updatedBook.getDateStartedReading());
        Assertions.assertEquals(dateFinished, updatedBook.getDateFinishedReading());
    }


    private static Stream<Arguments> shelfCombinations() {
        return Stream.of(
                Arguments.of(TO_READ, READING),
                Arguments.of(TO_READ, READ),
                Arguments.of(TO_READ, DID_NOT_FINISH),
                Arguments.of(READING, DID_NOT_FINISH),
                Arguments.of(READING, READ)
                // TODO: these cases are not passing atm because of issue #271
                // Arguments.of(READING, TO_READ),
                // Arguments.of(READ, TO_READ),
                // Arguments.of(READ, DID_NOT_FINISH),
                // Arguments.of(READ, READING),
                // Arguments.of(DID_NOT_FINISH, TO_READ),
                // Arguments.of(DID_NOT_FINISH, READING),
                // Arguments.of(DID_NOT_FINISH, READ)
                );
    }

    /**
     * Tests whether a book is updated in the database when the save-event is called
     */
    @ParameterizedTest
    @MethodSource("shelfCombinations")
    void shouldUpdateValuesWhenBookIsMovedBetweenShelves(ShelfName initialShelf, ShelfName newShelf) {
        // given
        bookForm = createBookForm(initialShelf, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();
        populateBookForm(newShelf, false, bookService.findAll().get(0));

        // when
        bookForm.saveButton.click();

        // then
        Assertions.assertEquals(1, bookService.count());
        Book bookInDatabase = bookService.findAll().get(0);
        assertBookInSpecificShelf(newShelf, bookInDatabase);
    }

    private void assertBookInSpecificShelf(ShelfName shelfName, Book bookInDatabase) {
        // these values should be present no matter which shelf the book is in
        Assertions.assertEquals(bookTitle, bookInDatabase.getTitle());
        Assertions.assertEquals(shelfName, bookInDatabase.getPredefinedShelf().getPredefinedShelfName());
        Assertions.assertEquals(firstName, bookInDatabase.getAuthor().getFirstName());
        Assertions.assertEquals(lastName, bookInDatabase.getAuthor().getLastName());
        Assertions.assertEquals(genre, bookInDatabase.getGenre());
        Assertions.assertEquals(numberOfPages, bookInDatabase.getNumberOfPages());

        // these values should only be present for specific predefined shelves
        switch (shelfName) {
            case TO_READ:
                Assertions.assertNull(bookInDatabase.getDateStartedReading());
                Assertions.assertNull(bookInDatabase.getDateFinishedReading());
                Assertions.assertEquals(RatingScale.NO_RATING, bookInDatabase.getRating());
                Assertions.assertNull(bookInDatabase.getPagesRead());
                break;
            case READING:
                Assertions.assertEquals(dateStarted, bookInDatabase.getDateStartedReading());
                Assertions.assertNull(bookInDatabase.getDateFinishedReading());
                Assertions.assertEquals(RatingScale.NO_RATING, bookInDatabase.getRating());
                Assertions.assertNull(bookInDatabase.getPagesRead());
                break;
            case READ:
                Assertions.assertEquals(dateStarted, bookInDatabase.getDateStartedReading());
                Assertions.assertEquals(dateFinished, bookInDatabase.getDateFinishedReading());
                Assertions.assertEquals(0, bookInDatabase.getRating().compareTo(ratingVal));
                Assertions.assertNull(bookInDatabase.getPagesRead());
                break;
            case DID_NOT_FINISH:
                Assertions.assertEquals(dateStarted, bookInDatabase.getDateStartedReading());
                Assertions.assertNull(bookInDatabase.getDateFinishedReading());
                Assertions.assertEquals(0, bookInDatabase.getRating().compareTo(ratingVal));
                Assertions.assertEquals(pagesRead, bookInDatabase.getPagesRead());
                break;
        }
    }

    /**
     * Tests whether a book is removed from the database when the delete-event is called
     */
    @Test
    void shouldDeleteBookFromDatabase() {
        // given
        bookForm = createBookForm(TO_READ, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();
        Assertions.assertEquals(1, bookService.count());

        // when
        bookForm.addListener(BookForm.DeleteEvent.class, event -> bookService.delete(event.getBook()));
        bookForm.delete.click();

        // then
        Assertions.assertEquals(0, bookService.count());
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

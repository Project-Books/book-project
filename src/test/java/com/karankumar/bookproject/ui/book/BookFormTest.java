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
import com.karankumar.bookproject.backend.entity.*;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
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

import javax.transaction.NotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.TO_READ;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READING;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.DID_NOT_FINISH;

@IntegrationTest
@WebAppConfiguration
class BookFormTest {

    private static final String firstName = "Nick";
    private static final String lastName = "Bostrom";
    private static final String bookTitle = "Superintelligence: Paths, Dangers, Strategies";
    private static final BookGenre BOOK_GENRE = BookGenre.SCIENCE;
    private static final LocalDate dateStarted = LocalDate.now().minusDays(4);
    private static final LocalDate dateFinished = LocalDate.now();
    private static final RatingScale ratingVal = RatingScale.NINE;
    private static final String bookReview = "Very good. Would read again.";
    private static final int SERIES_POSITION = 10;
    private static final int pagesRead = 450;
    private static final int numberOfPages = 1000;
    private static int seriesPosition;
    private static final CustomShelf customShelf = new CustomShelf("BookFormTestShelf");

    private static final LocalDate NULL_STARED_DATE = null;
    private static final LocalDate NULL_FINISHED_DATE = null;
    private static final Integer NO_PAGES_READ = null;
    private static final String NO_BOOK_REVIEW = "";

    private static Routes routes;
    private static PredefinedShelf readShelf;
    private static BookForm bookForm;

    private static PredefinedShelfUtils predefinedShelfUtils;

    @Autowired private ApplicationContext ctx;

    @Autowired private BookService bookService;
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

        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);

        bookService.deleteAll();
        customShelfService.deleteAll();
        customShelfService.save(customShelf);

        bookForm = createBookForm(READ, true);
    }

    private BookForm createBookForm(PredefinedShelf.ShelfName shelf, boolean isInSeries) {
        BookForm bookForm = new BookForm(predefinedShelfService, customShelfService);
        readShelf = predefinedShelfUtils.findReadShelf();
        bookForm.setBook(createBook(shelf, isInSeries, bookTitle));
        return bookForm;
    }

    private Book createBook(PredefinedShelf.ShelfName shelfName, boolean isInSeries,
                            String bookTitle) {
        Author author = new Author(firstName, lastName);
        PredefinedShelf shelf = predefinedShelfUtils.findPredefinedShelf(shelfName);
        Book book = new Book(bookTitle, author, shelf);

        seriesPosition = SERIES_POSITION;

        book.setBookGenre(BOOK_GENRE);
        book.setNumberOfPages(numberOfPages);
        book.setCustomShelf(customShelfService.findAll().get(0));
        if (isInSeries) {
            book.setSeriesPosition(SERIES_POSITION);
        }

        return setShelfSpecificFields(book, shelfName);
    }

    private Book setShelfSpecificFields(Book book, PredefinedShelf.ShelfName shelfName) {
        switch (shelfName) {
            case READING:
                book.setDateStartedReading(dateStarted);
                break;
            case READ:
                book.setDateStartedReading(dateStarted);
                book.setDateFinishedReading(dateFinished);
                book.setRating(ratingVal);
                book.setBookReview(bookReview);
                break;
            case DID_NOT_FINISH:
                book.setDateStartedReading(dateStarted);
                book.setPagesRead(pagesRead);
                book.setRating(ratingVal);
                break;
        }
        return book;
    }

    /**
     * Tests whether the form fields are correctly populated
     */
    @Test
    void formFieldsPopulated() {
        double rating = RatingScale.toDouble(ratingVal);
        assertAll(
                () -> assertEquals(bookTitle, bookForm.bookTitle.getValue()),
                () -> assertEquals(firstName, bookForm.authorFirstName.getValue()),
                () -> assertEquals(lastName, bookForm.authorLastName.getValue()),
                () -> assertEquals(readShelf.getPredefinedShelfName(),
                        bookForm.predefinedShelfField.getValue()),
                () -> assertEquals(BOOK_GENRE, bookForm.bookGenre.getValue()),
                () -> assertEquals(numberOfPages, bookForm.numberOfPages.getValue()),
                () -> assertEquals(dateStarted, bookForm.dateStartedReading.getValue()),
                () -> assertEquals(dateFinished, bookForm.dateFinishedReading.getValue()),
                () -> assertEquals(rating, bookForm.rating.getValue()),
                () -> assertEquals(bookReview, bookForm.bookReview.getValue()),
                () -> assertEquals(seriesPosition, bookForm.seriesPosition.getValue())
        );

    }

    enum EventType {SAVED, DELETED}

    /**
     * Tests whether the event is populated with the values from the form
     *
     * @param eventType represents a saved event (when the user presses the save button) or a delete
     *                  event (when the user presses the delete event)
     */
    @ParameterizedTest
    @EnumSource(EventType.class)
    void saveEventPopulated(EventType eventType) {
        // given
        populateBookForm();

        // when
        AtomicReference<Book> bookReference = new AtomicReference<>(null);
        if (eventType.equals(EventType.SAVED)) {
            bookForm
                    .addListener(BookForm.SaveEvent.class,
                            event -> bookReference.set(event.getBook()));
            bookForm.saveButton.click();
        } else if (eventType.equals(EventType.DELETED)) {
            bookForm.addListener(BookForm.DeleteEvent.class,
                    event -> bookReference.set(event.getBook()));
            bookForm.delete.click();
        }
        Book savedOrDeletedBook = bookReference.get();

        // then
        assertThat(createBook(readShelf.getPredefinedShelfName(), true, bookTitle))
                .isEqualToComparingFieldByField(savedOrDeletedBook);
    }

    private void populateBookForm() {
        bookForm.authorFirstName.setValue(firstName);
        bookForm.authorLastName.setValue(lastName);
        bookForm.bookTitle.setValue(bookTitle);
        bookForm.predefinedShelfField.setValue(readShelf.getPredefinedShelfName());
        bookForm.bookGenre.setValue(BOOK_GENRE);
        bookForm.numberOfPages.setValue(numberOfPages);
        populateBookShelf(PredefinedShelf.ShelfName.READ);
    }

    private void populateBookFormWithExistingBook(PredefinedShelf.ShelfName shelfName,
                                                  Book existingBook) {
        bookForm.setBook(existingBook);
        populateBookShelf(shelfName);
    }

    private void populateBookShelf(PredefinedShelf.ShelfName shelfName) {
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
                bookForm.rating.setValue(RatingScale.toDouble(ratingVal));
                bookForm.bookReview.setValue(bookReview);
                break;
            case DID_NOT_FINISH:
                bookForm.predefinedShelfField.setValue(shelfName);
                bookForm.dateStartedReading.setValue(dateStarted);
                bookForm.pagesRead.setValue(pagesRead);
                break;
        }
    }

    /**
     * Tests whether the reset button correctly clears all fields when clicked
     */
    @Test
    void formCanBeCleared() {
        // given
        populateBookForm();
        assumeAllFormFieldsArePopulated();

        // when
        bookForm.reset.click();

        // then
        assertAllFieldsAreEmpty();
    }

    private void assertAllFieldsAreEmpty() {
        System.out.println("hi");
        for (HasValue field : bookForm.allFields) {
            assertTrue(field.isEmpty(), field.toString() + " is not empty");
        }
    }

    private void assumeAllFormFieldsArePopulated() {
        assumeFalse(bookForm.authorFirstName.isEmpty());
        assumeFalse(bookForm.authorLastName.isEmpty());
        assumeFalse(bookForm.bookTitle.isEmpty());
        assumeFalse(bookForm.predefinedShelfField.isEmpty());
        assumeFalse(bookForm.bookGenre.isEmpty());
        assumeFalse(bookForm.numberOfPages.isEmpty());
        assumeFalse(bookForm.dateStartedReading.isEmpty());
        assumeFalse(bookForm.dateFinishedReading.isEmpty());
        assumeFalse(bookForm.rating.isEmpty());
        assumeFalse(bookForm.bookReview.isEmpty());
    }

    @Test
    void correctFormFieldsShowForToReadShelf() {
        bookForm.predefinedShelfField.setValue(TO_READ);
        assertNonToReadFieldsAreHidden();
    }

    private void assertNonToReadFieldsAreHidden() {
        assertAll(
                () -> assertFalse(bookForm.dateStartedReadingFormItem.isVisible()),
                () -> assertFalse(bookForm.dateFinishedReadingFormItem.isVisible()),
                () -> assertFalse(bookForm.ratingFormItem.isVisible()),
                () -> assertFalse(bookForm.bookReviewFormItem.isVisible()),
                () -> assertFalse(bookForm.pagesReadFormItem.isVisible())
        );
    }

    @Test
    void correctFormFieldsShowForReadingShelf() {
        bookForm.predefinedShelfField.setValue(READING);
        assertAll(
                () -> assertTrue(bookForm.dateStartedReadingFormItem.isVisible()),
                () -> assertFalse(bookForm.dateFinishedReadingFormItem.isVisible()),
                () -> assertFalse(bookForm.ratingFormItem.isVisible()),
                () -> assertFalse(bookForm.bookReviewFormItem.isVisible()),
                () -> assertFalse(bookForm.pagesReadFormItem.isVisible())
        );
    }

    @Test
    void correctFormFieldsShowForReadShelf() {
        bookForm.predefinedShelfField.setValue(READ);
        assertAll(
                () -> assertTrue(bookForm.dateStartedReadingFormItem.isVisible()),
                () -> assertTrue(bookForm.dateFinishedReadingFormItem.isVisible()),
                () -> assertTrue(bookForm.ratingFormItem.isVisible()),
                () -> assertTrue(bookForm.bookReviewFormItem.isVisible()),
                () -> assertFalse(bookForm.pagesReadFormItem.isVisible())
        );
    }

    @Test
    void correctFormFieldsShowForDidNotFinishShelf() {
        bookForm.predefinedShelfField.setValue(DID_NOT_FINISH);
        assertAll(
                () -> assertTrue(bookForm.dateStartedReadingFormItem.isVisible()),
                () -> assertFalse(bookForm.dateFinishedReadingFormItem.isVisible()),
                () -> assertFalse(bookForm.ratingFormItem.isVisible()),
                () -> assertFalse(bookForm.bookReviewFormItem.isVisible()),
                () -> assertTrue(bookForm.pagesReadFormItem.isVisible())
        );
    }

    private static Stream<Arguments> shelfNames() {
        return Stream.of(
                Arguments.of(TO_READ),
                Arguments.of(READING),
                Arguments.of(READ),
                Arguments.of(DID_NOT_FINISH)
        );
    }

    @ParameterizedTest
    @MethodSource("shelfNames")
    void fieldsToResetAreCorrectlyPopulated(
            PredefinedShelf.ShelfName newShelf) throws NotSupportedException {
        // given
        HasValue[] fieldsThatShouldBeReset = bookForm.getFieldsToReset(newShelf);
        populateBookForm();
        bookForm.pagesRead.setValue(pagesRead);

        // when
        bookForm.predefinedShelfField.setValue(newShelf);

        // then
        assertAll(
                () -> assertEquals(fieldsThatShouldBeReset.length, bookForm.fieldsToReset.length),
                () -> assertTrue(List.of(bookForm.fieldsToReset)
                                     .containsAll(List.of(fieldsThatShouldBeReset)))
        );
    }

    @Test
    void shouldNotAllowNegativeSeriesPosition() {
        // given
        bookForm.seriesPosition.setValue(-1);

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.SERIES_POSITION_ERROR,
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
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.PAGE_NUMBER_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void testNumberOfPagesFieldShouldNotExceedMaxValue() {
        // given
        bookForm.numberOfPages.setValue(Book.MAX_PAGES + 1);

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.MAX_PAGES_ERROR,
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
    }

    @Test
    void testPagesReadShouldNotExceedMaxValue() {
        // given
        bookForm.pagesRead.setValue(Book.MAX_PAGES + 1);

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();

        // then
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.MAX_PAGES_ERROR,
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
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.BOOK_TITLE_ERROR,
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
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.FIRST_NAME_ERROR,
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
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.LAST_NAME_ERROR,
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
        assertTrue(validationStatus.hasErrors());
        assertThat(validationStatus.getFieldValidationErrors().size()).isOne();
        assertEquals(BookFormErrors.SHELF_ERROR,
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
        assertTrue(validationStatus.hasErrors());
        assertEquals(2, validationStatus.getFieldValidationErrors().size());
        assertEquals(String.format(BookFormErrors.AFTER_TODAY_ERROR, "started"),
                validationStatus.getFieldValidationErrors()
                                .get(0)
                                .getMessage()
                                .orElseThrow()
        );
        assertEquals(BookFormErrors.FINISH_DATE_ERROR,
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
        assertAll(
                () -> assertTrue(bookForm.seriesPositionFormItem.isVisible()),
                () -> assertTrue(bookForm.seriesPosition.isVisible()),
                () -> assertEquals(SERIES_POSITION, bookForm.seriesPosition.getValue())
        );
    }

    @Test
    void whenIsInSeriesUnchecked_SeriesPositionShouldNotShow() {
        // given
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @Test
    void whenSeriesPositionIsSwitchedOnAndThenOff_seriesPositionHides() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        // when
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @Test
    void shouldDisplaySeriesPosition_withSeriesPositionPopulated_whenBookHasSeriesPosition() {
        // given
        populateBookForm();

        // when
        bookForm.openForm();

        // then
        assertAll(
                () -> assertTrue(bookForm.seriesPositionFormItem.isVisible()),
                () -> assertTrue(bookForm.seriesPosition.isVisible()),
                () -> assertEquals(SERIES_POSITION, bookForm.seriesPosition.getValue())
        );
    }

    @Test
    void shouldNotDisplaySeriesPosition_whenBookDoesNotHaveSeriesPosition() {
        // given
        bookForm = createBookForm(READ, false);

        bookForm.openForm();

        // then
        assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @Test
    void shouldAddBookToDatabaseWhenSaveEventIsCalled() {
        // given
        bookForm = createBookForm(TO_READ, false);

        // when
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();

        // then
        assertThat(bookService.count()).isOne();
        Book bookInDatabase = bookService.findAll().get(0);
        correctBookAttributesPresent(TO_READ, bookInDatabase);
    }

    @Test
    void shouldAddBooksToDatabaseWhenSaveEventIsCalled_withoutReplacingExistingBook() {
        // given
        bookForm = createBookForm(TO_READ, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();

        // when
        bookForm.setBook(createBook(TO_READ, false, "someOtherBook"));
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();

        // then
        List<Book> booksInDatabase = bookService.findAll();
        assertAll(
                () -> assertEquals(2, bookService.count()),
                () -> assertEquals(bookTitle, booksInDatabase.get(0).getTitle()),
                () -> assertEquals("someOtherBook", booksInDatabase.get(1).getTitle())
        );
    }

    @Test
    void shouldUpdateValuesInDatabaseForExistingBookWhenSaveEventIsCalled() {
        // given
        String newTitle = "IT";
        Author newAuthor = new Author("Stephen", "King");
        BookGenre newBookGenre = BookGenre.HORROR;

        bookForm = createBookForm(TO_READ, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();

        Book savedBook = bookService.findAll().get(0);
        populateBookFormWithExistingBook(READ, savedBook);

        // when
        bookForm.bookTitle.setValue(newTitle);
        bookForm.authorFirstName.setValue(newAuthor.getFirstName());
        bookForm.authorLastName.setValue(newAuthor.getLastName());
        bookForm.bookGenre.setValue(newBookGenre);
        bookForm.saveButton.click();

        // then
        assertThat(bookService.count()).isOne();

        Book updatedBook = bookService.findAll().get(0);
        assertAll(
                () -> assertEquals(newTitle, updatedBook.getTitle()),
                () -> assertEquals(READ, updatedBook.getPredefinedShelf().getPredefinedShelfName()),
                () -> assertEquals(newAuthor.getFirstName(), updatedBook.getAuthor().getFirstName()),
                () -> assertEquals(newAuthor.getLastName(), updatedBook.getAuthor().getLastName()),
                () -> assertEquals(newBookGenre, updatedBook.getBookGenre()),
                () -> assertEquals(dateStarted, updatedBook.getDateStartedReading()),
                () -> assertEquals(dateFinished, updatedBook.getDateFinishedReading())
        );
    }

    private static Stream<Arguments> shelfCombinations() {
        return Stream.of(
                Arguments.of(TO_READ, READING),
                Arguments.of(TO_READ, READ),
                Arguments.of(TO_READ, DID_NOT_FINISH),
                Arguments.of(READING, DID_NOT_FINISH),
                Arguments.of(READING, READ),
                Arguments.of(READING, TO_READ),
                Arguments.of(READ, TO_READ),
                Arguments.of(READ, DID_NOT_FINISH),
                Arguments.of(READ, READING),
                Arguments.of(DID_NOT_FINISH, TO_READ),
                Arguments.of(DID_NOT_FINISH, READING),
                Arguments.of(DID_NOT_FINISH, READ)
        );
    }

    /**
     * Tests whether a book is updated in the database when the save-event is called
     */
    @ParameterizedTest
    @MethodSource("shelfCombinations")
    void shouldUpdateValuesWhenBookIsMovedBetweenShelves(PredefinedShelf.ShelfName initialShelf,
                                                         PredefinedShelf.ShelfName newShelf) {
        // given
        bookForm = createBookForm(initialShelf, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.click();
        populateBookFormWithExistingBook(newShelf, bookService.findAll().get(0));

        // when
        bookForm.saveButton.click();

        // then
        assertThat(bookService.count()).isOne();
        Book bookInDatabase = bookService.findAll().get(0);
        correctBookAttributesPresent(newShelf, bookInDatabase);
    }

    private void correctBookAttributesPresent(PredefinedShelf.ShelfName shelfName, Book book) {
        testBookAttributesPresentForAllShelves(shelfName, book);
        bookAttributesPresentForSpecificPredefinedShelf(shelfName, book);
    }

    private void bookAttributesPresentForSpecificPredefinedShelf(
            PredefinedShelf.ShelfName shelfName,
            Book book) {
        switch (shelfName) {
            case TO_READ:
                assertStateSpecificFields(book, NULL_STARED_DATE, NULL_FINISHED_DATE,
                        RatingScale.NO_RATING, NO_BOOK_REVIEW, NO_PAGES_READ);
                break;
            case READING:
                assertStateSpecificFields(book, dateStarted, NULL_FINISHED_DATE,
                        RatingScale.NO_RATING, NO_BOOK_REVIEW, NO_PAGES_READ);
                break;
            case READ:
                assertStateSpecificFields(book, dateStarted, dateFinished, ratingVal,
                        bookReview, NO_PAGES_READ);
                break;
            case DID_NOT_FINISH:
                assertStateSpecificFields(book, dateStarted, NULL_FINISHED_DATE,
                        RatingScale.NO_RATING, NO_BOOK_REVIEW, pagesRead);
                break;
        }
    }

    private void testBookAttributesPresentForAllShelves(PredefinedShelf.ShelfName shelfName,
                                                        Book book) {
        assertAll(
                () -> assertEquals(bookTitle, book.getTitle()),
                () -> assertEquals(shelfName, book.getPredefinedShelf().getPredefinedShelfName()),
                () -> assertEquals(firstName, book.getAuthor().getFirstName()),
                () -> assertEquals(lastName, book.getAuthor().getLastName()),
                () -> assertEquals(BOOK_GENRE, book.getBookGenre()),
                () -> assertEquals(numberOfPages, book.getNumberOfPages())
        );
    }

    private void assertStateSpecificFields(Book book, LocalDate dateStarted, LocalDate dateFinished,
                                           RatingScale rating, String bookReview,
                                           Integer pagesRead) {
        assertAll(
                () -> assertEquals(dateStarted, book.getDateStartedReading()),
                () -> assertEquals(dateFinished, book.getDateFinishedReading()),
                () -> assertEquals(rating, book.getRating()),
                () -> assertEquals(bookReview, book.getBookReview()),
                () -> assertEquals(pagesRead, book.getPagesRead())
        );
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
        assertThat(bookService.count()).isOne();

        // when
        bookForm.addListener(BookForm.DeleteEvent.class,
                event -> bookService.delete(event.getBook()));
        bookForm.delete.click();

        // then
        assertThat(bookService.count()).isZero();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

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
import com.karankumar.bookproject.backend.entity.RatingScale;
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

import javax.transaction.NotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.TO_READ;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READING;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.DID_NOT_FINISH;

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
    private static final String bookReview = "Very good. Would read again.";
    private static final DoubleToRatingScaleConverter converter = new DoubleToRatingScaleConverter();
    private static final int SERIES_POSITION = 10;
    private static int pagesRead;
    private static int numberOfPages;
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

        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);

        Assumptions.assumeTrue(bookService != null);
        bookService.deleteAll();

        customShelfService.deleteAll();
        customShelfService.save(customShelf);

        Assumptions.assumeTrue(predefinedShelfService != null);
        bookForm = createBookForm(READ, true);
    }

    private BookForm createBookForm(PredefinedShelf.ShelfName shelf, boolean isInSeries) {
        BookForm bookForm = new BookForm(predefinedShelfService, customShelfService);
        readShelf = predefinedShelfUtils.findReadShelf();
        bookForm.setBook(createBook(shelf, isInSeries));
        return bookForm;
    }

    private Book createBook(PredefinedShelf.ShelfName shelfName, boolean isInSeries) {
        Author author = new Author(firstName, lastName);
        PredefinedShelf shelf = predefinedShelfUtils.findPredefinedShelf(shelfName);
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

    private int generateRandomNumberOfPages() {
        return ThreadLocalRandom.current().nextInt(300, (2000 + 1));
    }

    /**
     * Tests whether the form fields are correctly populated
     */
    @Test
    void formFieldsPopulated() {
        Assertions.assertEquals(bookTitle, bookForm.bookTitle.getField().getValue());
        Assertions.assertEquals(firstName, bookForm.authorFirstName.getField().getValue());
        Assertions.assertEquals(lastName, bookForm.authorLastName.getField().getValue());
        Assertions.assertEquals(readShelf.getPredefinedShelfName(),
                bookForm.predefinedShelfField.getValue());
        Assertions.assertEquals(genre, bookForm.bookGenre.getValue());
        Assertions.assertEquals(numberOfPages, bookForm.pageCount.getField().getValue());
        Assertions.assertEquals(dateStarted, bookForm.readingStartDate.getField().getValue());
        Assertions.assertEquals(dateFinished, bookForm.readingEndDate.getField().getValue());
        double rating = converter.convertToPresentation(ratingVal, null);
        Assertions.assertEquals(rating, bookForm.rating.getField().getValue());
        Assertions.assertEquals(bookReview, bookForm.bookReview.getField().getValue());
        Assertions.assertEquals(seriesPosition, bookForm.seriesPosition.getField().getValue());
    }

    enum EventType { SAVED, DELETED }

    /**
     * Tests whether the event is populated with the values from the form
     * @param eventType represents a saved event (when the user presses the save button) or a delete
     *                  event (when the user presses the delete event)
     */
    @ParameterizedTest
    @EnumSource(EventType.class)
    void saveEventPopulated(EventType eventType) {
        // given
        populateBookForm(READ, false);

        // when
        AtomicReference<Book> bookReference = new AtomicReference<>(null);
        if (eventType.equals(EventType.SAVED)) {
            bookForm
                .addListener(BookForm.SaveEvent.class, event -> bookReference.set(event.getBook()));
            bookForm.saveButton.getField().click();
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
        Assertions.assertEquals(bookReview, savedOrDeletedBook.getBookReview());
        Assertions.assertEquals(seriesPosition, savedOrDeletedBook.getSeriesPosition());
    }

    private void populateBookForm(PredefinedShelf.ShelfName shelfName, boolean isInSeries) {
        bookForm.authorFirstName.getField().setValue(firstName);
        bookForm.authorLastName.getField().setValue(lastName);
        bookForm.bookTitle.getField().setValue(bookTitle);
        bookForm.predefinedShelfField.setValue(readShelf.getPredefinedShelfName());
        bookForm.bookGenre.setValue(genre);
        bookForm.pageCount.getField().setValue(numberOfPages);
        if (isInSeries) {
            bookForm.seriesPosition.getField().setValue(SERIES_POSITION);
        }
        populateBookShelf(shelfName);
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
                bookForm.readingStartDate.getField().setValue(dateStarted);
                break;
            case READ:
                bookForm.predefinedShelfField.setValue(shelfName);
                bookForm.readingStartDate.getField().setValue(dateStarted);
                bookForm.readingEndDate.getField().setValue(dateFinished);
                bookForm.rating.getField().setValue(converter.convertToPresentation(ratingVal, null));
                bookForm.bookReview.getField().setValue(bookReview);
                break;
            case DID_NOT_FINISH:
                bookForm.predefinedShelfField.setValue(shelfName);
                bookForm.readingStartDate.getField().setValue(dateStarted);
                bookForm.pagesRead.getField().setValue(pagesRead);
                break;
        }
    }

    /**
     * Tests whether the reset button correctly clears all fields when clicked
     */
    @Test
    void formCanBeCleared() {
        // given
        populateBookForm(READ, false);
        assumeAllFormFieldsArePopulated();

        // when
        bookForm.reset.click();

        // then
        Assertions.assertTrue(bookForm.authorFirstName.getField().isEmpty());
        Assertions.assertTrue(bookForm.authorLastName.getField().isEmpty());
        Assertions.assertTrue(bookForm.bookTitle.getField().isEmpty());
        Assertions.assertTrue(bookForm.customShelfField.isEmpty());
        Assertions.assertTrue(bookForm.predefinedShelfField.isEmpty());
        Assertions.assertTrue(bookForm.bookGenre.isEmpty());
        Assertions.assertTrue(bookForm.pagesRead.getField().isEmpty());
        Assertions.assertTrue(bookForm.pageCount.getField().isEmpty());
        Assertions.assertTrue(bookForm.readingStartDate.getField().isEmpty());
        Assertions.assertTrue(bookForm.readingEndDate.getField().isEmpty());
        Assertions.assertTrue(bookForm.rating.getField().isEmpty());
        Assertions.assertTrue(bookForm.bookReview.getField().isEmpty());
    }

    private void assumeAllFormFieldsArePopulated() {
        Assumptions.assumeFalse(bookForm.authorFirstName.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.authorLastName.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.bookTitle.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.predefinedShelfField.isEmpty());
        Assumptions.assumeFalse(bookForm.bookGenre.isEmpty());
        Assumptions.assumeFalse(bookForm.pageCount.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.readingStartDate.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.readingEndDate.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.rating.getField().isEmpty());
        Assumptions.assumeFalse(bookForm.bookReview.getField().isEmpty());
    }

    @Test
    void correctFormFieldsShowForToReadShelf() {
        bookForm.predefinedShelfField.setValue(TO_READ);
        Assertions.assertFalse(bookForm.readingStartDate.isVisible());
        Assertions.assertFalse(bookForm.readingEndDate.isVisible());
        Assertions.assertFalse(bookForm.rating.isVisible());
        Assertions.assertFalse(bookForm.bookReview.isVisible());
        Assertions.assertFalse(bookForm.pagesRead.isVisible());
    }

    @Test
    void correctFormFieldsShowForReadingShelf() {
        bookForm.predefinedShelfField.setValue(READING);
        Assertions.assertTrue(bookForm.readingStartDate.isVisible());
        Assertions.assertFalse(bookForm.readingEndDate.isVisible());
        Assertions.assertFalse(bookForm.rating.isVisible());
        Assertions.assertFalse(bookForm.bookReview.isVisible());
        Assertions.assertFalse(bookForm.pagesRead.isVisible());
    }

    @Test
    void correctFormFieldsShowForReadShelf() {
        bookForm.predefinedShelfField.setValue(READ);
        Assertions.assertTrue(bookForm.readingStartDate.isVisible());
        Assertions.assertTrue(bookForm.readingEndDate.isVisible());
        Assertions.assertTrue(bookForm.rating.isVisible());
        Assertions.assertTrue(bookForm.bookReview.isVisible());
        Assertions.assertFalse(bookForm.pagesRead.isVisible());
    }

    @Test
    void correctFormFieldsShowForDidNotFinishShelf() {
        bookForm.predefinedShelfField.setValue(DID_NOT_FINISH);
        Assertions.assertTrue(bookForm.readingStartDate.isVisible());
        Assertions.assertFalse(bookForm.readingEndDate.isVisible());
        Assertions.assertFalse(bookForm.rating.isVisible());
        Assertions.assertFalse(bookForm.bookReview.isVisible());
        Assertions.assertTrue(bookForm.pagesRead.isVisible());
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
    void fieldsToResetAreCorrectlyPopulated(PredefinedShelf.ShelfName newShelf) throws NotSupportedException {
        // given
        HasValue[] fieldsThatShouldBeReset = bookForm.getFieldsToReset(newShelf);
        populateBookForm(READ, false);
        bookForm.pagesRead.getField().setValue(pagesRead);

        // when
        bookForm.predefinedShelfField.setValue(newShelf);

        // then
        Assertions.assertEquals(fieldsThatShouldBeReset.length, bookForm.fieldsToReset.length);
        Assertions.assertTrue(List.of(bookForm.fieldsToReset)
                                  .containsAll(List.of(fieldsThatShouldBeReset)));
    }

    @Test
    void shouldNotAllowNegativeSeriesPosition() {
        // given
        bookForm.seriesPosition.getField().setValue(-1);

        // when
        bookForm.saveButton.getField().click();
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
        bookForm.pageCount.getField().setValue(-1);

        // when
        bookForm.saveButton.getField().click();
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
        bookForm.bookTitle.getField().setValue("");

        // when
        bookForm.saveButton.getField().click();
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
        bookForm.authorFirstName.getField().setValue("");

        // when
        bookForm.saveButton.getField().click();
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
        bookForm.authorLastName.getField().setValue("");

        // when
        bookForm.saveButton.getField().click();
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
        bookForm.saveButton.getField().click();
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
        bookForm.readingStartDate.getField().setValue(LocalDate.now().plusDays(5));

        // when
        bookForm.saveButton.getField().click();
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
        bookForm.inSeries.setValue(true);

        // then
        Assertions.assertTrue(bookForm.seriesPosition.isVisible());
        Assertions.assertTrue(bookForm.seriesPosition.getField().isVisible());
        Assertions.assertEquals(SERIES_POSITION, bookForm.seriesPosition.getField().getValue());
    }

    @Test
    void whenIsInSeriesUnchecked_SeriesPositionShouldNotShow() {
        // given
        bookForm.inSeries.setValue(false);

        // then
        Assertions.assertFalse(bookForm.seriesPosition.isVisible());
    }

    @Test
    void whenSeriesPositionIsSwitchedOnAndThenOff_seriesPositionHides() {
        // given
        bookForm.inSeries.setValue(true);

        // when
        bookForm.inSeries.setValue(false);

        // then
        Assertions.assertFalse(bookForm.seriesPosition.isVisible());
    }

    @Test
    void shouldDisplaySeriesPosition_withSeriesPositionPopulated_whenBookHasSeriesPosition() {
        // given
        populateBookForm(READ, false);

        // when
        bookForm.openForm();

        // then
        Assertions.assertTrue(bookForm.seriesPosition.isVisible());
        Assertions.assertTrue(bookForm.seriesPosition.getField().isVisible());
        Assertions.assertEquals(SERIES_POSITION, bookForm.seriesPosition.getField().getValue());
    }

    @Test
    void shouldNotDisplaySeriesPosition_whenBookDoesNotHaveSeriesPosition() {
        // given
        bookForm = createBookForm(READ, false);

        bookForm.openForm();

        // then
        Assertions.assertFalse(bookForm.seriesPosition.isVisible());
    }

    @Test
    void shouldAddBookToDatabaseWhenSaveEventIsCalled() {
        // given
        bookForm = createBookForm(TO_READ, false);

        // when
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.getField().click();

        // then
        Assertions.assertEquals(1, bookService.count());
        Book bookInDatabase = bookService.findAll().get(0);
        correctBookAttributesPresent(TO_READ, bookInDatabase);
    }

    @Test
    void shouldUpdateValuesInDatabaseForExistingBookWhenSaveEventIsCalled() {
        // given
        String newTitle = "IT";
        Author newAuthor = new Author("Stephen", "King");
        Genre newGenre = Genre.HORROR;

        bookForm = createBookForm(TO_READ, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.getField().click();

        Book savedBook = bookService.findAll().get(0);
        populateBookFormWithExistingBook(READ, savedBook);

        // when
        bookForm.bookTitle.getField().setValue(newTitle);
        bookForm.authorFirstName.getField().setValue(newAuthor.getFirstName());
        bookForm.authorLastName.getField().setValue(newAuthor.getLastName());
        bookForm.bookGenre.setValue(newGenre);
        bookForm.saveButton.getField().click();

        // then
        Assertions.assertEquals(1, bookService.count());
        Book updatedBook = bookService.findAll().get(0);
        Assertions.assertEquals(newTitle, updatedBook.getTitle());
        Assertions.assertEquals(READ, updatedBook.getPredefinedShelf().getPredefinedShelfName());
        Assertions.assertEquals(newAuthor.getFirstName(), updatedBook.getAuthor().getFirstName());
        Assertions.assertEquals(newAuthor.getLastName(), updatedBook.getAuthor().getLastName());
        Assertions.assertEquals(newGenre, updatedBook.getGenre());
        Assertions.assertEquals(dateStarted, updatedBook.getDateStartedReading());
        Assertions.assertEquals(dateFinished, updatedBook.getDateFinishedReading());
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
        bookForm.saveButton.getField().click();
        populateBookFormWithExistingBook(newShelf, bookService.findAll().get(0));

        // when
        bookForm.saveButton.getField().click();

        // then
        Assertions.assertEquals(1, bookService.count());
        Book bookInDatabase = bookService.findAll().get(0);
        correctBookAttributesPresent(newShelf, bookInDatabase);
    }

    private void correctBookAttributesPresent(PredefinedShelf.ShelfName shelfName, Book book) {
        testBookAttributesPresentForAllShelves(shelfName, book);
        bookAttributesPresentForSpecificPredefinedShelf(shelfName, book);
    }

    private void bookAttributesPresentForSpecificPredefinedShelf(PredefinedShelf.ShelfName shelfName,
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
        Assertions.assertEquals(bookTitle, book.getTitle());
        Assertions.assertEquals(shelfName, book.getPredefinedShelf().getPredefinedShelfName());
        Assertions.assertEquals(firstName, book.getAuthor().getFirstName());
        Assertions.assertEquals(lastName, book.getAuthor().getLastName());
        Assertions.assertEquals(genre, book.getGenre());
        Assertions.assertEquals(numberOfPages, book.getNumberOfPages());
    }

    private void assertStateSpecificFields(Book book, LocalDate dateStarted, LocalDate dateFinished,
                                           RatingScale rating, String bookReview, Integer pagesRead) {
        Assertions.assertEquals(dateStarted, book.getDateStartedReading());
        Assertions.assertEquals(dateFinished, book.getDateFinishedReading());
        Assertions.assertEquals(rating, book.getRating());
        Assertions.assertEquals(bookReview, book.getBookReview());
        Assertions.assertEquals(pagesRead, book.getPagesRead());
    }

    /**
     * Tests whether a book is removed from the database when the delete-event is called
     */
    @Test
    void shouldDeleteBookFromDatabase() {
        // given
        bookForm = createBookForm(TO_READ, false);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookService.save(event.getBook()));
        bookForm.saveButton.getField().click();
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

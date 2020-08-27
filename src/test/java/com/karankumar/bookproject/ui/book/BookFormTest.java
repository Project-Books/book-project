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
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
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
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

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
    private static DoubleToRatingScaleConverter converter = new DoubleToRatingScaleConverter();
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
        bookForm = createBookForm(true);

    }

    private BookForm createBookForm(boolean isInSeries) {
        BookForm bookForm = new BookForm(predefinedShelfService, customShelfService);
        bookForm.setBook(createBook(predefinedShelfService, isInSeries));
        return bookForm;

    }

    private Book createBook(PredefinedShelfService predefinedShelfService, boolean isInSeries) {
        Author author = new Author(firstName, lastName);
        readShelf = predefinedShelfService.findAll().get(2);
        Book book = new Book(bookTitle, author, readShelf);


        pagesRead = generateRandomNumberOfPages();
        numberOfPages = generateRandomNumberOfPages();
        seriesPosition = SERIES_POSITION;

        book.setCustomShelf(customShelf);
        book.setGenre(genre);
        book.setPagesRead(pagesRead);
        book.setNumberOfPages(numberOfPages);
        book.setDateStartedReading(dateStarted);
        book.setDateFinishedReading(dateFinished);
        book.setRating(ratingVal);
        book.setBookReview(bookReview);
        if (isInSeries) {
            book.setSeriesPosition(seriesPosition);
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
        Assertions.assertEquals(pagesRead, bookForm.pagesRead.getValue());
        Assertions.assertEquals(numberOfPages, bookForm.numberOfPages.getValue());
        Assertions.assertEquals(dateStarted, bookForm.dateStartedReading.getValue());
        Assertions.assertEquals(dateFinished, bookForm.dateFinishedReading.getValue());
        double rating = converter.convertToPresentation(ratingVal, null);
        Assertions.assertEquals(rating, bookForm.rating.getValue());
        Assertions.assertEquals(bookReview, bookForm.bookReview.getValue());
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
        populateBookForm();

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
        Assertions.assertEquals(pagesRead, savedOrDeletedBook.getPagesRead());
        Assertions.assertEquals(numberOfPages, savedOrDeletedBook.getNumberOfPages());
        Assertions.assertEquals(dateStarted, savedOrDeletedBook.getDateStartedReading());
        Assertions.assertEquals(dateFinished, savedOrDeletedBook.getDateFinishedReading());
        Assertions.assertEquals(ratingVal, savedOrDeletedBook.getRating());
        Assertions.assertEquals(bookReview, savedOrDeletedBook.getBookReview());
        Assertions.assertEquals(seriesPosition, savedOrDeletedBook.getSeriesPosition());
    }

    private void populateBookForm() {
        bookForm.authorFirstName.setValue(firstName);
        bookForm.authorLastName.setValue(lastName);
        bookForm.bookTitle.setValue(bookTitle);
        bookForm.predefinedShelfField.setValue(readShelf.getPredefinedShelfName());
        bookForm.bookGenre.setValue(genre);
        bookForm.pagesRead.setValue(pagesRead);
        bookForm.numberOfPages.setValue(numberOfPages);
        bookForm.dateStartedReading.setValue(dateStarted);
        bookForm.dateFinishedReading.setValue(dateFinished);
        bookForm.rating.setValue(converter.convertToPresentation(ratingVal, null));
        bookForm.bookReview.setValue(bookReview);
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
        Assertions.assertTrue(bookForm.bookReview.isEmpty());
    }

    private void assumeAllFormFieldsArePopulated() {
        Assumptions.assumeFalse(bookForm.authorFirstName.isEmpty());
        Assumptions.assumeFalse(bookForm.authorLastName.isEmpty());
        Assumptions.assumeFalse(bookForm.bookTitle.isEmpty());
        Assumptions.assumeFalse(bookForm.predefinedShelfField.isEmpty());
        Assumptions.assumeFalse(bookForm.bookGenre.isEmpty());
        Assumptions.assumeFalse(bookForm.pagesRead.isEmpty());
        Assumptions.assumeFalse(bookForm.numberOfPages.isEmpty());
        Assumptions.assumeFalse(bookForm.dateStartedReading.isEmpty());
        Assumptions.assumeFalse(bookForm.dateFinishedReading.isEmpty());
        Assumptions.assumeFalse(bookForm.rating.isEmpty());
        Assumptions.assumeFalse(bookForm.bookReview.isEmpty());
    }

    @Test
    void correctFormFieldsShowForToReadShelf() {
        bookForm.predefinedShelfField.setValue(PredefinedShelf.ShelfName.TO_READ);
        Assertions.assertFalse(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.ratingFormItem.isVisible());
        Assertions.assertFalse(bookForm.bookReviewFormItem.isVisible());
        Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void correctFormFieldsShowForReadingShelf() {
        bookForm.predefinedShelfField.setValue(PredefinedShelf.ShelfName.READING);
        Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.ratingFormItem.isVisible());
        Assertions.assertFalse(bookForm.bookReviewFormItem.isVisible());
        Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void correctFormFieldsShowForReadShelf() {
        bookForm.predefinedShelfField.setValue(PredefinedShelf.ShelfName.READ);
        Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertTrue(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertTrue(bookForm.ratingFormItem.isVisible());
        Assertions.assertTrue(bookForm.bookReviewFormItem.isVisible());
        Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible());
    }

    @Test
    void correctFormFieldsShowForDidNotFinishShelf() {
        bookForm.predefinedShelfField.setValue(PredefinedShelf.ShelfName.DID_NOT_FINISH);
        Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible());
        Assertions.assertFalse(bookForm.ratingFormItem.isVisible());
        Assertions.assertFalse(bookForm.bookReviewFormItem.isVisible());
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
        populateBookForm();

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
        bookForm = createBookForm(false);

        bookForm.openForm();

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

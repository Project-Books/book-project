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

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.annotations.IntegrationTest;
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
public class BookFormTests {

    private static final String firstName = "Nick";
    private static final String lastName = "Bostrom";
    private static final String bookTitle = "Superintelligence: Paths, Dangers, Strategies";
    private static final Genre genre = Genre.SCIENCE;
    private static final LocalDate dateStarted = LocalDate.now().minusDays(4);
    private static final LocalDate dateFinished = LocalDate.now();

    // if either the RatingScale or the double rating is changed, then the other has to also be changed accordingly
    private static final RatingScale ratingVal = RatingScale.NINE;
    private static final int SERIES_POSITION = 10;
    private static double rating = 9.0;

    private static int pagesRead;
    private static int numberOfPages;
    private static int seriesPosition;
    private static Routes routes;
    private static PredefinedShelf readShelf;
    private static BookForm bookForm;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private BookService bookService;

    @Autowired
    private PredefinedShelfService shelfService;

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

        Assumptions.assumeTrue(shelfService != null);
        bookForm = createBookForm(true);
    }

    private BookForm createBookForm(boolean isInSeries) {
        BookForm bookForm = new BookForm(shelfService);
        bookForm.setBook(createBook(shelfService, isInSeries));
        return bookForm;

    }

    private Book createBook(PredefinedShelfService predefinedShelfService, boolean isInSeries) {
        Author author = new Author(firstName, lastName);
        Book book = new Book(bookTitle, author);

        readShelf = predefinedShelfService.findAll().get(2);

        pagesRead = generateRandomNumberOfPages();
        numberOfPages = generateRandomNumberOfPages();
        seriesPosition = SERIES_POSITION;

        book.setShelf(readShelf);
        book.setGenre(genre);
        book.setPagesRead(pagesRead);
        book.setNumberOfPages(numberOfPages);
        book.setDateStartedReading(dateStarted);
        book.setDateFinishedReading(dateFinished);
        book.setRating(ratingVal);
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
    public void formFieldsPopulated() {
        Assertions.assertEquals(bookTitle, bookForm.bookTitle.getValue());
        Assertions.assertEquals(firstName, bookForm.authorFirstName.getValue());
        Assertions.assertEquals(lastName, bookForm.authorLastName.getValue());
        Assertions.assertEquals(readShelf.getPredefinedShelfName(), bookForm.shelf.getValue());
        Assertions.assertEquals(genre, bookForm.bookGenre.getValue());
        Assertions.assertEquals(pagesRead, bookForm.pagesRead.getValue());
        Assertions.assertEquals(numberOfPages, bookForm.numberOfPages.getValue());
        Assertions.assertEquals(dateStarted, bookForm.dateStartedReading.getValue());
        Assertions.assertEquals(dateFinished, bookForm.dateFinishedReading.getValue());
        Assertions.assertEquals(rating, bookForm.rating.getValue());
        Assertions.assertEquals(seriesPosition, bookForm.seriesPosition.getValue());
    }

    enum EventType {
        SAVED,
        DELETED
    }

    /**
     * Tests whether the event is populated with the values from the form
     * @param eventType represents a saved event (when the user presses the save button) or a delete event (when the
     *                  user presses the delete event)
     */
    @ParameterizedTest
    @EnumSource(EventType.class)
    public void saveEventPopulated(EventType eventType) {
        populateBookForm();

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

        Assertions.assertEquals(bookTitle, savedOrDeletedBook.getTitle());
        Assertions.assertEquals(firstName, savedOrDeletedBook.getAuthor().getFirstName());
        Assertions.assertEquals(lastName, savedOrDeletedBook.getAuthor().getLastName());
        Assertions.assertEquals(readShelf.getShelfName(), savedOrDeletedBook.getShelf().getShelfName());
        Assertions.assertEquals(genre, savedOrDeletedBook.getGenre());
        Assertions.assertEquals(pagesRead, savedOrDeletedBook.getPagesRead());
        Assertions.assertEquals(numberOfPages, savedOrDeletedBook.getNumberOfPages());
        Assertions.assertEquals(dateStarted, savedOrDeletedBook.getDateStartedReading());
        Assertions.assertEquals(dateFinished, savedOrDeletedBook.getDateFinishedReading());
        Assertions.assertEquals(ratingVal, savedOrDeletedBook.getRating());
        Assertions.assertEquals(seriesPosition, savedOrDeletedBook.getSeriesPosition());
    }

    private void populateBookForm() {
        bookForm.authorFirstName.setValue(firstName);
        bookForm.authorLastName.setValue(lastName);
        bookForm.bookTitle.setValue(bookTitle);
        bookForm.shelf.setValue(readShelf.getPredefinedShelfName());
        bookForm.bookGenre.setValue(genre);
        bookForm.pagesRead.setValue(pagesRead);
        bookForm.numberOfPages.setValue(numberOfPages);
        bookForm.dateStartedReading.setValue(dateStarted);
        bookForm.dateFinishedReading.setValue(dateFinished);
        bookForm.rating.setValue(rating);
    }

    /**
     * Tests whether the reset button correctly clears all fields when clicked
     */
    @Test
    public void formCanBeCleared() {
        populateBookForm();
        // Assume that all form fields are non empty
        Assumptions.assumeFalse(bookForm.authorFirstName.isEmpty(), "Author first name not populated");
        Assumptions.assumeFalse(bookForm.authorLastName.isEmpty(), "Author last name not populated");
        Assumptions.assumeFalse(bookForm.bookTitle.isEmpty(), "Book title not populated");
        Assumptions.assumeFalse(bookForm.shelf.isEmpty(), "Shelf not populated");
        Assumptions.assumeFalse(bookForm.bookGenre.isEmpty(), "Book genre not populated");
        Assumptions.assumeFalse(bookForm.pagesRead.isEmpty(), "Pages read not populated");
        Assumptions.assumeFalse(bookForm.numberOfPages.isEmpty(), "Number of pages not populated");
        Assumptions.assumeFalse(bookForm.dateStartedReading.isEmpty(), "Date started populated");
        Assumptions.assumeFalse(bookForm.dateFinishedReading.isEmpty(), "Date finished populated");

        bookForm.reset.click();

        Assertions.assertTrue(bookForm.authorFirstName.isEmpty(), "Author first name not cleared");
        Assertions.assertTrue(bookForm.authorLastName.isEmpty(), "Author last name not cleared");
        Assertions.assertTrue(bookForm.bookTitle.isEmpty(), "Book title not cleared");
        Assertions.assertTrue(bookForm.shelf.isEmpty(), "Shelf not cleared");
        Assertions.assertTrue(bookForm.bookGenre.isEmpty(), "Book genre not cleared");
        Assertions.assertTrue(bookForm.pagesRead.isEmpty(), "Pages read not cleared");
        Assertions.assertTrue(bookForm.numberOfPages.isEmpty(), "Number of pages not cleared");
        Assertions.assertTrue(bookForm.dateStartedReading.isEmpty(), "Date started not cleared");
        Assertions.assertTrue(bookForm.dateFinishedReading.isEmpty(), "Date finished not cleared");
        Assertions.assertTrue(bookForm.rating.isEmpty(), "Rating not cleared");
    }

    @ParameterizedTest
    @EnumSource(PredefinedShelf.ShelfName.class)
    public void correctFormFieldsShownForGivenShelf(PredefinedShelf.ShelfName shelfName) {
        populateBookForm();
        bookForm.shelf.setValue(shelfName); // shelf chosen may have changed

        boolean shouldShowStarted = false;
        boolean shouldShowFinished = false;
        boolean shouldShowRating = false;
        boolean shouldShowPagesRead = false;

        switch (shelfName) {
            case TO_READ:
                break; // all fields already set to false
            case READING:
                shouldShowStarted = true;
                break;
            case DID_NOT_FINISH:
                shouldShowStarted = true;
                shouldShowPagesRead = true;
                // finished and rating already set to false
                break;
            case READ:
                shouldShowStarted = true;
                shouldShowFinished = true;
                shouldShowRating = true;
                shouldShowPagesRead = false;
                break;
        }

        final String dateStarted = "Date started ";
        final String dateFinished = "Date finished ";
        final String rating = "Rating ";
        final String pagesRead = "Pages read ";
        final String shown = String.format("shown for a book in the %s shelf", shelfName);
        final String notShown = String.format("not shown for a book in the %s shelf", shelfName);
        if (shouldShowStarted) {
            Assertions.assertTrue(bookForm.dateStartedReadingFormItem.isVisible(), dateStarted + notShown);
        } else {
            Assertions.assertFalse(bookForm.dateStartedReadingFormItem.isVisible(), dateStarted + shown);
        }

        if (shouldShowFinished) {
            Assertions.assertTrue(bookForm.dateFinishedReadingFormItem.isVisible(), dateFinished + notShown);
        } else {
            Assertions.assertFalse(bookForm.dateFinishedReadingFormItem.isVisible(), dateFinished + shown);
        }

        if (shouldShowRating) {
            Assertions.assertTrue(bookForm.ratingFormItem.isVisible(), rating + notShown);
        } else {
            Assertions.assertFalse(bookForm.ratingFormItem.isVisible(), rating + shown);
        }

        if (shouldShowPagesRead) {
            Assertions.assertTrue(bookForm.pagesReadFormItem.isVisible(), pagesRead + notShown);
        } else {
            Assertions.assertFalse(bookForm.pagesReadFormItem.isVisible(), pagesRead + shown);
        }
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
        bookForm.shelf.setValue(null);

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
    void shouldDisplaySeriesPositionForm_whenIsInSeriesCheckboxOn() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        // then
        Assertions.assertTrue(bookForm.seriesPositionFormItem.isVisible());
        Assertions.assertTrue(bookForm.seriesPosition.isVisible());
        Assertions.assertEquals(SERIES_POSITION, bookForm.seriesPosition.getValue());
    }

    @Test
    void shouldNotDisplaySeriesPositionForm_whenIsInSeriesCheckboxOff() {
        // given
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
    }

    @Test
    void shouldClearSeriesPositionValue_whenSeriesPositionCheckboxIsFirstEnabled_andThenDisabled() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        // when
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
        Assertions.assertNull(bookForm.seriesPosition.getValue());
    }

    @Test
    void shouldDisplaySeriesPositionForm_withSeriesPositionPopulated_whenBookHasSeriesPosition() {
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
    void shouldNotDisplaySeriesPositionForm_withSeriesPositionPopulated_whenBookDoesNotHaveSeriesPosition() {
        // given
        bookForm = createBookForm(false);

        // when
        bookForm.openForm();

        // then
        Assertions.assertFalse(bookForm.seriesPositionFormItem.isVisible());
        Assertions.assertNull(bookForm.seriesPosition.getValue());
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

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
import com.karankumar.bookproject.backend.entity.BookGenre;	
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import static com.karankumar.bookproject.ui.book.BookFormErrors.MAX_PAGES_ERROR;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.spring.SpringServlet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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


@IntegrationTest
@WebAppConfiguration
@DisplayName("BookForm should")
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

    private static final LocalDate NULL_STARED_DATE = null;
    private static final LocalDate NULL_FINISHED_DATE = null;
    private static final Integer NO_PAGES_READ = null;
    private static final String NO_BOOK_REVIEW = "";

    private static Routes routes;
    private static PredefinedShelf readShelf;
    private static BookForm bookForm;

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

        bookService.deleteAll();
        customShelfService.deleteAll();
        customShelfService.save(customShelfService.createCustomShelf("BookFormTestShelf"));

        bookForm = createBookForm(READ, true);
    }

    private BookForm createBookForm(PredefinedShelf.ShelfName shelf, boolean isInSeries) {
        BookForm bookForm = new BookForm(predefinedShelfService, customShelfService);
        readShelf = predefinedShelfService.findReadShelf();
        bookForm.setBook(createBook(shelf, isInSeries, bookTitle));
        return bookForm;
    }

    private Book createBook(PredefinedShelf.ShelfName shelfName, boolean isInSeries,
                            String bookTitle) {
        Author author = new Author(firstName, lastName);
        PredefinedShelf shelf = predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(shelfName);
        Book book = new Book(bookTitle, author, shelf);

        seriesPosition = SERIES_POSITION;

        book.setBookGenre(BOOK_GENRE);
        book.setNumberOfPages(numberOfPages);
        book.setCustomShelf(customShelfService.findAllForLoggedInUser().get(0));
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
    void populateFormFields() {
        double rating = RatingScale.toDouble(ratingVal);

        assertSoftly(softly -> {
            softly.assertThat(bookTitle).isEqualTo(bookForm.bookTitle.getValue());
            softly.assertThat(firstName).isEqualTo(bookForm.authorFirstName.getValue());
            softly.assertThat(lastName).isEqualTo(bookForm.authorLastName.getValue());
            softly.assertThat(readShelf.getPredefinedShelfName())
                  .isEqualTo(bookForm.predefinedShelfField.getValue());
            softly.assertThat(BOOK_GENRE).isEqualTo(bookForm.bookGenre.getValue());
            softly.assertThat(numberOfPages).isEqualTo(bookForm.numberOfPages.getValue());
            softly.assertThat(dateStarted).isEqualTo(bookForm.dateStartedReading.getValue());
            softly.assertThat(dateFinished).isEqualTo(bookForm.dateFinishedReading.getValue());
            softly.assertThat(rating).isEqualTo(bookForm.rating.getValue());
            softly.assertThat(bookReview).isEqualTo(bookForm.bookReview.getValue());
            softly.assertThat(seriesPosition).isEqualTo(bookForm.seriesPosition.getValue());
        });
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
    void populateSaveEvent(EventType eventType) {
        // given
        populateBookForm();

        // when
        AtomicReference<Book> bookReference = new AtomicReference<>(null);
        if (eventType.equals(EventType.SAVED)) {
            bookForm.addListener(BookForm.SaveEvent.class,
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

    @Test
    void clearFormOnResetButton() {
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
            assertThat(field.isEmpty()).isTrue();
        }
    }

    private void assumeAllFormFieldsArePopulated() {
        assumeThat(bookForm.authorFirstName.isEmpty()).isFalse();
        assumeThat(bookForm.authorLastName.isEmpty()).isFalse();
        assumeThat(bookForm.bookTitle.isEmpty()).isFalse();
        assumeThat(bookForm.predefinedShelfField.isEmpty()).isFalse();
        assumeThat(bookForm.bookGenre.isEmpty()).isFalse();
        assumeThat(bookForm.numberOfPages.isEmpty()).isFalse();
        assumeThat(bookForm.dateStartedReading.isEmpty()).isFalse();
        assumeThat(bookForm.dateFinishedReading.isEmpty()).isFalse();
        assumeThat(bookForm.rating.isEmpty()).isFalse();
        assumeThat(bookForm.bookReview.isEmpty()).isFalse();
    }

    @Test
    void showCorrectFormFieldsForToReadShelf() {
        bookForm.predefinedShelfField.setValue(TO_READ);
        assertNonToReadFieldsAreHidden();
    }

    private void assertNonToReadFieldsAreHidden() {
        assertSoftly(softly -> {
            softly.assertThat(bookForm.dateStartedReadingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.dateFinishedReadingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.ratingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.bookReviewFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.pagesReadFormItem.isVisible()).isFalse();
        });
    }

    @Test
    void showCorrectFormFieldsForReadingShelf() {
        bookForm.predefinedShelfField.setValue(READING);
        assertSoftly(softly -> {
            softly.assertThat(bookForm.dateStartedReadingFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.dateFinishedReadingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.ratingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.bookReviewFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.pagesReadFormItem.isVisible()).isFalse();
        });
    }

    @Test
    void showCorrectFormFieldsForReadShelf() {
        bookForm.predefinedShelfField.setValue(READ);
        assertSoftly(softly -> {
            softly.assertThat(bookForm.dateStartedReadingFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.dateFinishedReadingFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.ratingFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.bookReviewFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.pagesReadFormItem.isVisible()).isFalse();
        });
    }

    @Test
    void showCorrectFormFieldsForDidNotFinishShelf() {
        bookForm.predefinedShelfField.setValue(DID_NOT_FINISH);
        assertSoftly(softly -> {
            softly.assertThat(bookForm.dateStartedReadingFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.dateFinishedReadingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.ratingFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.bookReviewFormItem.isVisible()).isFalse();
            softly.assertThat(bookForm.pagesReadFormItem.isVisible()).isTrue();
        });
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
    void populateFieldsToResetCorrectly(
            PredefinedShelf.ShelfName newShelf) throws NotSupportedException {
        // given
        HasValue[] fieldsThatShouldBeReset = bookForm.getFieldsToReset(newShelf);
        populateBookForm();
        bookForm.pagesRead.setValue(pagesRead);

        // when
        bookForm.predefinedShelfField.setValue(newShelf);

        // then
        assertSoftly(softly -> {
            softly.assertThat(fieldsThatShouldBeReset).hasSameSizeAs(bookForm.fieldsToReset);
            softly.assertThat(bookForm.fieldsToReset).containsAll(List.of(fieldsThatShouldBeReset));
        });
    }

    @Test
    void notAllowNegativeSeriesPosition() {
        // given
        bookForm.seriesPosition.setValue(-1);

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(BookFormErrors.SERIES_POSITION_ERROR);
    }

    private void assertErrorShown(String errorMessage) {
        // when
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();
        List<BindingValidationStatus<?>> fieldValidationErrors =
                validationStatus.getFieldValidationErrors();

        // then
        assertSoftly(softly -> {
            softly.assertThat(validationStatus.hasErrors()).isTrue();
            softly.assertThat(fieldValidationErrors.size()).isOne();
            softly.assertThat(fieldValidationErrors.get(0).getMessage().orElseThrow())
                  .isEqualTo(errorMessage);
        });
    }

    @Test
    void notAllowNegativePageNumbers() {
        // given
        bookForm.numberOfPages.setValue(-1);

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(BookFormErrors.PAGE_NUMBER_ERROR);
    }

    @Test
    void notAllowPagesFieldToExceedMax() {
        // given
        bookForm.numberOfPages.setValue(Book.MAX_PAGES + 1);

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(MAX_PAGES_ERROR);
    }

    @Test
    void notAllowPagesReadToExceedMaxValue() {
        // given
        bookForm.pagesRead.setValue(Book.MAX_PAGES + 1);

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(MAX_PAGES_ERROR);
    }

    @Test
    void notAllowEmptyBookTitle() {
        // given
        bookForm.bookTitle.setValue("");

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(BookFormErrors.BOOK_TITLE_ERROR);
    }

    @Test
    void notAllowEmptyAuthorFirstName() {
        // given
        bookForm.authorFirstName.setValue("");

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(BookFormErrors.FIRST_NAME_ERROR);
    }

    @Test
    void saveChangedAuthorName() {
        // given
        Book book = createBook(READ, false, "Title");
        bookService.save(book);
        bookForm.setBook(book);

        AtomicReference<Book> bookReference = new AtomicReference<>(null);
        bookForm.addListener(BookForm.SaveEvent.class, event -> bookReference.set(event.getBook()));

        // when
        bookForm.authorFirstName.setValue("James");
        bookForm.authorLastName.setValue("Dean");
        bookForm.saveButton.click();

        // then
        Book savedBook = bookReference.get();

        assertThat(savedBook.getId()).isEqualTo(book.getId()); // Still the same book
        assertThat(savedBook.getAuthor().getFirstName()).isEqualTo("James"); // Author name changed
        assertThat(savedBook.getAuthor().getLastName()).isEqualTo("Dean");
    }

    @Test
    void notAllowEmptyAuthorLastName() {
        // given
        bookForm.authorLastName.setValue("");

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(BookFormErrors.LAST_NAME_ERROR);
    }

    @Test
    void notAllowEmptyShelf() {
        // given
        bookForm.predefinedShelfField.setValue(null);

        // when
        bookForm.saveButton.click();

        // then
        assertErrorShown(BookFormErrors.SHELF_ERROR);
    }

    @Test
    void notAllowFutureStartDate() {
        // given
        bookForm.dateStartedReading.setValue(LocalDate.now().plusDays(5));

        // when
        bookForm.saveButton.click();
        BinderValidationStatus<Book> validationStatus = bookForm.binder.validate();
        List<BindingValidationStatus<?>> fieldValidationErrors =
                validationStatus.getFieldValidationErrors();

        // then
        assertSoftly(softly -> {
            softly.assertThat(validationStatus.hasErrors()).isTrue();
            softly.assertThat(validationStatus.getFieldValidationErrors()).hasSize(2);
            softly.assertThat(fieldValidationErrors.get(0).getMessage().orElseThrow())
                  .isEqualTo(String.format(BookFormErrors.AFTER_TODAY_ERROR, "started"));
            softly.assertThat(fieldValidationErrors.get(1).getMessage().orElseThrow())
                  .isEqualTo(BookFormErrors.FINISH_DATE_ERROR);
        });
    }

    @Test
    void displaySeriesPositionWhenIsInSeriesChecked() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        assertSoftly(softly -> {
            softly.assertThat(bookForm.seriesPositionFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.seriesPosition.isVisible()).isTrue();
            softly.assertThat(SERIES_POSITION).isEqualTo(bookForm.seriesPosition.getValue());
            softly.assertAll();
        });

    }

    @Test
    void notShowSeriesPositionWhenIsInSeriesUnchecked() {
        // given
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        assertThat(bookForm.seriesPositionFormItem.isVisible()).isFalse();
    }

    @Test
    void hideSeriesPositionWhenSeriesPositionIsSwitchedOnAndThenOff() {
        // given
        bookForm.inSeriesCheckbox.setValue(true);

        // when
        bookForm.inSeriesCheckbox.setValue(false);

        // then
        assertThat(bookForm.seriesPositionFormItem.isVisible()).isFalse();
    }

    @Test
    void displaySeriesPosition_withSeriesPositionPopulated_whenBookHasSeriesPosition() {
        // given
        populateBookForm();

        // when
        bookForm.openForm();

        // then
        assertSoftly(softly -> {
            softly.assertThat(bookForm.seriesPositionFormItem.isVisible()).isTrue();
            softly.assertThat(bookForm.seriesPosition.isVisible()).isTrue();
            softly.assertThat(SERIES_POSITION).isEqualTo(bookForm.seriesPosition.getValue());
        });
    }

    @Test
    void notDisplaySeriesPosition_whenBookDoesNotHaveSeriesPosition() {
        // given
        bookForm = createBookForm(READ, false);

        bookForm.openForm();

        // then
        assertThat(bookForm.seriesPositionFormItem.isVisible()).isFalse();
    }

    @Test
    void addBookToDatabaseWhenSaveEventIsCalled() {
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
    void addBooksToDatabaseWhenSaveEventIsCalled_withoutReplacingExistingBook() {
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
        assertSoftly(softly -> {
            softly.assertThat(bookService.count()).isEqualTo(2);
            softly.assertThat(booksInDatabase.get(0).getTitle()).isEqualTo(bookTitle);
            softly.assertThat(booksInDatabase.get(1).getTitle()).isEqualTo("someOtherBook");
        });
    }

    @Test
    void updateValuesInDatabaseForExistingBookWhenSaveEventIsCalled() {
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
        Book updatedBook = bookService.findAll().get(0);

        assertSoftly(softly -> {
            softly.assertThat(bookService.count()).isOne();
            softly.assertThat(newTitle).isEqualTo(updatedBook.getTitle());
            softly.assertThat(READ).isEqualTo(updatedBook.getPredefinedShelf().getPredefinedShelfName());
            softly.assertThat((newAuthor.getFirstName())).isEqualTo(updatedBook.getAuthor().getFirstName());
            softly.assertThat(newAuthor.getLastName()).isEqualTo(updatedBook.getAuthor().getLastName());
            softly.assertThat(newBookGenre).isEqualTo(updatedBook.getBookGenre());
            softly.assertThat(dateStarted).isEqualTo(updatedBook.getDateStartedReading());
            softly.assertThat(dateFinished).isEqualTo(updatedBook.getDateFinishedReading());
        });
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
    void updateValuesWhenBookIsMovedBetweenShelves(PredefinedShelf.ShelfName initialShelf,
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
        assertSoftly(softly -> {
            softly.assertThat(book.getTitle()).isEqualTo(bookTitle);
            softly.assertThat(shelfName).isEqualTo(book.getPredefinedShelf().getPredefinedShelfName());
            softly.assertThat(firstName).isEqualTo(book.getAuthor().getFirstName());
            softly.assertThat(lastName).isEqualTo(book.getAuthor().getLastName());
            softly.assertThat(BOOK_GENRE).isEqualTo(book.getBookGenre());
            softly.assertThat(numberOfPages).isEqualTo(book.getNumberOfPages());
        });
    }

    private void assertStateSpecificFields(Book book, LocalDate dateStarted, LocalDate dateFinished,
                                           RatingScale rating, String bookReview,
                                           Integer pagesRead) {
        assertSoftly(softly -> {
            softly.assertThat(book.getDateStartedReading()).isEqualTo(dateStarted);
            softly.assertThat(dateFinished).isEqualTo(book.getDateFinishedReading());
            softly.assertThat(rating).isEqualTo(book.getRating());
            softly.assertThat(bookReview).isEqualTo(book.getBookReview());
            softly.assertThat(pagesRead).isEqualTo(book.getPagesRead());
        });
    }

    /**
     * Tests whether a book is removed from the database when the delete-event is called
     */
    @Test
    void deleteBookFromDatabase() {
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

    @Test
    void onlyEnableSaveWhenValid() {
        // given
        bookForm = new BookForm(predefinedShelfService,customShelfService);
        assertThat(bookForm.saveButton.isEnabled()).isFalse();

        // when
        bookForm.setBook(createBook(READ, true, "title"));

        // then
        assertThat(bookForm.saveButton.isEnabled()).isTrue();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

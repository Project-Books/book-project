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
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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
    private static double rating = 9.0;

    private static int numberOfPages;
    private static Routes routes;
    private static PredefinedShelf readShelf;
    private static BookForm bookForm;
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private BookService bookService;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired PredefinedShelfService shelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(bookService != null);
        bookService.deleteAll();

        Assumptions.assumeTrue(shelfService != null);
        bookForm = new BookForm(shelfService);
        bookForm.setBook(createBook(shelfService));
    }

    private Book createBook(PredefinedShelfService predefinedShelfService) {
        Author author = new Author(firstName, lastName, null);
        Book book = new Book(bookTitle, author);

        readShelf = predefinedShelfService.findAll().get(2);
        numberOfPages = generateRandomNumberOfPages();

        book.setShelf(readShelf);
        book.setGenre(genre);
        book.setNumberOfPages(numberOfPages);
        book.setDateStartedReading(dateStarted);
        book.setDateFinishedReading(dateFinished);
        book.setRating(ratingVal);

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
        Assertions.assertEquals(numberOfPages, bookForm.numberOfPages.getValue());
        Assertions.assertEquals(dateStarted, bookForm.dateStartedReading.getValue());
        Assertions.assertEquals(dateFinished, bookForm.dateFinishedReading.getValue());
        Assertions.assertEquals(rating, bookForm.rating.getValue());
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
        Assertions.assertEquals(numberOfPages, savedOrDeletedBook.getNumberOfPages());
        Assertions.assertEquals(dateStarted, savedOrDeletedBook.getDateStartedReading());
        Assertions.assertEquals(dateFinished, savedOrDeletedBook.getDateFinishedReading());
        Assertions.assertEquals(ratingVal, savedOrDeletedBook.getRating());
    }

    private void populateBookForm() {
        bookForm.authorFirstName.setValue(firstName);
        bookForm.authorLastName.setValue(lastName);
        bookForm.bookTitle.setValue(bookTitle);
        bookForm.shelf.setValue(readShelf.getPredefinedShelfName());
        bookForm.bookGenre.setValue(genre);
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
        Assumptions.assumeFalse(bookForm.numberOfPages.isEmpty(), "Number of pages not populated");
        Assumptions.assumeFalse(bookForm.dateStartedReading.isEmpty(), "Date started populated");
        Assumptions.assumeFalse(bookForm.dateFinishedReading.isEmpty(), "Date finished populated");

        bookForm.reset.click();

        Assertions.assertTrue(bookForm.authorFirstName.isEmpty(), "Author first name not cleared");
        Assertions.assertTrue(bookForm.authorLastName.isEmpty(), "Author last name not cleared");
        Assertions.assertTrue(bookForm.bookTitle.isEmpty(), "Book title not cleared");
        Assertions.assertTrue(bookForm.shelf.isEmpty(), "Shelf not cleared");
        Assertions.assertTrue(bookForm.bookGenre.isEmpty(), "Book genre not cleared");
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
        switch (shelfName) {
            case TO_READ:
                break; // all fields already set to false
            case READING:
            case DID_NOT_FINISH:
                shouldShowStarted = true;
                // finished and rating already set to false
                break;
            case READ:
                shouldShowStarted = true;
                shouldShowFinished = true;
                shouldShowRating = true;
                break;
        }

        final String dateStarted = "Date started ";
        final String dateFinished = "Date finished ";
        final String rating = "Rating ";
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
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

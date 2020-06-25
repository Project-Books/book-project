package com.karankumar.bookproject.ui;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.RatingScale;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.book.BookForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext
public class BookFormTests {

	private static Routes routes;

	private static final String firstName = "Nick";
	private static final String lastName = "Bostrom";
	private static final Author author = new Author(firstName, lastName);
	private static final String bookTitle = "Superintelligence: Paths, Dangers, Strategies";
	private static final Book book = new Book(bookTitle, author);
	private static PredefinedShelf readShelf;
	private static final Genre genre = Genre.SCIENCE;
	private static final int pageCount = 300;
	private static final LocalDate dateStarted = LocalDate.now().minusDays(4);
	private static final LocalDate dateFinished = LocalDate.now();
	private static final RatingScale ratingVal = RatingScale.NINE;
	private static final double rating = 9;
	private static BookForm bookForm;

	@BeforeAll
	public static void discoverRoutes() {
		routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
	}

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private	BookService bookService;

	@BeforeEach
	public void setup(@Autowired PredefinedShelfService shelfService) {
		final SpringServlet servlet = new MockSpringServlet(routes, ctx);
		MockVaadin.setup(UI::new, servlet);

		Assumptions.assumeTrue(bookService != null);
		bookService.deleteAll();

		Assumptions.assumeTrue(shelfService != null);

		readShelf = shelfService.findAll().get(2);
		book.setShelf(readShelf);
		book.setGenre(genre);
		book.setNumberOfPages(pageCount);
		book.setDateStartedReading(dateStarted);
		book.setDateFinishedReading(dateFinished);
		book.setRating(ratingVal);

		bookForm = new BookForm(shelfService);
		bookForm.setBook(book);
	}

	@AfterEach
	public void tearDown() {
		MockVaadin.tearDown();
	}

	@Test
	public void formFieldsPopulated() {
		Assertions.assertEquals(bookTitle, bookForm.bookTitle.getValue());
		Assertions.assertEquals(firstName, bookForm.authorFirstName.getValue());
		Assertions.assertEquals(lastName, bookForm.authorLastName.getValue());
		Assertions.assertEquals(readShelf.getShelfName(), bookForm.shelf.getValue());
		Assertions.assertEquals(genre, bookForm.bookGenre.getValue());
		Assertions.assertEquals(pageCount, bookForm.pageCount.getValue());
		Assertions.assertEquals(dateStarted, bookForm.dateStartedReading.getValue());
		Assertions.assertEquals(dateFinished, bookForm.dateFinishedReading.getValue());
		Assertions.assertEquals(rating, bookForm.rating.getValue());
	}

	@Test
	public void saveEventPopulated() {
		populateBookForm();

		AtomicReference<Book> savedBookReference = new AtomicReference<>(null);
		bookForm.addListener(BookForm.SaveEvent.class, event -> savedBookReference.set(event.getBook()));
		bookForm.saveButton.click();
		Book savedBook = savedBookReference.get();

		Assertions.assertEquals(bookTitle, savedBook.getTitle());
		Assertions.assertEquals(firstName, savedBook.getAuthor().getFirstName());
		Assertions.assertEquals(lastName, savedBook.getAuthor().getLastName());
		Assertions.assertEquals(readShelf.getShelfName(), savedBook.getShelf().getShelfName());
		Assertions.assertEquals(genre, savedBook.getGenre());
		Assertions.assertEquals(pageCount, savedBook.getNumberOfPages());
		Assertions.assertEquals(dateStarted, savedBook.getDateStartedReading());
		Assertions.assertEquals(dateFinished, savedBook.getDateFinishedReading());
		Assertions.assertEquals(ratingVal, savedBook.getRating());
	}

	private void populateBookForm() {
		bookForm.authorFirstName.setValue(firstName);
		bookForm.authorLastName.setValue(lastName);
		bookForm.bookTitle.setValue(bookTitle);
		bookForm.shelf.setValue(readShelf.getShelfName());
		bookForm.bookGenre.setValue(genre);
		bookForm.pageCount.setValue(pageCount);
		bookForm.dateStartedReading.setValue(dateStarted);
		bookForm.dateFinishedReading.setValue(dateFinished);
		bookForm.rating.setValue(rating);
	}

	@Test
	public void deleteEventPopulated() {
		populateBookForm();

		AtomicReference<Book> deletedBookReference = new AtomicReference<>(null);
		bookForm.addListener(BookForm.DeleteEvent.class, event -> deletedBookReference.set(event.getBook()));
		bookForm.delete.click();
		Book deletedBook = deletedBookReference.get();

		Assertions.assertEquals(bookTitle, deletedBook.getTitle());
		Assertions.assertEquals(firstName, deletedBook.getAuthor().getFirstName());
		Assertions.assertEquals(lastName, deletedBook.getAuthor().getLastName());
		Assertions.assertEquals(readShelf.getShelfName(), deletedBook.getShelf().getShelfName());
		Assertions.assertEquals(genre, deletedBook.getGenre());
		Assertions.assertEquals(pageCount, deletedBook.getNumberOfPages());
		Assertions.assertEquals(dateStarted, deletedBook.getDateStartedReading());
		Assertions.assertEquals(dateFinished, deletedBook.getDateFinishedReading());
		Assertions.assertEquals(ratingVal, deletedBook.getRating());
	}
}

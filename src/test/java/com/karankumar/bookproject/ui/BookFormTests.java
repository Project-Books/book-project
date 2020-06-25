package com.karankumar.bookproject.ui;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.model.*;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.book.BookForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

/**
 * @author karan on 25/06/2020
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext
public class BookFormTests {

	private static Routes routes;

	@BeforeAll
	public static void discoverRoutes() {
		routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
	}

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	public void setup() throws Exception {
		final SpringServlet servlet = new MockSpringServlet(routes, ctx);
		MockVaadin.setup(UI::new, servlet);
		bookRepository.deleteAll();
	}

	@AfterEach
	public void tearDown() {
		MockVaadin.tearDown();
	}

	@org.junit.jupiter.api.Test
	public void formFieldsPopulated(@Autowired PredefinedShelfService shelfService) {
		String firstName = "Nick";
		String lastName = "Bostrom";
		Author author = new Author(firstName, lastName);

		String bookTitle = "Superintelligence: Paths, Dangers, Strategies";
		Book book = new Book(bookTitle, author);

		PredefinedShelf shelf = shelfService.findAll().get(2);
		book.setShelf(shelf);

		Genre genre = Genre.SCIENCE;
		book.setGenre(genre);

		int pageCount = 300;
		book.setNumberOfPages(pageCount);

		LocalDate dateStarted = LocalDate.now().minusDays(4);
		LocalDate dateFinished = LocalDate.now();
		book.setDateStartedReading(dateStarted);
		book.setDateFinishedReading(dateFinished);

		double rating = 9;
		book.setRating(RatingScale.NINE);

		BookForm bookForm = new BookForm(shelfService);
		bookForm.setBook(book);

		Assertions.assertEquals(bookTitle, bookForm.bookTitle.getValue());
		Assertions.assertEquals(firstName, bookForm.authorFirstName.getValue());
		Assertions.assertEquals(lastName, bookForm.authorLastName.getValue());
		Assertions.assertEquals(shelf.getShelfName(), bookForm.shelf.getValue());
		Assertions.assertEquals(genre, bookForm.bookGenre.getValue());
		Assertions.assertEquals(pageCount, bookForm.pageCount.getValue());
		Assertions.assertEquals(dateStarted, bookForm.dateStartedReading.getValue());
		Assertions.assertEquals(dateFinished, bookForm.dateFinishedReading.getValue());
		Assertions.assertEquals(rating, bookForm.rating.getValue());

	}
}

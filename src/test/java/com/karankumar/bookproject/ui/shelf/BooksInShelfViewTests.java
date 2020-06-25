package com.karankumar.bookproject.ui.shelf;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.AUTHOR_KEY;
import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.DATE_FINISHED_KEY;
import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.DATE_STARTED_KEY;
import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.GENRE_KEY;
import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.PAGES_KEY;
import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.RATING_KEY;
import static com.karankumar.bookproject.ui.shelf.BooksInShelfView.TITLE_KEY;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext
public class BooksInShelfViewTests {

	private static Routes routes;
	private BooksInShelfView shelfView;

	private final ArrayList<String> expectedToReadColumns = new ArrayList<>(Arrays.asList(
			TITLE_KEY,
			AUTHOR_KEY,
			GENRE_KEY,
			PAGES_KEY
	));
	private final ArrayList<String> expectedReadingColumns = new ArrayList<>(Arrays.asList(
			TITLE_KEY,
			AUTHOR_KEY,
			GENRE_KEY,
			DATE_STARTED_KEY,
			PAGES_KEY
	));
	private final ArrayList<String> expectedReadColumns = new ArrayList<>(Arrays.asList(
			TITLE_KEY,
			AUTHOR_KEY,
			GENRE_KEY,
			DATE_STARTED_KEY,
			DATE_FINISHED_KEY,
			RATING_KEY,
			PAGES_KEY
	));

	@BeforeAll
	public static void discoverRoutes() {
		routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
	}

	@Autowired
	private ApplicationContext ctx;

	@BeforeEach
	public void setup(@Autowired BookService bookService, @Autowired PredefinedShelfService shelfService) {
		final SpringServlet servlet = new MockSpringServlet(routes, ctx);
		MockVaadin.setup(UI::new, servlet);

		Assumptions.assumeTrue(shelfService != null);
		shelfView = new BooksInShelfView(bookService, shelfService);
	}

	@AfterEach
	public void tearDown() {
		MockVaadin.tearDown();
	}

	@ParameterizedTest
	@EnumSource(PredefinedShelf.ShelfName.class)
	public void correctGridColumnsShow(PredefinedShelf.ShelfName shelfName) {
		System.out.println("Shelf: " + shelfName);

		shelfView.showOrHideGridColumns(shelfName);
		List<Grid.Column<Book>> columns = shelfView.bookGrid.getColumns();

		ArrayList<String> expectedColumns = new ArrayList<>();
		switch (shelfName) {
			case TO_READ:
				expectedColumns = expectedToReadColumns;
				break;
			case READING:
			case DID_NOT_FINISH: // intentional
				expectedColumns = expectedReadingColumns;
				break;
			case READ:
				expectedColumns = expectedReadColumns;
				break;
		}

		for (Grid.Column<Book> col : columns) {
			if (expectedColumns.contains(col.getKey())) {
				Assertions.assertTrue(col.isVisible(), col.getKey() + " column is not showing");
			} else {
				Assertions.assertFalse(col.isVisible(), col.getKey() + " column is showing");
			}
		}
	}
}

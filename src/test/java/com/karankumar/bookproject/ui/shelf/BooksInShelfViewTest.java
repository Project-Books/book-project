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

package com.karankumar.bookproject.ui.shelf;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.tags.IntegrationTest;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.karankumar.bookproject.ui.shelf.component.BookGridColumn.*;

@IntegrationTest
@WebAppConfiguration
public class BooksInShelfViewTests {

    private static Routes routes;

    @Autowired
    private ApplicationContext ctx;

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

    private final ArrayList<String> expectedDidNotFinishColumns = new ArrayList<>(Arrays.asList(
            TITLE_KEY,
            AUTHOR_KEY,
            GENRE_KEY,
            DATE_STARTED_KEY,
            PAGES_KEY,
            PAGES_READ_KEY
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
    private BooksInShelfView shelfView;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired BookService bookService,
                      @Autowired PredefinedShelfService shelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(shelfService != null);
        shelfView = new BooksInShelfView(bookService, shelfService);
    }

    @ParameterizedTest
    @EnumSource(PredefinedShelf.ShelfName.class)
    public void correctGridColumnsShow(PredefinedShelf.ShelfName shelfName) {
        System.out.println("Shelf: " + shelfName);
        try {
            shelfView.showOrHideGridColumns(shelfName);
        } catch (NotSupportedException e) {
            e.printStackTrace();
        }

        List<Grid.Column<Book>> columns = shelfView.getColumns();

        ArrayList<String> expectedColumns = new ArrayList<>();
        switch (shelfName) {
            case TO_READ:
                expectedColumns = expectedToReadColumns;
                break;
            case READING:
                expectedColumns = expectedReadingColumns;
                break;
            case DID_NOT_FINISH: // intentional
                expectedColumns = expectedDidNotFinishColumns;
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

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

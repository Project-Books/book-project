/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.shelf;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.karankumar.bookproject.ui.shelf.component.BookGridColumn;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.SpringServlet;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@IntegrationTest
@WebAppConfiguration
@DisplayName("BooksInShelfView should")
class BooksInShelfViewTest {

    private static Routes routes;

    private final ApplicationContext ctx;

    private final ArrayList<String> expectedToReadColumns = new ArrayList<>(Arrays.asList(
        BookGridColumn.TITLE_KEY,
        BookGridColumn.AUTHOR_KEY,
        BookGridColumn.GENRE_KEY,
        BookGridColumn.PAGES_KEY
    ));
    private final ArrayList<String> expectedReadingColumns = new ArrayList<>(Arrays.asList(
        BookGridColumn.TITLE_KEY,
        BookGridColumn.AUTHOR_KEY,
        BookGridColumn.GENRE_KEY,
        BookGridColumn.DATE_STARTED_KEY,
        BookGridColumn.PAGES_KEY
    ));

    private final ArrayList<String> expectedDidNotFinishColumns = new ArrayList<>(Arrays.asList(
        BookGridColumn.TITLE_KEY,
        BookGridColumn.AUTHOR_KEY,
        BookGridColumn.GENRE_KEY,
        BookGridColumn.DATE_STARTED_KEY,
        BookGridColumn.PAGES_KEY,
        BookGridColumn.PAGES_READ_KEY
    ));

    private final ArrayList<String> expectedReadColumns = new ArrayList<>(Arrays.asList(
        BookGridColumn.TITLE_KEY,
        BookGridColumn.AUTHOR_KEY,
        BookGridColumn.GENRE_KEY,
        BookGridColumn.DATE_STARTED_KEY,
        BookGridColumn.DATE_FINISHED_KEY,
        BookGridColumn.RATING_KEY,
        BookGridColumn.PAGES_KEY
    ));

    private BooksInShelfView shelfView;

    @BeforeAll
    public static void discoverRoutes() {
        UI.setCurrent(new UI()); // UI.getCurrent can be null and throw NPE in SettingsView
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @Autowired
    public BooksInShelfViewTest(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @BeforeEach
    public void setUp(@Autowired BookService bookService,
                      @Autowired PredefinedShelfService predefinedShelfService,
                      @Autowired CustomShelfService customShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        shelfView = new BooksInShelfView(bookService, predefinedShelfService, customShelfService);
    }

    @ParameterizedTest
    @EnumSource(PredefinedShelf.ShelfName.class)
    void showCorrectGridColumns(PredefinedShelf.ShelfName shelfName) {
        try {
            shelfView.showOrHideGridColumns(shelfName.toString());
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
            case DID_NOT_FINISH:
                expectedColumns = expectedDidNotFinishColumns;
                break;
            case READ:
                expectedColumns = expectedReadColumns;
                break;
            default:
                Assertions.fail();
        }

        for (Grid.Column<Book> col : columns) {
            if (expectedColumns.contains(col.getKey())) {
                assertThat(col.isVisible())
                        .withFailMessage(col.getKey() + " column is not showing")
                        .isTrue();
            } else {
                assertThat(col.isVisible())
                        .withFailMessage(col.getKey() + " column is showing")
                        .isFalse();
            }
        }
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

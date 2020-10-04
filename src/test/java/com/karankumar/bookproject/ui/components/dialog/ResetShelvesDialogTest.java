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

package com.karankumar.bookproject.ui.components.dialog;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("ResetShelvesDialog should")
class ResetShelvesDialogTest {
    
    private BookService bookService;
    private PredefinedShelf toRead;

    @Autowired private ApplicationContext ctx;
    private static Routes routes;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired BookService bookService,
                      @Autowired PredefinedShelfService predefinedShelfService) {
        toRead = new PredefinedShelfUtils(predefinedShelfService).findToReadShelf();
        this.bookService = bookService;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);
    }
    
    private void populateBookService() {
        Author author = new Author("H.A.", "Rey");
        Book book1 = new Book("Curious George Takes A Job", author, toRead);
        bookService.save(book1);
        Book book2 = new Book("Curious George Gets a medal", author, toRead);
        bookService.save(book2);
    }

    @Test
    void deletesAllBooks() {
        // given
        populateBookService();
        ResetShelvesDialog resetShelvesDialog = new ResetShelvesDialog(bookService);

        // when
        resetShelvesDialog.openDialog();
        resetShelvesDialog.confirmButton.click();

        // then
        assertThat(bookService.count()).isZero();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

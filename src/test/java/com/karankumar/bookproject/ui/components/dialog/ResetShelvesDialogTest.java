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

package com.karankumar.bookproject.ui.components.dialog;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@IntegrationTest
public class ResetShelvesDialogTest {

    // Arrange
    private BookService bookService;
    private ResetShelvesDialog resetShelvesDialog;
    private static Routes routes;

    @Autowired
    private ApplicationContext ctx;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired BookService bookService) {
        this.bookService = bookService;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(bookService != null);
    }

    @Test
    void openDialog(){
        //Act
        resetShelvesDialog = new ResetShelvesDialog(bookService);
        resetShelvesDialog.open();
        resetShelvesDialog.confirmButton.click();
        //Assert
        Assertions.assertTrue(resetShelvesDialog.isEmpty());

    }


    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

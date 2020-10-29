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

package com.karankumar.bookproject.ui.shelf.component;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("BookShelfComboBox should")
class BookShelfComboBoxTest {
    private static Routes routes;

    private final CustomShelfService customShelfService;
    private final ApplicationContext ctx;

    private BookShelfComboBox comboBox;

    @Autowired
    BookShelfComboBoxTest(CustomShelfService customShelfService, ApplicationContext ctx) {
        this.customShelfService = customShelfService;
        this.ctx = ctx;
    }

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setUp() {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);
        comboBox = new BookShelfComboBox(customShelfService);
    }

    @Test
    void displayNewShelfInList() {
        // given
        String name = "UnitTest";
        CustomShelf test = customShelfService.createCustomShelf(name);
        customShelfService.save(test);

        // when
        comboBox.updateShelfList();

        // then
        List<String> shelvesList = comboBox.allShelvesList
                .getDataProvider()
                .fetch(new Query<>())
                .collect(Collectors.toList());

        assertThat(shelvesList.contains(name))
                .withFailMessage("Adding a custom shelf is not properly showing in " +
                        "the shelf combo box")
                .isTrue();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}


package com.karankumar.bookproject.ui.shelf.component;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WebAppConfiguration
@DisplayName("BookShelfComboBox should")
class BookShelfComboBoxTest {

    private static Routes routes;
    private CustomShelfService customShelfService;
    private BooksInShelfView shelfView;

    @Autowired private ApplicationContext ctx;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired BookService bookService,
                      @Autowired PredefinedShelfService predefinedShelfService,
                      @Autowired CustomShelfService customShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        shelfView = new BooksInShelfView(bookService, predefinedShelfService, customShelfService);
        this.customShelfService = customShelfService;
    }

    @Test
    void editDisabledWhenPredefinedShelfSelected() {
        //given
        String name = "To read";

        //when
        shelfView.whichShelf.allShelvesList.setValue(name);

        //then
        assertThat(shelfView.editShelf.isEnabled()).isFalse();
    }

    @Test
    void editEnabledWhenCustomShelfSelected() {
        //given
        String name = "UnitTest";
        CustomShelf test = new CustomShelf(name);
        customShelfService.save(test);

        //when
        shelfView.whichShelf.allShelvesList.setLabel(name);

        //then
        assertThat(shelfView.editShelf.isEnabled()).isFalse();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

package com.karankumar.bookproject.ui.shelf;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WebAppConfiguration
class NewShelfTest {
    private final BooksInShelfView shelfView;
    private static Routes routes;
    private final CustomShelfService customShelfService;

    @Autowired private ApplicationContext ctx;

    @Autowired
    NewShelfTest(BookService bookService,
                 PredefinedShelfService predefinedShelfService,
                 CustomShelfService customShelfService) {
        this.customShelfService = customShelfService;
        shelfView = new BooksInShelfView(bookService, predefinedShelfService, customShelfService);
    }

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setUp() {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);
    }

    @Test
    void newShelfShowsInList() {
        CustomShelf test = new CustomShelf("UnitTest");
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ** * * * * * * * * * * ** * * * * * * * * * * ** * * * * * * * * * * * * * * * * * * * * * * *
        WRONG TEST - DON'T LOOK AT CUSTOM SHELF REPOSITORY HAVING THIS SHELF
        USE A LISTDATAPROVIDER TO GET ITEMS FROM A ComboBox
        REACH OUT TO KARAN FOR HELP
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         */

        customShelfService.save(test);
        shelfView.whichShelf.updateShelfList();

        assertThat(customShelfService.findAll("UnitTest").size() > 0).isTrue();
        //, "Adding a new custom shelf does not save as expected."
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}


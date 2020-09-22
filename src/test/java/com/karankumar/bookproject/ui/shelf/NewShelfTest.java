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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * The NewShelfTest helps verify functionality around creating a new shelf works as expected.
 */
@IntegrationTest
@WebAppConfiguration
public class NewShelfTest {
    private BooksInShelfView shelfView;
    private static Routes routes;
    private CustomShelfService customShelfService;

    @Autowired
    private ApplicationContext ctx;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    /**
     * setup here has been copied from BooksInShelfViewTest, as this test requires a shelf view as well.
     */
    @BeforeEach
    public void setup(@Autowired BookService bookService,
                      @Autowired PredefinedShelfService predefinedShelfService,
                      @Autowired CustomShelfService customShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(predefinedShelfService != null);
        this.customShelfService = customShelfService;
        shelfView = new BooksInShelfView(bookService, predefinedShelfService, customShelfService);
    }

    /**
     * This test verifies that when creating a new shelf and adding it to the shelf list,
     * the dropdown that shows all shelves contains the new shelf. Otherwise, an
     * IllegalStateException will be thrown from ComboBox.
     */
    @Test
    public void newShelfShowsInList() {
        CustomShelf test = new CustomShelf("UnitTest");
        customShelfService.save(test);
        shelfView.whichShelf.updateShelfList();

        Assertions.assertTrue(customShelfService.findAll("UnitTest").size() > 0, "Adding a new custom shelf does not save as expected.");
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}
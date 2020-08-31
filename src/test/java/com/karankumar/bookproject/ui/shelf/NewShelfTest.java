package com.karankumar.bookproject.ui.shelf;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.ShelfUtils;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * The NewShelfTest helps verify functionality around creating a new shelf works as expected.
 * @author MikeLowrie
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
     * This test verifies that when the user creates a new shelf (here, named "Test"), the shelf list
     * will be updated correctly once saved. The test makes sure that the count of shelves is not the same
     * before and after saving.
     */
    @ParameterizedTest
    @EnumSource(PredefinedShelf.ShelfName.class)
    public void newShelfShowsInList() {
        int before = 0;
        int after = -1;
        try {
            before = ShelfUtils.findAllShelfNames(customShelfService.findAll()).size();

            CustomShelf test = new CustomShelf("Test");
            customShelfService.save(test);
            shelfView.whichShelf.updateShelfList();

            after = ShelfUtils.findAllShelfNames(customShelfService.findAll()).size();

            Assertions.assertEquals(before + 1, after, "Shelf list is not updating when a custom shelf is created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

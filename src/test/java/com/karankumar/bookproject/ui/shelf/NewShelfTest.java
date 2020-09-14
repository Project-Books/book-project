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
import com.vaadin.flow.component.combobox.ComboBox;
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
    @ParameterizedTest
    @EnumSource(PredefinedShelf.ShelfName.class)
    public void newShelfShowsInList() {
        int before = 0;
        int after = -1;
        try {
            CustomShelf test = new CustomShelf("UnitTest");
            customShelfService.save(test);
            shelfView.whichShelf.updateShelfList();

            Assertions.assertThrows(IllegalStateException.class,
                    ()->{shelfView.whichShelf.getAllShelvesList().setValue("UnitTest");});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

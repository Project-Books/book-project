package com.karankumar.bookproject.ui.settings;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class SettingsViewTest {

    private static Routes routes;

    @Autowired
    private ApplicationContext ctx;

    // Arrange
    private BookService bookService;
    private static SettingsView settingsView;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired BookService bookService){
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(bookService != null);
        this.bookService = bookService;
        settingsView = new SettingsView(bookService);
    }

    @Test
    void deleteAllBooks(){
        Assertions.assertFalse(settingsView.isEmpty());
        // Act
        bookService.deleteAll();
        // Assert
        Assertions.assertTrue(settingsView.isEmpty());
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}
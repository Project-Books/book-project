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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WebAppConfiguration
@DisplayName("CustomShelfForm should")
public class CustomShelfFormTest {

    private static Routes routes;
    private CustomShelfService customShelfService;
    private BooksInShelfView shelfView;
    private CustomShelfForm form;

    @Autowired private ApplicationContext ctx;


    private final String shelfName = "UnitTest";

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
        this.form = shelfView.customShelfForm;
        this.customShelfService = customShelfService;
    }

    @Test
    void deleteButtonCreatedCorrectly() {
        // given

        // when

        // then
        assertThat(form.delete.getText().equals("Delete shelf")).isTrue();
    }

    @Test
    void deleteShelfOnDeleteButtonClick() {
        // given
        CustomShelf test = new CustomShelf(shelfName);
        customShelfService.save(test);
        shelfView.updateWhichShelfList();

        // when
        form.editShelf(shelfName);
        form.delete.click();

        // then
        assertThat(customShelfService.findAll(shelfName).size() == 0
                && !form.dialog.isOpened()).isTrue();
    }

    @Test
    void editShelfAssignsShelfNameField() {
        // given

        // when
        form.editShelf(shelfName);

        // then
        assertThat(form.shelfNameField.getValue().equals(shelfName)
                && form.deletingShelf.equals(shelfName)).isTrue();
    }

    @Test
    void setCustomShelfBeanFindsExistingShelf() {
        // given
        CustomShelf test = new CustomShelf(shelfName);
        customShelfService.save(test);
        form.deletingShelf = shelfName;

        // when
        form.setCustomShelfBean();

        // then
        assertThat(form.binder.getBean().equals(test)).isTrue();
    }

    @Test
    void setCustomShelfBeanFindsNewShelf() {
        // given
        form.shelfNameField.setValue(shelfName);

        // when
        form.setCustomShelfBean();

        // then
        assertThat(form.binder.getBean().getShelfName().equals(shelfName)).isTrue();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}


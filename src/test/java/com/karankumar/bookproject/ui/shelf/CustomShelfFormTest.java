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

package com.karankumar.bookproject.ui.shelf;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
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
class CustomShelfFormTest {

    private static Routes routes;

    @Autowired private ApplicationContext ctx;

    private CustomShelfForm customShelfForm;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired PredefinedShelfService predefinedShelfService,
                      @Autowired CustomShelfService customShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        customShelfForm = new CustomShelfForm(customShelfService, predefinedShelfService);
    }

    @Test
    void saveButtonShouldBeInitiallyGreyedOut () {
        // given
        // customShelfForm

        // when
        userOpenDialog();

        // then
        formIsInInitialState();
    }

    private void userOpenDialog() {
        customShelfForm.addShelf();
    }

    private void formIsInInitialState() {
        assertThat(customShelfForm.shelfNameField.getValue()).isEmpty();
        assertThat(customShelfForm.shelfNameField.isInvalid()).isFalse();
        assertThat(customShelfForm.saveButton.isEnabled()).isFalse();
    }

    @Test
    void saveButtonShouldBeGreyedOutWithExistingShelfName () {
        // given
        // customShelfForm

        // when
        shelfNameIsLikeOneAlreadyInUse();

        // then
        formsIsInErrorState();
    }

    private void shelfNameIsLikeOneAlreadyInUse() {
        customShelfForm.shelfNameField.setValue(PredefinedShelf.ShelfName.TO_READ.toString());
    }

    private void formsIsInErrorState() {
        assertThat(customShelfForm.shelfNameField.isInvalid()).isTrue();
        assertThat(customShelfForm.saveButton.isEnabled()).isFalse();
    }

    @Test
    void saveButtonShouldBeEnabledWithNonExistingShelfName () {
        // given
        // customShelfForm

        // when
        shelfNameIsNotLikeOneAlreadyInUse();

        // then
        formIsInValidState();
    }

    private void shelfNameIsNotLikeOneAlreadyInUse() {
        customShelfForm.shelfNameField.setValue("NonExistingShelfName");
    }

    private void formIsInValidState() {
        assertThat(customShelfForm.shelfNameField.getValue()).isNotEmpty();
        assertThat(customShelfForm.shelfNameField.isInvalid()).isFalse();
        assertThat(customShelfForm.saveButton.isEnabled()).isTrue();
    }

    @Test
    void savingAndReopeningFormShouldClearTextfield () {
        // given
        // customShelfForm

        // when
        userOpenDialog();
        userCloseSavingAShelf();
        userOpenDialog();

        // then
        formIsInInitialState();
    }

    private void userCloseSavingAShelf() {
        customShelfForm.shelfNameField.setValue("Test");
        customShelfForm.saveButton.click();
    }

    @Test
    void closingWithoutSavingAndReopeningFormShouldClearTextField () {
        // given
        // customShelfForm

        // when
        userOpenDialog();
        userCloseWithoutSavingAShelf();
        userOpenDialog();

        // then
        formIsInInitialState();
    }

    private void userCloseWithoutSavingAShelf() {
        customShelfForm.shelfNameField.setValue("Test");
        customShelfForm.closeForm();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

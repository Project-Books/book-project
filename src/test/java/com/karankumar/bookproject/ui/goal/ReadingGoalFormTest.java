/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.goal;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

@IntegrationTest
@WebAppConfiguration
@DisplayName("ReadingGoalForm should")
class ReadingGoalFormTest {
    private static Routes routes;

    private final ApplicationContext ctx;
    private ReadingGoalService readingGoalService;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    public ReadingGoalFormTest(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @BeforeEach
    public void setUp(@Autowired ReadingGoalService readingGoalService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);
        this.readingGoalService = readingGoalService;
    }

    @Test
    void onlyHaveATargetGoalOfAtLeastOne() {
        // given
        ReadingGoalForm goalForm = new ReadingGoalForm();

        // when
        goalForm.targetToRead.setValue(1);
        goalForm.chooseGoalType.setValue(ReadingGoal.GoalType.BOOKS);
        goalForm.saveButton.click();

        assertThat(goalForm.binder.isValid()).isTrue();
        goalForm.targetToRead.setValue(0);

        // then
        assertThat(goalForm.binder.isValid()).isFalse();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
        readingGoalService.deleteAll(); // reset
    }
}
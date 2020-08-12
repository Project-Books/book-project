package com.karankumar.bookproject.ui.goal;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

@IntegrationTest
@WebAppConfiguration
public class ReadingGoalFormTest {
    static Routes routes;

    @Autowired
    private ApplicationContext ctx;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired ReadingGoalService goalService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(goalService != null);
        goalService.deleteAll(); // reset
    }

    private ReadingGoal.GoalType getRandomGoalType() {
        ReadingGoal.GoalType[] goalTypes = ReadingGoal.GoalType.values();
        return goalTypes[new Random().nextInt(goalTypes.length)];
    }

    @Test
    public void onlyTargetGoalOfAtLeastOneIsValid() {
        ReadingGoalForm goalForm = new ReadingGoalForm();

        goalForm.targetToRead.setValue(1);
        goalForm.chooseGoalType.setValue(getRandomGoalType());
        goalForm.saveButton.click();
        Assertions.assertTrue(goalForm.binder.isValid());

        goalForm.targetToRead.setValue(0);
        Assertions.assertFalse(goalForm.binder.isValid());
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

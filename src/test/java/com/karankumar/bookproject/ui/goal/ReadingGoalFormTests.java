package com.karankumar.bookproject.ui.goal;


import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration

public class ReadingGoalFormTests {
     static Routes routes;


    @Autowired
    private ApplicationContext ctx;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired ReadingGoalService goalService, @Autowired PredefinedShelfService predefinedShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(goalService != null);
        goalService.deleteAll(); // reset

    }


    private ReadingGoal.GoalType getRandomGoalType() {
        ReadingGoal.GoalType[] goalTypes = ReadingGoal.GoalType.values();
        return goalTypes[new Random().nextInt(goalTypes.length)];
    }



    /**
     * Tests that only a target >= 1 is valid
     */
    @Test
    public void isValidGoal () {
            ReadingGoalForm goalForm = new ReadingGoalForm();

            goalForm.targetToRead.setValue(1);
            goalForm.chooseGoalType.setValue(getRandomGoalType());
            goalForm.saveButton.click();
            Assertions.assertTrue(goalForm.binder.isValid());

            goalForm.targetToRead.setValue(0);
            Assertions.assertFalse(goalForm.binder.isValid());


        }

        @AfterEach
        public void tearDown () {
            MockVaadin.tearDown();
        }

}

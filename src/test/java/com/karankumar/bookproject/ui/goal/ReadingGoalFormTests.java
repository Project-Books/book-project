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

/*
Instead, we need to test the binder validation:

Instead of putting the test in ReadingGoalViewTests, make a new class called ReadingGoalFormTests
Make whichever fields you need package-private
Since the package structure in the test directory is the same as the source,
you'll be able to access these variables with package access level

Simulate button presses with click() (e.g. readingGoalForm.saveButton().click())
Pass in 0 to the targetToRead form field. Assert that the binder is invalid
Pass in to the targetToRead` form field. Assert that the binder is now valid
I've missed out some details intentionally, but this is the main idea


There's some boilerplate code you'll need for Karibu.
You can take a look at the other ui test classes to try to figure out what you need.
Let me know if you get stuck on this step
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration

public class ReadingGoalFormTests {
     static Routes routes;


    @Autowired
    private ApplicationContext ctx;

    private ReadingGoalView goalView;




    //final Binder<ReadingGoal> binder = new BeanValidationBinder<>(ReadingGoal.class);


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

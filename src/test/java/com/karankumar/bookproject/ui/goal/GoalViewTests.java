package com.karankumar.bookproject.ui.goal;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.service.GoalService;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class GoalViewTests {
    private static Routes routes;

    @Autowired
    private ApplicationContext ctx;

    private GoalService goalService;
    private GoalView goalView;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired GoalService goalService, @Autowired PredefinedShelfService predefinedShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(goalService != null);
        goalService.deleteAll(); // reset

        this.goalService = goalService;
        goalView = new GoalView(goalService, predefinedShelfService);
    }

    @Test
    public void progressValueCorrect() {
        int booksToRead = new Random().nextInt(100);

        Assertions.assertEquals(GoalView.getProgress(5, 0), 0); // == 0%
        Assertions.assertEquals(GoalView.getProgress(25, 5), 0.2); // < 100%
        Assertions.assertEquals(GoalView.getProgress(booksToRead, booksToRead), 1.0); // == 100%
        Assertions.assertEquals(GoalView.getProgress(booksToRead, (booksToRead + 1)), 1.0); // > 100%

        // ensure 0, and not an arithmetic exception, is returned
        Assertions.assertEquals(GoalView.getProgress(0, 5), 0);
    }

    /**
     * Check whether the set goal button text correctly updates when the goal has been updated
     */
    @Test
    public void setGoalButtonTextIsCorrect() {
        Assumptions.assumeTrue(goalService.findAll().size() == 0);
        Assertions.assertEquals(goalView.setGoalButton.getText(), GoalView.SET_GOAL);

        ReadingGoal.GoalType[] goalTypes = ReadingGoal.GoalType.values();
        ReadingGoal.GoalType randomGoalType = goalTypes[new Random().nextInt(goalTypes.length)];
        int randomGoalTarget = ThreadLocalRandom.current().nextInt(0, 10_000);

        ReadingGoal goal = new ReadingGoal(randomGoalTarget, randomGoalType);
        goalView.updateReadingGoal(goal.getTarget(), goal.getGoalType());
        Assertions.assertEquals(goalView.setGoalButton.getText(), GoalView.UPDATE_GOAL);
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoalServiceTest {
    private GoalService goalService;
    @BeforeEach
    public void setup(@Autowired GoalService goalService) {
        Assumptions.assumeTrue(goalService != null);
        this.goalService = goalService;
        goalService.deleteAll(); // reset
    }

    /**
     * Tests whether save() in the reading goal service overwrites an existing goal with a new goal
     */
    @Test
    public void onlyOneReadingGoalExists() {
        Assumptions.assumeTrue(goalService.count() == 0);

        ReadingGoal readingGoal1 = new ReadingGoal(24, ReadingGoal.GoalType.BOOKS);
        goalService.save(readingGoal1);
        Assertions.assertEquals(1, (long) goalService.count());

        ReadingGoal readingGoal2 = new ReadingGoal(240, ReadingGoal.GoalType.PAGES);
        goalService.save(readingGoal2);
        Assertions.assertEquals(1, (long) goalService.count());
        Assertions.assertEquals(readingGoal2, goalService.findAll().get(0));
    }
}

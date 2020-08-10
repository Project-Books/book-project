package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class ReadingGoalServiceTest {
    private ReadingGoalService goalService;
    @BeforeEach
    public void setup(@Autowired ReadingGoalService goalService) {
        Assumptions.assumeTrue(goalService != null);
        this.goalService = goalService;
        resetBooks();
    }

    private void resetBooks() {
        goalService.deleteAll();
    }

    /**
     * Tests whether save() in the reading goal service overwrites an existing goal with a new goal
     */
    @Test
    public void expectSavingGoalToOverwriteExistingGoal() {
        ReadingGoal readingGoal1 = new ReadingGoal(20, ReadingGoal.GoalType.BOOKS);
        goalService.save(readingGoal1);
        Assertions.assertEquals(1, (long) goalService.count());

        ReadingGoal readingGoal2 = new ReadingGoal(40, ReadingGoal.GoalType.PAGES);
        goalService.save(readingGoal2);
        Assertions.assertEquals(1, (long) goalService.count());
        // check reading goal 2 overwrote reading goal 1
        Assertions.assertEquals(readingGoal2, goalService.findAll().get(0));
    }
}

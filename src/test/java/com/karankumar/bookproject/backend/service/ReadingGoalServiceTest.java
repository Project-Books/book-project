package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

@IntegrationTest
public class ReadingGoalServiceTest {
    private ReadingGoalService goalService;
    @BeforeEach
    public void setup(@Autowired ReadingGoalService goalService) {
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

        ReadingGoal readingGoal1 = new ReadingGoal(generateRandomTarget(), ReadingGoal.GoalType.BOOKS);
        goalService.save(readingGoal1);
        Assertions.assertEquals(1, (long) goalService.count());

        ReadingGoal readingGoal2 = new ReadingGoal(generateRandomTarget(), ReadingGoal.GoalType.PAGES);
        goalService.save(readingGoal2);
        Assertions.assertEquals(1, (long) goalService.count());
        // check reading goal 2 overwrote reading goal 1
        Assertions.assertEquals(readingGoal2, goalService.findAll().get(0));
    }

    private int generateRandomTarget() {
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }
}

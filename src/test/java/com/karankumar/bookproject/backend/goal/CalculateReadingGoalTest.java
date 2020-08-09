package com.karankumar.bookproject.backend.goal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculateReadingGoalTest {
    private int booksToRead = 52;
    @Test
    public void progressValueCorrect() {
        int toRead = 25;
        int read = 5;
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(toRead, read), 0.2);
    }

    @Test
    public void testProgressWhenGoalMet() {
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(booksToRead, booksToRead), 1.0);
    }

    @Test
    public void testProgressWhenGoalExceeded() {
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(booksToRead, (booksToRead + 1)), 1.0);
    }

    @Test
    public void testNoProgressMadeTowardsGoal() {
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0), 0);
    }

    @Test
    public void testCalculateProgressTowardsReadingGoalDivideByZero() {
        // ensure 0, and not an arithmetic exception, is returned
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0), 0);
    }
}

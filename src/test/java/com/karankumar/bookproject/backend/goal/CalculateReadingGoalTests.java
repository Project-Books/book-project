package com.karankumar.bookproject.backend.goal;

import com.karankumar.bookproject.annotations.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

@IntegrationTest
public class CalculateReadingGoalTests {

    @Test
    public void progressValueCorrect() {
        int booksToRead = new Random().nextInt(100);

        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0), 0); // == 0%
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(25, 5), 0.2); // < 100%
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(booksToRead, booksToRead), 1.0,
                "Books to read = " + booksToRead); // == 100%
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(booksToRead, (booksToRead + 1)), 1.0,
                "Books to read = " + booksToRead); // > 100%

        // ensure 0, and not an arithmetic exception, is returned
        Assertions.assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0), 0);
    }
}

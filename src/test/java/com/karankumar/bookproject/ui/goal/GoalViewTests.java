package com.karankumar.bookproject.ui.goal;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class GoalViewTests {
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

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

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

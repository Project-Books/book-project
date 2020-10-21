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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.karankumar.bookproject.util.ReadingGoalTestUtils.resetGoalService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@IntegrationTest
@DisplayName("ReadingGoalService should")
class ReadingGoalServiceTest {
    private final ReadingGoalService goalService;
    private ReadingGoal existingReadingGoal;

    @Autowired
    ReadingGoalServiceTest(ReadingGoalService goalService) {
        this.goalService = goalService;
    }

    @BeforeEach
    void beforeEach(){
        resetGoalService(goalService);
        initReadingGoalServiceTest();
    }

    private void initReadingGoalServiceTest() {
        existingReadingGoal = new ReadingGoal(20, ReadingGoal.GoalType.BOOKS);
        goalService.save(existingReadingGoal);
    }

    @Test
    void overwriteExistingGoalOnSave() {
        // given we have a reading goal
        assumeThat(goalService.count()).isOne();

        // when we create an new reading goal
        ReadingGoal newReadingGoal = new ReadingGoal(40, ReadingGoal.GoalType.PAGES);
        goalService.save(newReadingGoal);

        // then the new reading goal overrides the first one
        assertThat(goalService.count()).isOne();
        ReadingGoal actual = goalService.findAll().get(0);
        assertThat(newReadingGoal).isEqualToComparingFieldByField(actual);
    }

    @Test
    void deleteExistingGoal() {
        // given we have a reading goal
        assumeThat(goalService.count()).isOne();

        // when we delete that reading goal
        goalService.delete(existingReadingGoal);

        // then we no longer have a reading goal set
        assertThat(goalService.count()).isZero();
    }

    @Test
    void notSaveANullGoal() {
        // given we have a reading goal
        assumeThat(goalService.count()).isOne();

        // when we try to save null
        goalService.save(null);

        // then we have same number of records as before
        assertThat(goalService.count()).isOne();
    }
}

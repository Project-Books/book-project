/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
    void setUp(){
        resetGoalService(goalService);
        initReadingGoalServiceTest();
    }

    private void initReadingGoalServiceTest() {
        existingReadingGoal = new ReadingGoal(20, ReadingGoal.GoalType.BOOKS);
        goalService.save(existingReadingGoal);
    }

    @Test
    void overwriteExistingGoalOnSave() {
        // given
        assumeThat(goalService.count()).isOne();

        // when
        ReadingGoal newReadingGoal = new ReadingGoal(40, ReadingGoal.GoalType.PAGES);
        goalService.save(newReadingGoal);

        // then
        ReadingGoal actual = goalService.findAll().get(0);
        assertSoftly(softly -> {
            softly.assertThat(goalService.count()).isOne();
            softly.assertThat(newReadingGoal).isEqualToComparingFieldByField(actual);
        });
    }

    @Test
    void findExistingGoal() {
        // given
        assumeThat(goalService.count()).isOne();

        // when
        ReadingGoal actual = goalService.findById(existingReadingGoal.getId());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void deleteExistingGoal() {
        // given
        assumeThat(goalService.count()).isOne();

        // when
        goalService.delete(existingReadingGoal);

        // then
        assertThat(goalService.count()).isZero();
    }

    @Test
    @DisplayName("throw an exception on an attempt to delete a null goal")
    void throwExceptionOnAttemptToDeleteANullGoal() {
        assertThatThrownBy(() -> goalService.delete(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("throw an exception on an attempt to save a null goal")
    void throwExceptionOnAttemptToSaveANullGoal() {
        assertThatThrownBy(() -> goalService.save(null))
                .isInstanceOf(NullPointerException.class);
    }
}

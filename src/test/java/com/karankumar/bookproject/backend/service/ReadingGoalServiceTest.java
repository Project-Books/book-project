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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ReadingGoalServiceTest {
    @Test
    void expectSavingGoalToOverwriteExistingGoal(@NotNull @Autowired ReadingGoalService goalService) {
        // given
        ReadingGoal oldReadingGoal = new ReadingGoal(20, ReadingGoal.GoalType.BOOKS);
        goalService.save(oldReadingGoal);
        assertThat(goalService.count()).isOne();

        // when
        ReadingGoal newReadingGoal = new ReadingGoal(40, ReadingGoal.GoalType.PAGES);
        goalService.save(newReadingGoal);

        // then (check reading goal 2 overwrote reading goal 1)
        assertThat(goalService.count()).isOne();
        ReadingGoal actual = goalService.findAll().get(0);
        assertThat(newReadingGoal).isEqualToComparingFieldByField(actual);
    }
}

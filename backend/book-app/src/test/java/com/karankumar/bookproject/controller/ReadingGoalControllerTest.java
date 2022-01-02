/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.karankumar.bookproject.controller;

import com.karankumar.bookproject.model.ReadingGoal;
import com.karankumar.bookproject.service.ReadingGoalService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.karankumar.bookproject.model.ReadingGoal.GoalType.BOOKS;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestPropertySource(locations = "classpath:application-test.properties")
class ReadingGoalControllerTest {
    private final ReadingGoalService readingGoalService;
    private final ReadingGoalController readingGoalController;
    private final List<ReadingGoal> readingGoalList = new ArrayList<>();

    ReadingGoalControllerTest() {
        readingGoalService = mock(ReadingGoalService.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        readingGoalController = new ReadingGoalController(readingGoalService);

        this.readingGoalList.add(new ReadingGoal(5, BOOKS));
    }

    @Test
    void getCurrentReadingGoal_returnResponseStatusException_whenNoReadingGoalExists() {
        when(readingGoalService.findAll()).thenReturn(Collections.emptyList());

        ThrowableAssert.ThrowingCallable callable =
                readingGoalController::getExistingReadingGoal;

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(callable)
                .withMessage(String.format(
                        "%s \"%s\"",
                        HttpStatus.NOT_FOUND,
                        ReadingGoalController.READING_GOAL_NOT_FOUND
                ));
    }

    @Test
    void getPreviousReadingGoal_returnReadingGoal_whenPreviousGoalExists() {
        when(readingGoalService.findAll()).thenReturn(readingGoalList);

        assertThat(readingGoalController.getPreviousReadingGoals()).isEqualTo(readingGoalList);
    }

    @Test
    void getCurrentReadingGoal_returnCurrentGoal_whenReadingGoalExists() {
        when(readingGoalService.findAll()).thenReturn(readingGoalList);

        assertThat(readingGoalController.getExistingReadingGoal())
                .isEqualTo(readingGoalList.get(0));
    }

    @Test
    void getPreviousReadingGoals_returnSizeOfReadingGoals_whenNoGoalExist() {
        when(readingGoalService.findAll()).thenReturn(Collections.emptyList());
        assertThat(readingGoalController.getPreviousReadingGoals().size()).isZero();
    }
}

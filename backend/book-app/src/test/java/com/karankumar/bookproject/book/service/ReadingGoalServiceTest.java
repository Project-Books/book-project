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
*/

package com.karankumar.bookproject.book.service;

import com.karankumar.bookproject.goal.ReadingGoal;
import com.karankumar.bookproject.goal.ReadingGoalRepository;
import com.karankumar.bookproject.goal.ReadingGoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReadingGoalServiceTest {
  private ReadingGoalService underTest;
  private ReadingGoalRepository readingGoalRepository;

  @BeforeEach
  void setUp() {
    readingGoalRepository = mock(ReadingGoalRepository.class);
    underTest = new ReadingGoalService(readingGoalRepository);
  }

  @Test
  void findById_throwsNullPointerException_ifIdIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.findById(null));
    verify(readingGoalRepository, never()).findById(anyLong());
  }

  @Test
  void canFindByNonNullId() {
    // given
    Long id = 1L;

    // when
    underTest.findById(id);

    // then
    verify(readingGoalRepository).findById(id);
  }

  @Test
  void canFindAll() {
    underTest.findAll();
    verify(readingGoalRepository).findAll();
  }

  @Test
  void save_throwsNullPointerException_ifGoalIsNull() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> underTest.save(null));
    verify(readingGoalRepository, never()).save(any(ReadingGoal.class));
  }

  @Test
  void canSaveNonNullGoal() {
    // given
    ReadingGoal goal = new ReadingGoal(1, ReadingGoal.GoalType.BOOKS);

    // when
    underTest.save(goal);

    // then
    verify(readingGoalRepository).save(goal);
  }

  @Test
  void canDeleteAll() {
    underTest.deleteAll();
    verify(readingGoalRepository).deleteAll();
  }

  @Test
  void canCount() {
    underTest.count();
    verify(readingGoalRepository).count();
  }
}

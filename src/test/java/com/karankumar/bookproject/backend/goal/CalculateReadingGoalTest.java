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

package com.karankumar.bookproject.backend.goal;

import com.karankumar.bookproject.backend.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateReadingGoalTest {
    private final int BOOKS_TO_READ = 52;

    @Test
    void progressValueCorrect() {
        int toRead = 25;
        int read = 5;
        assertEquals( CalculateReadingGoal.calculateProgressTowardsReadingGoal(toRead, read), 0.2);
    }

    @Test
    void testProgressWhenGoalMet() {
        assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(BOOKS_TO_READ, BOOKS_TO_READ),
                1.0
        );
    }

    @Test
    void testProgressWhenGoalExceeded() {
        assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(BOOKS_TO_READ, (BOOKS_TO_READ + 1)),
                1.0
        );
    }

    @Test
    void testNoProgressMadeTowardsGoal() {
        assertEquals( CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0), 0);
    }

    @Test
    void testCalculateProgressTowardsReadingGoalDivideByZero() {
        // ensure 0, and not an arithmetic exception, is returned
        assertEquals(CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0), 0);
    }

    @Test
    void testHowFarAheadOrBehindSchedule(){
        Mockito.mockStatic(DateUtils.class);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(1);
        assertEquals(0,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,1));
        assertEquals(1,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,0));
        assertEquals(9,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,10));
        assertEquals(9,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,12));
        assertEquals(2,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,5));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(15);
        assertEquals(12,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,3));
        assertEquals(9,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,24));
        assertEquals(5,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,20));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(10);
        assertEquals(20,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,50));
        assertEquals(22,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,8));
        assertEquals(70,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,100));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(43);
        assertEquals(7,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(113,79));
        assertEquals(45,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(113,41));
        assertEquals(0,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(113,86));
    }
}

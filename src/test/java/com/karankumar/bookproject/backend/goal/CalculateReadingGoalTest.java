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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CalculateReadingGoalTest {
    private int booksToRead = 52;
    @Test
    public void progressValueCorrect() {
        int toRead = 25;
        int read = 5;
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(toRead, read),
                0.2
        );
    }

    @Test
    public void testProgressWhenGoalMet() {
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(booksToRead, booksToRead),
                1.0
        );
    }

    @Test
    public void testProgressWhenGoalExceeded() {
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(booksToRead, (booksToRead + 1)),
                1.0
        );
    }

    @Test
    public void testNoProgressMadeTowardsGoal() {
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0),
                0);
    }

    @Test
    public void testCalculateProgressTowardsReadingGoalDivideByZero() {
        // ensure 0, and not an arithmetic exception, is returned
        Assertions.assertEquals(
                CalculateReadingGoal.calculateProgressTowardsReadingGoal(5, 0),
                0
        );
    }

    /**
     * Checks whether ahead or behind with reading goal
     */
    @Test
    public void testHowFarAheadOrBehindSchedule(){
        Mockito.mockStatic(DateUtils.class);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(1);
        Assertions.assertEquals(0,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,1));
        Assertions.assertEquals(1,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,0));
        Assertions.assertEquals(9,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,10));
        Assertions.assertEquals(9,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,12));
        Assertions.assertEquals(2,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,5));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(15);
        Assertions.assertEquals(12,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,3));
        Assertions.assertEquals(9,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,24));
        Assertions.assertEquals(5,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(52,20));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(10);
        Assertions.assertEquals(20,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,50));
        Assertions.assertEquals(22,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,8));
        Assertions.assertEquals(70,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(199,100));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(43);
        Assertions.assertEquals(7,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(113,79));
        Assertions.assertEquals(45,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(113,41));
        Assertions.assertEquals(0,
                CalculateReadingGoal.howFarAheadOrBehindSchedule(113,86));
    }
}

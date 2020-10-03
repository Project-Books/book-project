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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.calculateProgressTowardsReadingGoal;
import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.howFarAheadOrBehindSchedule;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateReadingGoalTest {
    private final int BOOKS_TO_READ = 52;

    @Test
    void testProgressValueIsCorrect() {
        int toRead = 25;
        int read = 5;
        double expected = 0.2;
        double actual = calculateProgressTowardsReadingGoal(toRead, read);
        assertEquals(expected, actual);
    }

    @Test
    void testProgressValueIsCorrectWhenGoalMet() {
        double expected = 1.0;
        double actual = calculateProgressTowardsReadingGoal(BOOKS_TO_READ, BOOKS_TO_READ);
        assertEquals(expected, actual);
    }

    @Test
    void testProgressIsCorrectWhenGoalExceeded() {
        double expected = 1.0;
        double actual = calculateProgressTowardsReadingGoal(BOOKS_TO_READ, (BOOKS_TO_READ + 1));
        assertEquals(expected, actual);
    }

    @Test
    void testNoProgressMadeTowardsGoal() {
        assertThat(calculateProgressTowardsReadingGoal(5, 0)).isZero();
    }

    @Test
    @DisplayName("Ensure 0, and not an arithmetic exception, is returned")
    void testCalculateProgressTowardsReadingGoalDivideByZero() {
        int toRead = 5;
        int read = 0;

        SoftAssertions softly = new SoftAssertions();
        softly.assertThatCode(() ->
                calculateProgressTowardsReadingGoal(toRead, read)
        ).doesNotThrowAnyException();
        softly.assertThat(calculateProgressTowardsReadingGoal(toRead, read)).isZero();
        softly.assertAll();
    }

    // TODO: refactor this method to test for two boundary cases and one normal case
    @Test
    void testHowFarAheadOrBehindSchedule(){
        Mockito.mockStatic(DateUtils.class);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(1);
        assertThat(howFarAheadOrBehindSchedule(52, 1)).isZero();
        assertThat(howFarAheadOrBehindSchedule(52,0)).isOne();
        assertEquals(9, howFarAheadOrBehindSchedule(52,10));
        assertEquals(9, howFarAheadOrBehindSchedule(199,12));
        assertEquals(2, howFarAheadOrBehindSchedule(199,5));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(15);
        assertEquals(12, howFarAheadOrBehindSchedule(52,3));
        assertEquals(9, howFarAheadOrBehindSchedule(52,24));
        assertEquals(5, howFarAheadOrBehindSchedule(52,20));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(10);
        assertEquals(20, howFarAheadOrBehindSchedule(199,50));
        assertEquals(22, howFarAheadOrBehindSchedule(199,8));
        assertEquals(70, howFarAheadOrBehindSchedule(199,100));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(43);
        assertEquals(7, howFarAheadOrBehindSchedule(113,79));
        assertEquals(45, howFarAheadOrBehindSchedule(113,41));
        assertThat(howFarAheadOrBehindSchedule(113,86)).isZero();
    }

    //Test to test the change in the method booksToReadFromStartOfYear
    @Test
    void testBooksToReadFromStartOfYear(){
        int booksToReadThisYear = 104;
        int booksToReadFromStartOfYear = CalculateReadingGoal.booksToReadFromStartOfYear(booksToReadThisYear);
        assertEquals(2, booksToReadFromStartOfYear);
    }
}

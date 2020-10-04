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

import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.calculateProgressTowardsReadingGoal;
import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.howFarAheadOrBehindSchedule;
import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.booksToReadFromStartOfYear;
import com.karankumar.bookproject.backend.utils.DateUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CalculateReadingGoalTest {
    private final int BOOKS_TO_READ = 52;

    @Test
    void testProgressValueIsCorrect() {
        int toRead = 25;
        int read = 5;
        double expected = 0.2;
        double actual = calculateProgressTowardsReadingGoal(toRead, read);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testProgressValueIsCorrectWhenGoalMet() {
        double expected = 1.0;
        double actual = calculateProgressTowardsReadingGoal(BOOKS_TO_READ, BOOKS_TO_READ);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testProgressIsCorrectWhenGoalExceeded() {
        double expected = 1.0;
        double actual = calculateProgressTowardsReadingGoal(BOOKS_TO_READ, (BOOKS_TO_READ + 1));
        assertThat(actual).isEqualTo(expected);
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

    @Test
    void testHowFarAheadOrBehindSchedule(){
        Mockito.mockStatic(DateUtils.class);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(1);
        assertThat(howFarAheadOrBehindSchedule(52, 1)).isZero();
        assertThat(howFarAheadOrBehindSchedule(52,0)).isOne();
        assertEquals(9, howFarAheadOrBehindSchedule(52,10));

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(52);
        assertEquals(25, howFarAheadOrBehindSchedule(113,79));
        assertEquals(63, howFarAheadOrBehindSchedule(113,41));
        assertThat(howFarAheadOrBehindSchedule(113,104)).isZero();

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(26);
        assertEquals(20, howFarAheadOrBehindSchedule(40,20));
        assertEquals(55, howFarAheadOrBehindSchedule(40,55));
        assertThat(howFarAheadOrBehindSchedule(40,0)).isZero();
    }

    @Test
    void testBooksToReadFromStartOfYear(){
        double booksToReadFromStartOfYear = booksToReadFromStartOfYear(26);
        assertThat(booksToReadFromStartOfYear).isEqualTo(0.5);
    }
}

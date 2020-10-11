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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

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

    // TODO: refactor this method to test for two boundary cases and one normal case
    @Test
    void testHowFarAheadOrBehindSchedule(){
        Mockito.mockStatic(DateUtils.class);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(1);
        assertThat(howFarAheadOrBehindSchedule(52, 1)).isZero();
        assertThat(howFarAheadOrBehindSchedule(52,0)).isOne();
        assertThat(howFarAheadOrBehindSchedule(52,10)).isEqualTo(9);
        assertThat(howFarAheadOrBehindSchedule(199,12)).isEqualTo(9);
        assertThat(howFarAheadOrBehindSchedule(199,5)).isEqualTo(2);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(15);
        assertThat(howFarAheadOrBehindSchedule(52,3)).isEqualTo(12);
        assertThat(howFarAheadOrBehindSchedule(52,24)).isEqualTo(9);
        assertThat(howFarAheadOrBehindSchedule(52,20)).isEqualTo(5);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(10);
        assertThat(howFarAheadOrBehindSchedule(199,50)).isEqualTo(20);
        assertThat(howFarAheadOrBehindSchedule(199,8)).isEqualTo(22);
        assertThat(howFarAheadOrBehindSchedule(199,100)).isEqualTo(70);

        Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(43);
        assertThat(howFarAheadOrBehindSchedule(113,79)).isEqualTo(7);
        assertThat(howFarAheadOrBehindSchedule(113,41)).isEqualTo(45);
        assertThat(howFarAheadOrBehindSchedule(113,86)).isZero();
    }

    @Test
    void testBooksToReadFromStartOfYear(){
        double booksToReadFromStartOfYear = booksToReadFromStartOfYear(26);
        assertThat(booksToReadFromStartOfYear).isEqualTo(0.5);
    }
}

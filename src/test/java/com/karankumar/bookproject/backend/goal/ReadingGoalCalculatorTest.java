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

import static com.karankumar.bookproject.backend.goal.ReadingGoalCalculator.calculateProgressTowardsReadingGoal;
import static com.karankumar.bookproject.backend.goal.ReadingGoalCalculator.howFarAheadOrBehindSchedule;
import static com.karankumar.bookproject.backend.goal.ReadingGoalCalculator.booksToReadFromStartOfYear;
import com.karankumar.bookproject.backend.util.DateUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("ReadingGoalCalculator should")
class ReadingGoalCalculatorTest {
    private final int BOOKS_TO_READ = 52;

    @BeforeAll
    static void setUp() {
        Mockito.mockStatic(DateUtils.class);
    }

    @Test
    void calculateCorrectProgressValue() {
        int toRead = 25;
        int read = 5;
        double expected = 0.2;
        double actual = calculateProgressTowardsReadingGoal(toRead, read);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculateCorrectProgressValueWhenGoalMet() {
        double expected = 1.0;
        double actual = calculateProgressTowardsReadingGoal(BOOKS_TO_READ, BOOKS_TO_READ);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void calculateCorrectProgressValueWhenGoalExceeded() {
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
    void shouldNotThrowExceptionOnZeroDivision() {
        int toRead = 5;
        int read = 0;

        SoftAssertions softly = new SoftAssertions();
        softly.assertThatCode(() ->
                calculateProgressTowardsReadingGoal(toRead, read)
        ).doesNotThrowAnyException();
        softly.assertThat(calculateProgressTowardsReadingGoal(toRead, read)).isZero();
        softly.assertAll();
    }

    @Nested
    @DisplayName("show correct progress value for lower bound (1st week of the year) when")
    class LowerBoundProgressTests {
        @BeforeEach
        void setFirstWeek() {
            Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(1);
        }

        @Test
        void behindSchedule() {
            // one book behind schedule
            assertThat(howFarAheadOrBehindSchedule(52,0)).isOne();
        }

        @Test
        void onSchedule() {
            assertThat(howFarAheadOrBehindSchedule(52, 1)).isZero();
        }

        @Test
        void aheadOfSchedule() {
            // 9 books ahead of schedule
            assertEquals(9, howFarAheadOrBehindSchedule(52,10));
        }
    }

    @Nested
    @DisplayName("show correct progress value for normal case (half way into the year) when")
    class NormalCaseProgressTests {
        @BeforeEach
        void setWeekToMiddleOfYear() {
            Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(26);
        }

        @Test
        void behindSchedule() {
            // 10 books behind schedule
            assertEquals(10, howFarAheadOrBehindSchedule(40,10));
        }

        @Test
        void onSchedule() {
            assertEquals(20, howFarAheadOrBehindSchedule(40,20));
        }

        @Test
        void aheadOfSchedule() {
            // 30 books ahead of schedule
            assertEquals(30, howFarAheadOrBehindSchedule(40,30));
        }
    }

    @Nested
    @DisplayName("show correct progress value for upper bound (last week of the year) when")
    class UpperBoundProgressTests {
        @BeforeEach
        void setLastWeekOfYear() {
            Mockito.when(DateUtils.getCurrentWeekNumberOfYear()).thenReturn(52);
        }

        @Test
        void behindSchedule() {
            // 25 books behind schedule
            assertEquals(25, howFarAheadOrBehindSchedule(113,79));
        }

        @Test
        void onSchedule() {
            assertThat(howFarAheadOrBehindSchedule(113,104)).isZero();
        }

        @Test
        void aheadOfSchedule() {
            // 46 books ahead of schedule
            assertEquals(46, howFarAheadOrBehindSchedule(113,150));
        }
    }

    @Test
    void testBooksToReadFromStartOfYear(){
        double booksToReadFromStartOfYear = booksToReadFromStartOfYear(26);
        assertThat(booksToReadFromStartOfYear).isEqualTo(0.5);
    }
}

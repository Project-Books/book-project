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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // given
        int toRead = 5;
        int read = 0;

        // then
        assertSoftly(softly -> {
            softly.assertThatCode(() -> calculateProgressTowardsReadingGoal(toRead, read))
                  .doesNotThrowAnyException();
            softly.assertThat(calculateProgressTowardsReadingGoal(toRead, read)).isZero();
        });
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
            // given
            int booksToReadThisYear = 52;
            int booksReadThisYear = 0;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear,booksReadThisYear);

            // then
            int booksBehindSchedule = 1;
            assertThat(actual).isEqualTo(booksBehindSchedule);
        }

        @Test
        void onSchedule() {
            // given
            int booksToReadThisYear = 52;
            int booksReadThisYear = 1;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            assertThat(actual).isZero();
        }

        @Test
        void aheadOfSchedule() {
            // given
            int booksToReadThisYear = 52;
            int booksReadThisYear = 10;
            int booksAheadOfSchedule = 9;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            assertThat(actual).isEqualTo(booksAheadOfSchedule);
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
            // given
            int booksToReadThisYear = 40;
            int booksReadThisYear = 10;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            int booksBehindSchedule = 10;
            assertThat(actual).isEqualTo(booksBehindSchedule);
        }

        @Test
        void onSchedule() {
            // given
            int booksToReadThisYear = 40;
            int booksReadThisYear = 20;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            int expected = 20;
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void aheadOfSchedule() {
            // given
            int booksToReadThisYear = 40;
            int booksReadThisYear = 30;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            int booksAheadOfSchedule = 30;
            assertThat(actual).isEqualTo(booksAheadOfSchedule);
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
            // given
            int booksToReadThisYear = 113;
            int booksReadThisYear = 79;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            int booksBehindSchedule = 25;
            assertEquals(booksBehindSchedule, actual);
        }

        @Test
        void onSchedule() {
            // given
            int booksToReadThisYear = 113;
            int booksReadThisYear = 104;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            assertThat(actual).isZero();
        }

        @Test
        void aheadOfSchedule() {
            // given
            int booksToReadThisYear = 113;
            int booksReadThisYear = 150;

            // when
            int actual = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);

            // then
            int booksAheadOfSchedule = 46;
            assertThat(actual).isEqualTo(booksAheadOfSchedule);
        }
    }

    @Test
    void testBooksToReadFromStartOfYear(){
        // given
        int booksToReadThisYear = 26;

        // when
        double booksToReadFromStartOfYear = booksToReadFromStartOfYear(booksToReadThisYear);

        // then
        double expected = 0.5;
        assertThat(booksToReadFromStartOfYear).isEqualTo(expected);
    }
}

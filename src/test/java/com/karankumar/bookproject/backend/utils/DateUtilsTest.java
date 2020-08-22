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

package com.karankumar.bookproject.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtilsTest {
    @BeforeAll
    static void setup() {
        Mockito.mockStatic(DateUtils.TimeUtils.class);
        Mockito.when(DateUtils.TimeUtils.getWeekFields()).thenReturn(WeekFields.of(Locale.getDefault()));
    }

    private int mockFirstWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.now())
               .thenReturn(LocalDateTime.of(2020, 1, 1, 1, 1)
        );
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    private int mockFortiethWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.now())
               .thenReturn(LocalDateTime.of(2020, 10, 1, 1, 1));
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    private int mockLastWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.now())
               .thenReturn(LocalDateTime.of(2020, 12, 25, 1, 1));
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    @Test
    void testCurrentWeekNumberOfYearAndWeeksLeftInYear() {
        Assertions.assertEquals(1, mockFirstWeekOfYear());
        Assertions.assertEquals(40, mockFortiethWeekOfYear());
        Assertions.assertEquals(52, mockLastWeekOfYear());

    }

    @Test
    void testWeeksLeftInYearFromCurrentWeek() {
        Assertions.assertEquals(calculateWeeksLeftInYear(1),
                DateUtils.calculateWeeksLeftInYearFromCurrentWeek(mockFirstWeekOfYear()));

        Assertions.assertEquals(calculateWeeksLeftInYear(mockFortiethWeekOfYear()),
                DateUtils.calculateWeeksLeftInYearFromCurrentWeek(mockFortiethWeekOfYear()));

        Assertions.assertEquals(calculateWeeksLeftInYear(mockLastWeekOfYear()),
                DateUtils.calculateWeeksLeftInYearFromCurrentWeek(mockLastWeekOfYear()));
    }

    private int calculateWeeksLeftInYear(int currentWeekNumber) {
        int weeksInYear = 52;
        return weeksInYear - currentWeekNumber;
    }
}

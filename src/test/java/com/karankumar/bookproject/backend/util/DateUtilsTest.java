/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
@DisplayName("DateUtils should")
class DateUtilsTest {

    private static final LocalDate MOCK_CURRENT_DATE = LocalDate.of(2020, 6, 1);
    private static MockedStatic<DateUtils.TimeUtils> mockDateUtils;

    @BeforeAll
    static void setup() {
        mockDateUtils = Mockito.mockStatic(DateUtils.TimeUtils.class);
        Mockito.when(DateUtils.TimeUtils.getWeekFields())
               .thenReturn(WeekFields.of(Locale.getDefault()));
        Mockito.when(DateUtils.TimeUtils.currentDate())
               .thenReturn(MOCK_CURRENT_DATE);
    }

    @AfterAll
    static void tearDown() {
        mockDateUtils.close();
    }

    private int mockFirstWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.currentDateTime())
               .thenReturn(LocalDateTime.of(2020, 1, 1, 1, 1)
        );
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    private int mockFortiethWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.currentDateTime())
               .thenReturn(LocalDateTime.of(2020, 10, 1, 1, 1));
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    private int mockLastWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.currentDateTime())
               .thenReturn(LocalDateTime.of(2020, 12, 25, 1, 1));
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    @Test
    void correctlyGetWeekNumberOfYear() {
        assertThat(mockFirstWeekOfYear()).isOne();
        assertThat(mockFortiethWeekOfYear()).isEqualTo(40);
        assertThat(mockLastWeekOfYear()).isEqualTo(52);
    }

    @Test
    void correctlyGetWeeksLeftInYearFromCurrentWeek() {
        assertThat(DateUtils.calculateWeeksLeftInYearFromCurrentWeek(mockFirstWeekOfYear()))
                .isEqualTo(calculateWeeksLeftInYear(1));

        assertThat(DateUtils.calculateWeeksLeftInYearFromCurrentWeek(mockFortiethWeekOfYear()))
                .isEqualTo(calculateWeeksLeftInYear(mockFortiethWeekOfYear()));

        assertThat(DateUtils.calculateWeeksLeftInYearFromCurrentWeek(mockLastWeekOfYear()))
                .isEqualTo(calculateWeeksLeftInYear(mockLastWeekOfYear()));
    }

    @Test
    void correctlyCheckIfDateIsInCurrentYear() {
        LocalDate lastDayOfYear = LocalDate.of(MOCK_CURRENT_DATE.getYear(), 12, 31);

        assertSoftly(softly -> {
            softly.assertThat(DateUtils.dateIsInCurrentYear(MOCK_CURRENT_DATE)).isTrue();
            softly.assertThat(DateUtils.dateIsInCurrentYear(MOCK_CURRENT_DATE.minusDays(1)))
                  .isTrue();
            softly.assertThat(DateUtils.dateIsInCurrentYear(lastDayOfYear)).isTrue();
            softly.assertThat(DateUtils.dateIsInCurrentYear(MOCK_CURRENT_DATE.minusYears(1)))
                  .isFalse();
            softly.assertThat(DateUtils.dateIsInCurrentYear(MOCK_CURRENT_DATE.plusYears(1)))
                  .isFalse();
        });
    }

    @Test
    void correctlyCheckIfDateIsInFuture() {
        assertSoftly(softly -> {
            softly.assertThat(DateUtils.isDateInFuture(MOCK_CURRENT_DATE.plusYears(1))).isTrue();
            softly.assertThat(DateUtils.isDateInFuture(MOCK_CURRENT_DATE.plusDays(1))).isTrue();
            softly.assertThat(DateUtils.isDateInFuture(MOCK_CURRENT_DATE)).isFalse();
            softly.assertThat(DateUtils.isDateInFuture(MOCK_CURRENT_DATE.minusDays(1))).isFalse();
            softly.assertThat(DateUtils.isDateInFuture(MOCK_CURRENT_DATE.minusMonths(1))).isFalse();
        });
    }

    private int calculateWeeksLeftInYear(int currentWeekNumber) {
        int weeksInYear = 52;
        return weeksInYear - currentWeekNumber;
    }
}

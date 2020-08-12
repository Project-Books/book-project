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
        Mockito.when(DateUtils.TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 1, 1, 1, 1));
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    private int mockFortiethWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 10, 1, 1, 1));
        return DateUtils.getCurrentWeekNumberOfYear();
    }

    private int mockLastWeekOfYear() {
        Mockito.when(DateUtils.TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 12, 25, 1, 1));
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

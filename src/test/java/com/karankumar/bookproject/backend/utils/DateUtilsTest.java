package com.karankumar.bookproject.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtilsTest {
    /**
     * Test the current week and remaining weeks
     */
    @Test
    public void testCurrentWeekAndLeftWeek() {
        Mockito.mockStatic(TimeUtils.class);
        Mockito.when(TimeUtils.getWeekFields()).thenReturn(WeekFields.of(Locale.getDefault()));
        Mockito.when(TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 1, 1, 1, 1));

        int firstWeekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        Assertions.assertEquals(1, firstWeekOfYear);
        Assertions.assertEquals(calculateWeeksLeftInYear(1),
                DateUtils.calculateWeeksLeftInYearFromCurrentWeek(firstWeekOfYear));

        Mockito.when(TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 10, 1, 1, 1));
        int fortiethsWeekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        Assertions.assertEquals(40, fortiethsWeekOfYear);
        Assertions.assertEquals(calculateWeeksLeftInYear(fortiethsWeekOfYear),
                DateUtils.calculateWeeksLeftInYearFromCurrentWeek(fortiethsWeekOfYear));

        Mockito.when(TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 12, 25, 1, 1));
        int lastWeekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        Assertions.assertEquals(52, lastWeekOfYear);
        Assertions.assertEquals(calculateWeeksLeftInYear(lastWeekOfYear),
                DateUtils.calculateWeeksLeftInYearFromCurrentWeek(lastWeekOfYear));
    }

    private int calculateWeeksLeftInYear(int currentWeekNumber) {
        return 52 - currentWeekNumber;
    }

}

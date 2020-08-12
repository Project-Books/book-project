package com.karankumar.bookproject.backend.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
public class DateUtilsTest {

    /**
     * Test the weeks in years it should be 52
     */
    @Test
    public void testWeeksInYear() {
        Assertions.assertEquals(52, DateUtils.getWeeksInYear());
    }

    /**
     * Test the current week and remaining weeks
     */
    @Test
    @PrepareForTest({ TimeUtils.class})
    public void testCurrentWeekAndLeftWeek() {
        PowerMockito.mockStatic(TimeUtils.class);
        PowerMockito.when(TimeUtils.getWeekFields()).thenReturn(WeekFields.of(Locale.getDefault()));
        PowerMockito.when(TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 1, 1, 1, 1));
        int firstWeekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        Assertions.assertEquals(1, firstWeekOfYear);
        Assertions.assertEquals(52 - firstWeekOfYear, DateUtils.calculateWeeksLeftInYearFromCurrentWeek(firstWeekOfYear));
        PowerMockito.when(TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 10, 1, 1, 1));
        int fortiethsWeekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        Assertions.assertEquals(40, fortiethsWeekOfYear);
        Assertions.assertEquals(52 - fortiethsWeekOfYear, DateUtils.calculateWeeksLeftInYearFromCurrentWeek(fortiethsWeekOfYear));
        PowerMockito.when(TimeUtils.now()).thenReturn(LocalDateTime.of(2020, 12, 25, 1, 1));
        int lastWeekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        Assertions.assertEquals(52, lastWeekOfYear);
        Assertions.assertEquals(52 - lastWeekOfYear, DateUtils.calculateWeeksLeftInYearFromCurrentWeek(lastWeekOfYear));
    }

}

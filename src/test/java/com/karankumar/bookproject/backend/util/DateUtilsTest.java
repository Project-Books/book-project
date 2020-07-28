package com.karankumar.bookproject.backend.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@SpringBootTest
class DateUtilsTest {

    private DateUtils goalUtils;

    @MockBean
    private TimeUtils timeUtils;

    @BeforeEach
    public void setup(@Autowired DateUtils goalUtils) {
        Assumptions.assumeTrue(goalUtils != null);
        this.goalUtils = goalUtils;
        MockitoAnnotations.initMocks(timeUtils);
    }

    /**
     * Test the weeks in years it should be 52
     */
    @Test
    public void testWeeksInYear() {
        Assertions.assertEquals(52, goalUtils.getWeeksInYear());
    }

    /**
     * Test the current week and remaining weeks
     */
    @Test
    public void testCurrentWeekAndLeftWeek() {
        Mockito.when(timeUtils.getWeekFields()).thenReturn(WeekFields.of(Locale.getDefault()));
        Mockito.when(timeUtils.now()).thenReturn(LocalDateTime.of(2020, 1, 1, 1, 1));
        int firstWeekOfYear = goalUtils.getWeekOfYear();
        Assertions.assertEquals(1, firstWeekOfYear);
        Assertions.assertEquals(52 - firstWeekOfYear, goalUtils.getWeeksLeftInYear(firstWeekOfYear));
        Mockito.when(timeUtils.now()).thenReturn(LocalDateTime.of(2020, 10, 1, 1, 1));
        int fortiethsWeekOfYear = goalUtils.getWeekOfYear();
        Assertions.assertEquals(40, fortiethsWeekOfYear);
        Assertions.assertEquals(52 - fortiethsWeekOfYear, goalUtils.getWeeksLeftInYear(fortiethsWeekOfYear));
        Mockito.when(timeUtils.now()).thenReturn(LocalDateTime.of(2020, 12, 25, 1, 1));
        int lastWeekOfYear = goalUtils.getWeekOfYear();
        Assertions.assertEquals(52, lastWeekOfYear);
        Assertions.assertEquals(52 - lastWeekOfYear, goalUtils.getWeeksLeftInYear(lastWeekOfYear));
    }

}

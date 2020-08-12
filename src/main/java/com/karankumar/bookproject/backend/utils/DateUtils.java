package com.karankumar.bookproject.backend.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;

@Component
public class DateUtils {
    public static final int WEEKS_IN_YEAR = 52;

    private DateUtils() {}

    public static int getCurrentWeekNumberOfYear() {
        LocalDateTime now = TimeUtils.now();
        TemporalField week = TimeUtils.getWeekFields().weekOfWeekBasedYear();
        return now.get(week);
    }

    public static int calculateWeeksLeftInYearFromCurrentWeek(int currentWeekNumberOfYear) {
        return (WEEKS_IN_YEAR - currentWeekNumberOfYear);
    }
}

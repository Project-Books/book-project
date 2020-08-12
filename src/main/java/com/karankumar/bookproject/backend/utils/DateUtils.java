package com.karankumar.bookproject.backend.utils;

import com.helger.commons.annotation.VisibleForTesting;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

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

    @VisibleForTesting
    static class TimeUtils {
        static LocalDateTime now() {
            return LocalDateTime.now();
        }

        static WeekFields getWeekFields() {
            return WeekFields.of(Locale.getDefault());
        }
    }
}

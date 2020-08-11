package com.karankumar.bookproject.backend.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;

/**
 * An utils to provide Date related common functions.
 */
@Component
public class DateUtils {

    private static final int WEEKS_IN_YEAR = 52;

    /**
     * @return the current week number of the year
     */
    public static int getWeekOfYear() {
        LocalDateTime now = TimeUtils.now();
        TemporalField week = TimeUtils.getWeekFields().weekOfWeekBasedYear();
        return now.get(week);
    }

    /**
     * @param weekOfYear the current week number of the year
     * @return the number of weeks left in the year from the current week
     */
    public static int getWeeksLeftInYear(int weekOfYear) {
        return (WEEKS_IN_YEAR - weekOfYear);
    }

    /**
     * @return the number of weeks left in the year from the current week
     */
    public static int getWeeksInYear() {
        return WEEKS_IN_YEAR;
    }


}

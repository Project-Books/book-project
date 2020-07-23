package com.karankumar.bookproject.backend.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Component
public class DateUtils {

    private static final int WEEKS_IN_YEAR = 52;

    /**
     * @return the current week number of the year
     */
    public int getWeekOfYear() {
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return now.get(weekFields.weekOfWeekBasedYear());
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

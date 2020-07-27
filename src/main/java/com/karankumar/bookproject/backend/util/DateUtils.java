package com.karankumar.bookproject.backend.util;

import org.springframework.stereotype.Component;

/**
 * An utils to provide Date related common functions.
 */
@Component
public class DateUtils {

    private static final int WEEKS_IN_YEAR = 52;

    private TimeUtils timeUtils;

    public DateUtils(TimeUtils timeUtils) {
        this.timeUtils = timeUtils;
    }

    /**
     * @return the current week number of the year
     */
    public int getWeekOfYear() {
        return timeUtils.now().get(timeUtils.getWeekFields().weekOfWeekBasedYear());
    }

    /**
     * @param weekOfYear the current week number of the year
     * @return the number of weeks left in the year from the current week
     */
    public int getWeeksLeftInYear(int weekOfYear) {
        return (WEEKS_IN_YEAR - weekOfYear);
    }

    /**
     * @return the number of weeks left in the year from the current week
     */
    public int getWeeksInYear() {
        return WEEKS_IN_YEAR;
    }


}

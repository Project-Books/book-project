/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.utils;

import com.helger.commons.annotation.VisibleForTesting;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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

    public static boolean dateIsInCurrentYear(@NotNull LocalDate date) {
        return date.getYear() == LocalDate.now().getYear();
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

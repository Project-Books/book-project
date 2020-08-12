package com.karankumar.bookproject.backend.utils;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class TimeUtils {
    private TimeUtils() {}

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static WeekFields getWeekFields() {
        return WeekFields.of(Locale.getDefault());
    }
}

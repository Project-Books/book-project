package com.karankumar.bookproject.backend.utils;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Component
public class TimeUtils {
    private TimeUtils() {}

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static WeekFields getWeekFields() {
        return WeekFields.of(Locale.getDefault());
    }
}

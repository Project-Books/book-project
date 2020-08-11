package com.karankumar.bookproject.backend.utils;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * An utils to provide time related common functions.
 */
@Component
public class TimeUtils {

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static WeekFields getWeekFields() {
        return WeekFields.of(Locale.getDefault());
    }
}

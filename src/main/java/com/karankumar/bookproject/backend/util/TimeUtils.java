package com.karankumar.bookproject.backend.util;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * An utils to provide time related common functions.
 */
@Component
public class TimeUtils {
    public LocalDateTime now(){
        return LocalDateTime.now();
    }

    public WeekFields getWeekFields(){
        return WeekFields.of(Locale.getDefault());
    }
}

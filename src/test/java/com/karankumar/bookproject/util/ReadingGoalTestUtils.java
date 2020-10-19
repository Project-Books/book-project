package com.karankumar.bookproject.util;

import com.karankumar.bookproject.backend.service.ReadingGoalService;

public class ReadingGoalTestUtils {
    private ReadingGoalTestUtils() {}

    public static void resetGoalService(ReadingGoalService goalService) {
        goalService.deleteAll();
    }
}

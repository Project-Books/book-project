package com.karankumar.bookproject.util;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.ReadingGoalService;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;

public class ReadingGoalTestUtils {
    private ReadingGoalTestUtils() {}

    public static void resetGoalService(ReadingGoalService goalService) {
        goalService.deleteAll();
    }
}

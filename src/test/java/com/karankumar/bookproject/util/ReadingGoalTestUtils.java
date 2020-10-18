package com.karankumar.bookproject.util;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.ReadingGoalService;

import java.util.List;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.backend.util.DateUtils.dateIsInCurrentYear;

public class ReadingGoalTestUtils {
    private ReadingGoalTestUtils() {}

    static boolean wasBookReadThisYearAndHasPageCount(Book book) {
        return hasBookBeenRead(book) && book.getNumberOfPages() != null &&
                dateIsInCurrentYear(book.getDateFinishedReading());
    }

    static boolean hasBookBeenRead(Book book) {
        PredefinedShelf.ShelfName predefinedShelfName =
                book.getPredefinedShelf().getPredefinedShelfName();
        return predefinedShelfName.equals(READ) && book.getDateFinishedReading() != null;
    }

    public static int findHowManyBooksInReadShelfWithFinishDate(List<Book> books) {
        int count = 0;
        for (Book book : books) {
            if (hasBookBeenRead(book)) {
                count++;
            }
        }
        return count;
    }

    public static int findHowManyPagesInReadShelfWithFinishDate(List<Book> books) {
        int count = 0;
        for (Book book : books) {
            if (wasBookReadThisYearAndHasPageCount(book)) {
                count += book.getNumberOfPages();
            }
        }
        return count;
    }

    public static void resetGoalService(ReadingGoalService goalService) {
        goalService.deleteAll();
    }
}

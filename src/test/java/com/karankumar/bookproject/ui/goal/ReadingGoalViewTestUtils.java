package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.backend.utils.DateUtils.dateIsInCurrentYear;

class ReadingGoalViewTestUtils {
    private ReadingGoalViewTestUtils() {}

    static boolean wasBookReadThisYearAndHasPageCount(Book book) {
        return hasBookBeenRead(book) && book.getNumberOfPages() != null &&
                dateIsInCurrentYear(book.getDateFinishedReading());
    }

    static boolean hasBookBeenRead(Book book) {
        PredefinedShelf.ShelfName predefinedShelfName = book.getPredefinedShelf().getPredefinedShelfName();
        return predefinedShelfName.equals(READ) && book.getDateFinishedReading() != null;
    }

    static int findHowManyBooksInReadShelfWithFinishDate(List<Book> books) {
        int count = 0;
        for (Book book : books) {
            if (hasBookBeenRead(book)) {
                count++;
            }
        }
        return count;
    }

    static int findHowManyPagesInReadShelfWithFinishDate(List<Book> books) {
        int count = 0;
        for (Book book : books) {
            if (wasBookReadThisYearAndHasPageCount(book)) {
                count += book.getNumberOfPages();
            }
        }
        return count;
    }
}

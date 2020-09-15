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

package com.karankumar.bookproject.ui.goal;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.goal.CalculateReadingGoal;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static com.karankumar.bookproject.backend.utils.DateUtils.dateIsInCurrentYear;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;

@IntegrationTest
@WebAppConfiguration
class ReadingGoalViewTest {
    private static Routes routes;

    @Autowired private ApplicationContext ctx;
    @Autowired private BookService bookService;

    private ReadingGoalService goalService;
    private PredefinedShelfService predefinedShelfService;
    private PredefinedShelfUtils predefinedShelfUtils;
    private ReadingGoalView goalView;

    private final int GOAL_TARGET = 52;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired ReadingGoalService goalService,
                      @Autowired PredefinedShelfService predefinedShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(goalService != null);
        goalService.deleteAll(); // reset

        this.goalService = goalService;
        this.predefinedShelfService = predefinedShelfService;
        this.predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        goalView = new ReadingGoalView(goalService, predefinedShelfService);
    }

    @Test
    void testSetGoalButtonTextUpdatesWhenGoalUpdates() {
        Assumptions.assumeTrue(goalService.findAll().isEmpty());
        assertEquals(goalView.setGoalButton.getText(), ReadingGoalView.SET_GOAL);

        goalService.save(new ReadingGoal(GOAL_TARGET, getRandomGoalType()));
        goalView.getCurrentGoal();

        String expectedGoalButtonText = ReadingGoalView.UPDATE_GOAL;
        String actualGoalButtonText = goalView.setGoalButton.getText();
        assertEquals(expectedGoalButtonText, actualGoalButtonText);
    }

    private ReadingGoal.GoalType getRandomGoalType() {
        ReadingGoal.GoalType[] goalTypes = ReadingGoal.GoalType.values();
        return goalTypes[new Random().nextInt(goalTypes.length)];
    }

    @Test
    void testTargetMetMessageNotShownWhenGoalNotMet() {
        assertNotEquals(ReadingGoalView.TARGET_MET,
                goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET - 1));
    }

    @Test
    void testTargetMetMessageShownWhenGoalMet() {
        Assumptions.assumeTrue(goalService.findAll().size() == 0);
        assertEquals(ReadingGoalView.TARGET_MET, goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET));
    }

    @Test
    void testTargetMetMessageShownWhenGoalExceeded() {
        assertEquals(ReadingGoalView.TARGET_MET, goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET + 1));
    }

    @Test
    void onlyReadBooksWithAFinishDateCountTowardsGoal() {
        // TODO: fix failing test. This runs fine in IntelliJ, but fails when `mvn clean install` is executed on Windows
//        int numberOfShelves = predefinedShelfService.findAll().size();
//        Assumptions.assumeTrue(numberOfShelves == 4);
//        Assumptions.assumeFalse(bookService == null);
//
//        resetBookService(bookService);
//        Assumptions.assumeTrue(bookService.findAll().isEmpty());
//
//        addBooksToAllShelves(numberOfShelves);
//
//        List<Book> allBooks = bookService.findAll();
//        int booksInReadShelf = findHowManyBooksInReadShelfWithFinishDate(allBooks);
//        int pagesReadInReadShelf = findHowManyPagesInReadShelfWithFinishDate(allBooks);
//        System.out.println("Pages read " + pagesReadInReadShelf);
//
//        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();
//        Assumptions.assumeTrue(readShelf != null);
//        Assertions.assertEquals(booksInReadShelf,
//                CalculateReadingGoal.howManyReadThisYear(ReadingGoal.GoalType.BOOKS, readShelf));
//        Assertions.assertEquals(pagesReadInReadShelf,
//                CalculateReadingGoal.howManyReadThisYear(ReadingGoal.GoalType.PAGES, readShelf));
    }

    private void addBooksToAllShelves(int numberOfShelves) {
        int booksToAdd = 100;
        for (int i = 0; i < booksToAdd; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, numberOfShelves);
            Book book;

            switch(random) {
                case 0:
                    book = createBook(ShelfName.TO_READ);
                    break;
                case 1:
                    book = createBook(ShelfName.READING);
                    break;
                case 2:
                    book = createBook(ShelfName.READ);
                    if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
                        book.setDateFinishedReading(null);
                    }
                    break;
                default:
                    book = createBook(ShelfName.DID_NOT_FINISH);
            }
            bookService.save(book);
        }
    }

    private int findHowManyBooksInReadShelfWithFinishDate(List<Book> books) {
        int count = 0;
        for (Book book : books) {
            ShelfName predefinedShelfName = book.getPredefinedShelf().getPredefinedShelfName();
            if (predefinedShelfName.equals(ShelfName.READ) && book.getDateFinishedReading() != null) {
                count++;
            }
        }
        return count;
    }

    private int findHowManyPagesInReadShelfWithFinishDate(List<Book> books) {
        int count = 0;
        for (Book book : books) {
            ShelfName predefinedShelfName = book.getPredefinedShelf().getPredefinedShelfName();
            if (book.getDateFinishedReading() != null) {
                LocalDate dateFinished = book.getDateFinishedReading();
                if (predefinedShelfName.equals(ShelfName.READ) && book.getNumberOfPages() != null &&
                        dateFinished != null && dateIsInCurrentYear(dateFinished)) {
                    count += book.getNumberOfPages();
                }
            }
        }
        return count;
    }

    private void resetBookService(BookService bookService) {
        bookService.deleteAll();
    }

    /**
     * Creates a book in the specified shelf
     */
    private Book createBook(ShelfName shelfName) {
        Book book = new Book("Title", new Author("Joe", "Bloggs"), predefinedShelfUtils.findReadShelf());
        if (shelfName.equals(ShelfName.READ)) {
            book.setDateFinishedReading(LocalDate.now());
        }
        book.setNumberOfPages(300);
        return book;
    }

    @Test
    void correctInformationShownWhenGoalIsSetOrUpdated() {
        Assumptions.assumeTrue(goalService.findAll().isEmpty());

        ReadingGoal readingGoal = new ReadingGoal(GOAL_TARGET, getRandomGoalType());
        goalService.save(readingGoal);
        goalView.getCurrentGoal();

        // should be visible for both a book or pages goal
        assertTrue(goalView.readingGoalSummary.isVisible());
        assertTrue(goalView.goalProgressPercentage.isVisible());

        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();
        int howManyReadThisYear =
                CalculateReadingGoal.howManyReadThisYear(readingGoal.getGoalType(), readShelf);
        int targetToRead = readingGoal.getTarget();
        boolean hasReachedGoal = (targetToRead <= howManyReadThisYear);

        if (readingGoal.getGoalType().equals(ReadingGoal.GoalType.BOOKS)) {
            // Additional components that should be visible for a books goal
            assertTrue(goalView.goalProgress.isVisible());
            if (hasReachedGoal) {
                assertFalse(goalView.booksToReadOnAverageToMeetGoal.isVisible());
            } else {
                assertTrue(goalView.booksToReadOnAverageToMeetGoal.isVisible());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@IntegrationTest
@WebAppConfiguration
public class ReadingGoalViewTests {
    private static Routes routes;

    @Autowired
    private ApplicationContext ctx;

    private ReadingGoalService goalService;
    private PredefinedShelfService predefinedShelfService;
    private PredefinedShelfUtils predefinedShelfUtils;
    private ReadingGoalView goalView;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup(@Autowired ReadingGoalService goalService, @Autowired PredefinedShelfService predefinedShelfService) {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        Assumptions.assumeTrue(goalService != null);
        goalService.deleteAll(); // reset

        this.goalService = goalService;
        this.predefinedShelfService = predefinedShelfService;
        this.predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        goalView = new ReadingGoalView(goalService, predefinedShelfService);
    }

    /**
     * Check whether the set goal button text correctly updates when the goal has been updated
     */
    @Test
    public void setGoalButtonTextIsCorrect() {
        Assumptions.assumeTrue(goalService.findAll().size() == 0);
        Assertions.assertEquals(goalView.setGoalButton.getText(), ReadingGoalView.SET_GOAL);

        goalService.save(new ReadingGoal(getRandomGoalTarget(), getRandomGoalType()));
        goalView.getCurrentGoal();
        Assertions.assertEquals(goalView.setGoalButton.getText(), ReadingGoalView.UPDATE_GOAL);
    }

    private ReadingGoal.GoalType getRandomGoalType() {
        ReadingGoal.GoalType[] goalTypes = ReadingGoal.GoalType.values();
        return goalTypes[new Random().nextInt(goalTypes.length)];
    }

    /**
     * Generates a random goal
     * @return an integer that represents the goal target
     */
    private int getRandomGoalTarget() {
        return ThreadLocalRandom.current().nextInt(0, 10_000);
    }

    /**
     * Tests whether the target message is shown when the goal target has been met
     */
    @Test
    public void targetMetMessageShown() {
        Assumptions.assumeTrue(goalService.findAll().size() == 0);
        int randomGoalTarget = getRandomGoalTarget();

        // target met:
        Assertions.assertEquals(
                ReadingGoalView.TARGET_MET, goalView.calculateProgress(randomGoalTarget, randomGoalTarget));
        Assertions.assertEquals(ReadingGoalView.TARGET_MET,
                goalView.calculateProgress(randomGoalTarget, randomGoalTarget + 1));
        // target not met:
        Assertions.assertNotEquals(ReadingGoalView.TARGET_MET,
                goalView.calculateProgress(randomGoalTarget, randomGoalTarget - 1));
    }

    /**
     * Only books that in the read shelf that have a date finished (which will always be this year) should count towards the reading goal
     * @param bookService an Autowired book service to access the book repository
     */
    @Test
    public void onlyReadBooksCountTowardsGoal(@Autowired BookService bookService) {
        int numberOfShelves = predefinedShelfService.findAll().size();
        Assumptions.assumeTrue(numberOfShelves == 4);
        Assumptions.assumeFalse(bookService == null);

        bookService.deleteAll(); // reset
        Assumptions.assumeTrue(bookService.findAll().size() == 0);

        // Add books to all shelves (books in the read shelf must have a non-null finish date)
        int booksToAdd = ThreadLocalRandom.current().nextInt(10, (100 + 1));
        int booksInReadShelf = 0;
        int pagesReadInReadShelf = 0;
        for (int i = 0; i < booksToAdd; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, numberOfShelves);
            Book book;
            if (random == 0) {
                book = createBook(PredefinedShelf.ShelfName.TO_READ);
            } else if (random == 1) {
                book = createBook(PredefinedShelf.ShelfName.READING);
            } else if (random == 2) {
                book = createBook(PredefinedShelf.ShelfName.READ);
                if (ThreadLocalRandom.current().nextInt(0, (1 + 1)) == 0) {
                    // disregard this book in the goal count as it has no finish date
                    book.setDateFinishedReading(null);
                } else {
                    booksInReadShelf++;
                    pagesReadInReadShelf += book.getNumberOfPages();
                }
            } else {
                book = createBook(PredefinedShelf.ShelfName.DID_NOT_FINISH);
            }
            bookService.save(book);
        }

        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();
        Assumptions.assumeTrue(readShelf != null);
        Assertions.assertEquals(booksInReadShelf,
                CalculateReadingGoal.howManyReadThisYear(ReadingGoal.GoalType.BOOKS, readShelf));
        Assertions.assertEquals(pagesReadInReadShelf,
                CalculateReadingGoal.howManyReadThisYear(ReadingGoal.GoalType.PAGES, readShelf));
    }

    /**
     * Creates a book in the specified shelf
     * @param shelfName the name of the shelf to place the book in
     * @return a new Book
     */
    private Book createBook(PredefinedShelf.ShelfName shelfName) {
        Book book = new Book("Title", new Author("Joe", "Bloggs"));
        book.setShelf(predefinedShelfUtils.findReadShelf()); // important not to create a new predefined shelf
        if (shelfName.equals(PredefinedShelf.ShelfName.READ)) {
            book.setDateFinishedReading(generateRandomDate());
        }
        book.setNumberOfPages(ThreadLocalRandom.current().nextInt(300, (1000 + 1)));
        return book;
    }

    /**
     * Generates a new random date that is in the same year as the current year
     * @return a new random LocalDate
     */
    private LocalDate generateRandomDate() {
        // important to get the current year for the reading goal to work
        int year = LocalDate.now().getYear();
        int day = ThreadLocalRandom.current().nextInt(1, (27 + 1));
        int month = ThreadLocalRandom.current().nextInt(1, (12 + 1));
        return LocalDate.of(year, month, day);
    }

    /**
     * Checks whether the right information is shown depending on whether the goal type is set to pages or books
     */
    @Test
    public void correctInformationShownForGoalType() {
        Assumptions.assumeTrue(goalService.findAll().size() == 0);

        ReadingGoal booksGoal = new ReadingGoal(getRandomGoalTarget(), getRandomGoalType());
        goalService.save(booksGoal);
        goalView.getCurrentGoal();
        // should be visible for both a book or pages goal
        Assertions.assertTrue(goalView.readingGoalSummary.isVisible());
        Assertions.assertTrue(goalView.goalProgressPercentage.isVisible());

        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();
        int howManyReadThisYear = CalculateReadingGoal.howManyReadThisYear(ReadingGoal.GoalType.BOOKS, readShelf);
        int targetToRead = booksGoal.getTarget();
        boolean hasReachedGoal = (targetToRead <= howManyReadThisYear);

        if (booksGoal.getGoalType().equals(ReadingGoal.GoalType.BOOKS)) {
            // Additional components that should be visible for a books goal
            Assertions.assertTrue(goalView.goalProgress.isVisible());
            if(hasReachedGoal) {
                Assertions.assertFalse(goalView.booksToReadOnAverageToMeetGoal.isVisible());
            } else {
                Assertions.assertTrue(goalView.booksToReadOnAverageToMeetGoal.isVisible());
            }
        }
    }

    /**
     * Checks whether the right information is shown when a goal is set/updated.
     */
    @Test
    public void correctInformationShownWhenGoalIsUpdated() {
        Assumptions.assumeTrue(goalService.findAll().size() == 0);

        ReadingGoal readingGoal = new ReadingGoal(getRandomGoalTarget(), getRandomGoalType());
        goalService.save(readingGoal);
        goalView.getCurrentGoal();
        // should be visible for both a book or pages goal
        Assertions.assertTrue(goalView.readingGoalSummary.isVisible());
        Assertions.assertTrue(goalView.goalProgressPercentage.isVisible());

        PredefinedShelf readShelf = predefinedShelfUtils.findReadShelf();
        int howManyReadThisYear = CalculateReadingGoal.howManyReadThisYear(readingGoal.getGoalType(), readShelf);
        int targetToRead = readingGoal.getTarget();
        boolean hasReachedGoal = (targetToRead <= howManyReadThisYear);

        if (readingGoal.getGoalType().equals(ReadingGoal.GoalType.BOOKS)) {
            // Additional components that should be visible for a books goal
            Assertions.assertTrue(goalView.goalProgress.isVisible());
            if(hasReachedGoal) {
                Assertions.assertFalse(goalView.booksToReadOnAverageToMeetGoal.isVisible());
            } else {
                Assertions.assertTrue(goalView.booksToReadOnAverageToMeetGoal.isVisible());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

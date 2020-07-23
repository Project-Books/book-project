package com.karankumar.bookproject.ui.goal;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.backend.util.DateUtils;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
public class ReadingGoalViewTests {
    private static Routes routes;

    @Autowired
    private ApplicationContext ctx;

    private ReadingGoalService goalService;
    private PredefinedShelfService predefinedShelfService;
    private ReadingGoalView goalView;
    @MockBean
    private DateUtils dateUtils;

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
        MockitoAnnotations.initMocks(dateUtils);
        goalView = new ReadingGoalView(goalService, predefinedShelfService, dateUtils);
    }

    /**
     * Tests whether the progress value calculation is correct
     */
    @Test
    public void progressValueCorrect() {
        int booksToRead = new Random().nextInt(100);

        Assertions.assertEquals(ReadingGoalView.getProgress(5, 0), 0); // == 0%
        Assertions.assertEquals(ReadingGoalView.getProgress(25, 5), 0.2); // < 100%
        Assertions.assertEquals(ReadingGoalView.getProgress(booksToRead, booksToRead), 1.0,
                "Books to read = " + booksToRead); // == 100%
        Assertions.assertEquals(ReadingGoalView.getProgress(booksToRead, (booksToRead + 1)), 1.0,
                "Books to read = " + booksToRead); // > 100%

        // ensure 0, and not an arithmetic exception, is returned
        Assertions.assertEquals(ReadingGoalView.getProgress(0, 5), 0);
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

        PredefinedShelf readShelf = findShelf(PredefinedShelf.ShelfName.READ);
        Assumptions.assumeTrue(readShelf != null);
        Assertions.assertEquals(booksInReadShelf, ReadingGoalView
                .howManyReadThisYear(ReadingGoal.GoalType.BOOKS, readShelf));
        Assertions.assertEquals(pagesReadInReadShelf, ReadingGoalView
                .howManyReadThisYear(ReadingGoal.GoalType.PAGES, readShelf));
    }

    /**
     * Helper method that find a shelf with a particular name
     * @param shelfName the name of the shelf to look for
     * @return the shelf that matches the shelf name provided
     */
    private PredefinedShelf findShelf(PredefinedShelf.ShelfName shelfName) {
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(shelfName))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }

    /**
     * Creates a book in the specified shelf
     * @param shelfName the name of the shelf to place the book in
     * @return a new Book
     */
    private Book createBook(PredefinedShelf.ShelfName shelfName) {
        Book book = new Book("Title", new Author("Joe", "Bloggs"));
        book.setShelf(findShelf(shelfName)); // important not to create a new predefined shelf
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
        Assertions.assertTrue(goalView.readingGoal.isVisible());
        Assertions.assertTrue(goalView.progressPercentage.isVisible());

        if (booksGoal.getGoalType().equals(ReadingGoal.GoalType.BOOKS)) {
            // Additional components that should be visible for a books goal
            Assertions.assertTrue(goalView.goalProgress.isVisible());
            Assertions.assertTrue(goalView.booksToRead.isVisible());
        }
    }

    @Test
    public void testHowFarAheadOrBehindSchedule(){
        Mockito.when(dateUtils.getWeekOfYear()).thenReturn(1);
        Assertions.assertEquals(0, goalView.howFarAheadOrBehindSchedule(52,1));
        Assertions.assertEquals(1, goalView.howFarAheadOrBehindSchedule(52,0));
        Assertions.assertEquals(9, goalView.howFarAheadOrBehindSchedule(52,10));
        Assertions.assertEquals(9, goalView.howFarAheadOrBehindSchedule(199,12));
        Assertions.assertEquals(2, goalView.howFarAheadOrBehindSchedule(199,5));
        Mockito.when(dateUtils.getWeekOfYear()).thenReturn(15);
        Assertions.assertEquals(12, goalView.howFarAheadOrBehindSchedule(52,3));
        Assertions.assertEquals(9, goalView.howFarAheadOrBehindSchedule(52,24));
        Assertions.assertEquals(5, goalView.howFarAheadOrBehindSchedule(52,20));
        Mockito.when(dateUtils.getWeekOfYear()).thenReturn(10);
        Assertions.assertEquals(20, goalView.howFarAheadOrBehindSchedule(199,50));
        Assertions.assertEquals(22, goalView.howFarAheadOrBehindSchedule(199,8));
        Assertions.assertEquals(70, goalView.howFarAheadOrBehindSchedule(199,100));
        Mockito.when(dateUtils.getWeekOfYear()).thenReturn(43);
        Assertions.assertEquals(7, goalView.howFarAheadOrBehindSchedule(113,79));
        Assertions.assertEquals(45, goalView.howFarAheadOrBehindSchedule(113,41));
        Assertions.assertEquals(0, goalView.howFarAheadOrBehindSchedule(113,86));
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

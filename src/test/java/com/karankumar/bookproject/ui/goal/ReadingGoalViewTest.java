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
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringServlet;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;
import static com.karankumar.bookproject.backend.entity.ReadingGoal.GoalType.PAGES;
import static com.karankumar.bookproject.backend.entity.ReadingGoal.GoalType.BOOKS;
import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.howManyReadThisYear;
import static com.karankumar.bookproject.utils.ReadingGoalTestUtils.resetGoalService;
import static com.karankumar.bookproject.utils.ReadingGoalTestUtils.findHowManyBooksInReadShelfWithFinishDate;
import static com.karankumar.bookproject.utils.ReadingGoalTestUtils.findHowManyPagesInReadShelfWithFinishDate;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;


@IntegrationTest
@WebAppConfiguration
class ReadingGoalViewTest {
    private static Routes routes;

    @Autowired private ApplicationContext ctx;
    @Autowired private BookService bookService;

    private ReadingGoalService goalService;
    private PredefinedShelfService predefinedShelfService;
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

        resetGoalService(goalService);

        this.goalService = goalService;
        this.predefinedShelfService = predefinedShelfService;
        goalView = new ReadingGoalView(goalService, predefinedShelfService);
    }

    @Test
    void testSetGoalButtonTextUpdatesWhenGoalUpdates() {
        // given initial state
        assumeThat(goalService.findAll()).isEmpty();
        String expected = ReadingGoalView.SET_GOAL;
        String actual = goalView.setGoalButton.getText();
        assertThat(actual).isEqualTo(expected);

        // when
        goalService.save(new ReadingGoal(GOAL_TARGET, getRandomGoalType()));
        goalView.getCurrentGoal();

        // then
        String expectedGoalButtonText = ReadingGoalView.UPDATE_GOAL;
        String actualGoalButtonText = goalView.setGoalButton.getText();
        assertThat(actualGoalButtonText).isEqualTo(expectedGoalButtonText);
    }

    private ReadingGoal.GoalType getRandomGoalType() {
        ReadingGoal.GoalType[] goalTypes = ReadingGoal.GoalType.values();
        return goalTypes[new Random().nextInt(goalTypes.length)];
    }

    @Test
    void testTargetMetMessageNotShownWhenGoalNotMet() {
        String expected = ReadingGoalView.TARGET_MET;
        String actual = goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET - 1);
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void testTargetMetMessageShownWhenGoalMet() {
        assumeThat(goalService.findAll()).isEmpty();
        String expected = ReadingGoalView.TARGET_MET;
        String actual = goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testTargetMetMessageShownWhenGoalExceeded() {
        String expected = ReadingGoalView.TARGET_MET;
        String actual = goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET + 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Disabled
        // TODO: fix failing test. This runs fine in IntelliJ, but fails when `mvn clean install` is executed on Windows
    void onlyReadBooksWithAFinishDateCountTowardsGoal() {
        int numberOfShelves = predefinedShelfService.findAll().size();
        assumeThat(numberOfShelves).isEqualTo(4);

        Assumptions.assumeTrue(numberOfShelves == 4);

        resetBookService(bookService);
        assumeThat(bookService.findAll()).isEmpty();

        addBooksToAllShelves(numberOfShelves);

        List<Book> allBooks = bookService.findAll();
        int booksInReadShelf = findHowManyBooksInReadShelfWithFinishDate(allBooks);
        int pagesReadInReadShelf = findHowManyPagesInReadShelfWithFinishDate(allBooks);
        System.out.println("Pages read " + pagesReadInReadShelf);

        PredefinedShelf readShelf = predefinedShelfService.findReadShelf();
        Assumptions.assumeTrue(readShelf != null);
        assertThat(howManyReadThisYear(BOOKS, readShelf)).isEqualTo(booksInReadShelf);
        assertThat(howManyReadThisYear(PAGES, readShelf)).isEqualTo(pagesReadInReadShelf);
    }

    private void addBooksToAllShelves(int numberOfShelves) {
        int booksToAdd = 10;
        for (int i = 0; i < booksToAdd; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, numberOfShelves);
            Book book;

            switch (random) {
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

    private void resetBookService(BookService bookService) {
        bookService.deleteAll();
    }

    private Book createBook(ShelfName shelfName) {
        Book book = new Book("Title", new Author("Joe", "Bloggs"),
                predefinedShelfService.findReadShelf());
        if (shelfName.equals(ShelfName.READ)) {
            book.setDateFinishedReading(LocalDate.now());
        }
        book.setNumberOfPages(300);
        return book;
    }

    @Test
    void correctInformationShownWhenGoalIsSetOrUpdated() {
        assumeThat(goalService.findAll()).isEmpty();

        // given
        ReadingGoal readingGoal = new ReadingGoal(GOAL_TARGET, getRandomGoalType());

        // when
        goalService.save(readingGoal);
        goalView.getCurrentGoal();

        // then
        assertBooksAndPagesGoalComponentsShown();
        if (readingGoal.getGoalType().equals(BOOKS)) {
            assertGoalOnlyComponentsShown(readingGoal);
        }
    }

    private void assertBooksAndPagesGoalComponentsShown() {
        assertSoftly(softly -> {
            softly.assertThat(goalView.readingGoalSummary.isVisible()).isTrue();
            softly.assertThat(goalView.goalProgressPercentage.isVisible()).isTrue();
        });
    }

    private void assertGoalOnlyComponentsShown(ReadingGoal goal) {
        PredefinedShelf readShelf = predefinedShelfService.findReadShelf();
        int howManyReadThisYear = howManyReadThisYear(goal.getGoalType(), readShelf);
        boolean hasReachedGoal = (goal.getTarget() <= howManyReadThisYear);

        assertThat(goalView.goalProgress.isVisible()).isTrue();
        assertBooksToReadOnAverageIsShown(hasReachedGoal);
    }

    private void assertBooksToReadOnAverageIsShown(boolean hasReachedGoal) {
        if (hasReachedGoal) {
            assertThat(goalView.booksToReadOnAverageToMeetGoal.isVisible()).isFalse();
        } else {
            assertThat(goalView.booksToReadOnAverageToMeetGoal.isVisible()).isTrue();
        }
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }
}

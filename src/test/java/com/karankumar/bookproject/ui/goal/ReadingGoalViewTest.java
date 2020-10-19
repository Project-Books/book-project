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

import static com.karankumar.bookproject.backend.entity.ReadingGoal.GoalType.PAGES;
import static com.karankumar.bookproject.util.ReadingGoalTestUtils.resetGoalService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.Random;

import static com.karankumar.bookproject.backend.entity.ReadingGoal.GoalType.BOOKS;
import static com.karankumar.bookproject.backend.goal.CalculateReadingGoal.howManyReadThisYear;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@WebAppConfiguration
@DisplayName("ReadingGoalView should")
class ReadingGoalViewTest {
    private static Routes routes;
    private final int PAGES_PER_BOOK = 300;

    private final ApplicationContext ctx;
    private final BookService bookService;

    private final ReadingGoalService goalService;
    private final PredefinedShelfService predefinedShelfService;
    private ReadingGoalView goalView;

    private final int GOAL_TARGET = 52;
    private final int READ_BOOKS_TO_ADD = 5;

    @Autowired
    ReadingGoalViewTest(ApplicationContext ctx, ReadingGoalService readingGoalService,
                        PredefinedShelfService predefinedShelfService,
                        BookService bookService) {
        this.ctx = ctx;
        this.goalService = readingGoalService;
        this.predefinedShelfService = predefinedShelfService;
        this.bookService = bookService;
    }

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup() {
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        resetGoalService(goalService);

        goalView = new ReadingGoalView(goalService, predefinedShelfService);
    }

    @Test
    void updateSetGoalButtonTextWhenGoalUpdates() {
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
    void notShowTargetMetMessageWhenGoalNotMet() {
        String expected = ReadingGoalView.TARGET_MET;
        String actual = goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET - 1);
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void showTargetMetMessageWhenGoalMet() {
        assumeThat(goalService.findAll()).isEmpty();
        String expected = ReadingGoalView.TARGET_MET;
        String actual = goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void showTargetMetMessageWhenGoalExceeded() {
        String expected = ReadingGoalView.TARGET_MET;
        String actual = goalView.calculateProgress(GOAL_TARGET, GOAL_TARGET + 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void countOnlyReadBooksWithAFinishDateTowardsGoal() {
        // given
        resetBookService(bookService);

        // when
        addBooksToAllShelves();
        int pagesReadInReadShelf = PAGES_PER_BOOK * READ_BOOKS_TO_ADD;

        // then
        PredefinedShelf readShelf = predefinedShelfService.findReadShelf();
        assertThat(howManyReadThisYear(BOOKS, readShelf)).isEqualTo(READ_BOOKS_TO_ADD);
        assertThat(howManyReadThisYear(PAGES, readShelf)).isEqualTo(pagesReadInReadShelf);
    }

    private void addBooksToAllShelves() {
        populateBooksInShelf(3, predefinedShelfService.findToReadShelf());
        populateBooksInShelf(4, predefinedShelfService.findReadingShelf());
        populateBooksInShelf(READ_BOOKS_TO_ADD, predefinedShelfService.findReadShelf());
        populateBooksInShelf(6, predefinedShelfService.findDidNotFinishShelf());
    }

    private void populateBooksInShelf(int booksToAdd, PredefinedShelf predefinedShelf) {
        for (int i = 0; i < booksToAdd; i++) {
            bookService.save(createBook(predefinedShelf));
        }
    }

    private Book createBook(PredefinedShelf predefinedShelf) {
        Book book = new Book("Title", new Author("Joe", "Bloggs"), predefinedShelf);
        book.setDateFinishedReading(LocalDate.now());
        book.setNumberOfPages(PAGES_PER_BOOK);
        return book;
    }

    private void resetBookService(BookService bookService) {
        bookService.deleteAll();
    }

    @Test
    void showCorrectInformationWhenGoalIsSetOrUpdated() {
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

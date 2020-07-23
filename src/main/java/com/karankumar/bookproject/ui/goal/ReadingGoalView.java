/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.util.DateUtils;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Vaadin view that represents the reading goal page (the number of books or pages a user wants to have read by the end
 * of the year)
 */
@Route(value = "goal", layout = MainView.class)
@PageTitle("Goal | Book Project")
public class ReadingGoalView extends VerticalLayout {

    public static final String SET_GOAL = "Set goal";
    public static final String UPDATE_GOAL = "Update goal";
    public static final String TARGET_MET = "Congratulations for reaching your target!";

    private static final Logger LOGGER = Logger.getLogger(ReadingGoalView.class.getName());
    private static final String BEHIND = "behind";
    private static final String AHEAD_OF = "ahead of";

    public final Button setGoalButton;
    private final PredefinedShelfService predefinedShelfService;
    private final ProgressBar progressBar;

    private ReadingGoalService goalService;

    private static DateUtils dateUtils;

    /**
     * Displays what the reading goal is and how many books/pages the user has read
     */
    H1 readingGoal;

    /**
     * Displays whether a user has met the goal, is ahead or is behind the goal
     */
    H3 goalProgress;

    /**
     * Displays the user's progress towards their goal as a percentage
     */
    Span progressPercentage;

    /**
     * Displays how many books a user needs to read on average to meet their goal
     */
    Span booksToRead;

    public ReadingGoalView(ReadingGoalService goalService, PredefinedShelfService predefinedShelfService, DateUtils dateUtils) {
        this.goalService = goalService;
        this.predefinedShelfService = predefinedShelfService;
        this.dateUtils = dateUtils;

        readingGoal = new H1();
        setGoalButton = new Button();
        goalProgress = new H3();
        booksToRead = new Span();
        progressBar = new ProgressBar();
        progressBar.setMaxWidth("500px");
        progressPercentage = new Span();
        progressPercentage.getElement().getStyle().set("font-style", "italic");

        configureSetGoal();
        getCurrentGoal();

        add(readingGoal, progressBar, progressPercentage, goalProgress, booksToRead, setGoalButton);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private void configureSetGoal() {
        setGoalButton.addClickListener(event -> {
            ReadingGoalForm goalForm = new ReadingGoalForm();
            add(goalForm);
            goalForm.openForm();

            goalForm.addListener(ReadingGoalForm.SaveEvent.class, this::saveGoal);
        });
    }

    void getCurrentGoal() {
        List<ReadingGoal> goals = goalService.findAll();
        if (goals.size() == 0) {
            readingGoal.setText("Reading goal not set");
            setGoalButton.setText(SET_GOAL);
        } else {
            updateReadingGoal(goals.get(0).getTarget(), goals.get(0).getGoalType());
        }
    }

    private void updateSetGoalText() {
        setGoalButton.setText(UPDATE_GOAL);
    }

    private void saveGoal(ReadingGoalForm.SaveEvent event) {
        if (event.getReadingGoal() != null) {
            LOGGER.log(Level.INFO, "Retrieved goal from form is not null");
            goalService.save(event.getReadingGoal());
            updateReadingGoal(event.getReadingGoal().getTarget(), event.getReadingGoal().getGoalType());
        } else {
            LOGGER.log(Level.SEVERE, "Retrieved goal from form is null");
        }
    }

    private void updateReadingGoal(int targetToRead, ReadingGoal.GoalType goalType) {
        PredefinedShelf readShelf = findReadShelf();
        if (readShelf == null || readShelf.getBooks() == null) {
            return;
        }

        int howManyReadThisYear = howManyReadThisYear(goalType, readShelf);
        String haveRead = "You have read ";
        String outOf = " out of ";
        if (goalType.equals(ReadingGoal.GoalType.BOOKS)) {
            toggleBooksGoalInfo(true);
            readingGoal.setText(haveRead + howManyReadThisYear + outOf + + targetToRead + " books");
            goalProgress.setText(calculateProgress(targetToRead, howManyReadThisYear));
        } else {
            toggleBooksGoalInfo(false);
            readingGoal.setText(haveRead + howManyReadThisYear + outOf + targetToRead + " pages");
        }

        double progress = getProgress(targetToRead, howManyReadThisYear);
        progressBar.setValue(progress);
        progressPercentage.setText(String.format("%.2f%% completed", (progress * 100)));

        updateSetGoalText();
    }

    /**
     * Only books in the read shelf count towards the goal
     * @return the read shelf if it can be found, null otherwise.
     */
    private PredefinedShelf findReadShelf() {
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(PredefinedShelf.ShelfName.READ))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }

    /**
     * Find how many books or pages have been read this year
     * @param goalType either books or pages
     * @param readShelf the predefined read shelf
     * @return the number of books or pages read this year
     */
    public static int howManyReadThisYear(ReadingGoal.GoalType goalType, @NotNull PredefinedShelf readShelf) {
        int readThisYear = 0;
        boolean lookingForBooks = goalType.equals(ReadingGoal.GoalType.BOOKS);
        for (Book book : readShelf.getBooks()) {
            // only books that have been given a finish date can count towards the reading goal
            if (book != null && book.getDateFinishedReading() != null
                    && book.getDateFinishedReading().getYear() == LocalDate.now().getYear()) {
                int pages = (book.getNumberOfPages() == null) ? 0 : book.getNumberOfPages();
                readThisYear += (lookingForBooks ? (1) : pages);
            }
        }
        return readThisYear;
    }

    /**
     * @param isOn if true, set the visibility of the book goal-specific text to true. Otherwise, set them to false
     */
    private void toggleBooksGoalInfo(boolean isOn) {
        goalProgress.setVisible(isOn);
        booksToRead.setVisible(isOn);
    }

    /**
     * Calculates the reading progress for the books goal only
     * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
     * @param booksReadThisYear the number of books that have already been read by th end of the year
     * @return a String that displays whether the goal was met, or whether the user is ahead or behind schedule
     */
    String calculateProgress(int booksToReadThisYear, int booksReadThisYear) {
        LOGGER.log(Level.INFO, "\nBooks to read this year: " + booksToReadThisYear);
        LOGGER.log(Level.INFO, "Books read this year: " + booksReadThisYear);

        String schedule = "";
        int booksStillToRead = booksToReadThisYear - booksReadThisYear;
        LOGGER.log(Level.INFO, "Books still to read: " + booksStillToRead);

        if (booksStillToRead <= 0) {
            schedule = TARGET_MET;
        } else {
            int weekOfYear = dateUtils.getWeekOfYear();
            int weeksLeftInYear = dateUtils.getWeeksLeftInYear(weekOfYear);
            double booksStillToReadAWeek = Math.ceil((double) booksStillToRead / weeksLeftInYear);
            booksToRead.setText("You need to read " + booksStillToReadAWeek +
                    " books a week on average to achieve your goal");

            int howManyBehindOrAhead = howFarAheadOrBehindSchedule(booksToReadThisYear, booksReadThisYear);
            schedule = String.format("You are %d books %s schedule", howManyBehindOrAhead,
                    behindOrAheadSchedule(booksReadThisYear, shouldHaveRead(booksToReadThisYear)));
        }
        return schedule;
    }

    /**
     * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
     * @param booksReadThisYear the number of books read so far
     * @return the number of books that the user is ahead or behind schedule by
     */
    public int howFarAheadOrBehindSchedule(int booksToReadThisYear, int booksReadThisYear) {
        int booksToReadFromStartOfYear = booksToReadFromStartOfYear(booksToReadThisYear);
        int weekOfYear = dateUtils.getWeekOfYear();
        int shouldHaveRead =  booksToReadFromStartOfYear * weekOfYear;
        return Math.abs(shouldHaveRead - booksReadThisYear);
    }


    /**
     * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
     * @return the number of books that should have been read a week (on average) from the start of the year
     */
    public static int booksToReadFromStartOfYear(int booksToReadThisYear) {
        int weeksInYear = dateUtils.getWeeksInYear();
        return ((int) Math.ceil(booksToReadThisYear / weeksInYear));
    }

    /**
     * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
     * @return the number of books that the user should have ready by this point in the year in order to be on target
     */
    public int shouldHaveRead(int booksToReadThisYear) {
        return booksToReadFromStartOfYear(booksToReadThisYear) * dateUtils.getWeekOfYear();
    }

    /**
     * Note that this method assumes that the user is behind or ahead of schedule (and that they haven't met their goal)
     * @param booksReadThisYear the number of books read so far
     * @param shouldHaveRead the number of books that should have been ready by this point to be on schedule
     * @return a String denoting that the user is ahead or behind schedule
     */
    public static String behindOrAheadSchedule(int booksReadThisYear, int shouldHaveRead) {
        return (booksReadThisYear < shouldHaveRead) ? BEHIND : AHEAD_OF;
    }

    /**
     * Calculates a user's progress towards their reading goal
     * @param toRead the number of books to read by the end of the year (the goal)
     * @param read the number of books that the user has read so far
     * @return a fraction of the number of books to read over the books read. If greater than 1, 1.0 is returned
     */
    public static double getProgress(int toRead, int read) {
        double progress = (toRead == 0) ? 0 : ((double) read / toRead);
        return Math.min(progress, 1.0);
    }
}

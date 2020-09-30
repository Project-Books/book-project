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

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.goal.CalculateReadingGoal;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.backend.utils.StringUtils;
import com.karankumar.bookproject.backend.utils.DateUtils;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.java.Log;

import java.util.List;
import java.util.logging.Level;

@Route(value = "goal", layout = MainView.class)
@PageTitle("Goal | Book Project")
@Log
public class ReadingGoalView extends VerticalLayout {

    @VisibleForTesting static final String SET_GOAL = "Set goal";
    @VisibleForTesting static final String UPDATE_GOAL = "Update goal";
    @VisibleForTesting static final String TARGET_MET = "Congratulations for reaching your target!";

    @VisibleForTesting final Button setGoalButton;
    private final PredefinedShelfService predefinedShelfService;
    private final ProgressBar progressBar;

    private final ReadingGoalService goalService;

    @VisibleForTesting H1 readingGoalSummary;
    @VisibleForTesting H3 goalProgress;
    @VisibleForTesting Span goalProgressPercentage;
    @VisibleForTesting Span booksToReadOnAverageToMeetGoal;

    public ReadingGoalView(ReadingGoalService goalService,
                           PredefinedShelfService predefinedShelfService) {
        this.goalService = goalService;
        this.predefinedShelfService = predefinedShelfService;

        readingGoalSummary = new H1();
        setGoalButton = new Button();
        goalProgress = new H3();
        booksToReadOnAverageToMeetGoal = new Span();
        progressBar = new ProgressBar();
        progressBar.setMaxWidth("500px");
        goalProgressPercentage = new Span();
        goalProgressPercentage.getElement().getStyle().set("font-style", "italic");

        configureSetGoal();
        getCurrentGoal();

        add(readingGoalSummary, progressBar, goalProgressPercentage, goalProgress,
                booksToReadOnAverageToMeetGoal, setGoalButton);
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

    @VisibleForTesting
    void getCurrentGoal() {
        List<ReadingGoal> goals = goalService.findAll();
        if (goals.isEmpty()) {
            readingGoalSummary.setText("Reading goal not set");
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
            updateReadingGoal(event.getReadingGoal().getTarget(),
                    event.getReadingGoal().getGoalType());
        } else {
            LOGGER.log(Level.SEVERE, "Retrieved goal from form is null");
        }
    }

    private void updateReadingGoal(int targetToRead, ReadingGoal.GoalType goalType) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        if (readShelf == null || readShelf.getBooks() == null) {
            return;
        }

        int howManyReadThisYear = CalculateReadingGoal.howManyReadThisYear(goalType, readShelf);
        boolean hasReachedGoal = (targetToRead <= howManyReadThisYear);
        String haveRead = "You have read ";
        String outOf = " out of ";

        if (goalType.equals(ReadingGoal.GoalType.BOOKS)) {
            readingGoalSummary.setText(haveRead + howManyReadThisYear + outOf + + targetToRead +
                    StringUtils.pluralize(" book", targetToRead));
            goalProgress.setText(calculateProgress(targetToRead, howManyReadThisYear));
            booksToReadOnAverageToMeetGoal.setText(calculateBooksToRead(targetToRead,
                    howManyReadThisYear));
            toggleBooksGoalInfo(true, hasReachedGoal);    // show book goal-specific information
        } else {
            readingGoalSummary.setText(haveRead + howManyReadThisYear + outOf + targetToRead +
                            StringUtils.pluralize(" page", targetToRead));
            toggleBooksGoalInfo(false, hasReachedGoal);
        }

        double progress = CalculateReadingGoal.calculateProgressTowardsReadingGoal(targetToRead,
                howManyReadThisYear);
        progressBar.setValue(progress);
        goalProgressPercentage.setText(String.format("%.2f%% completed", (progress * 100)));

        updateSetGoalText();
    }

    /**
     * @param isOn if true, set the visibility of the book goal-specific text to true.
     *             Otherwise, set them to false
     * @param hasReachedGoal if true, set the visibility of the "books to read on average" text to
     *                       false.
     */
    private void toggleBooksGoalInfo(boolean isOn, boolean hasReachedGoal) {
        goalProgress.setVisible(isOn);
        booksToReadOnAverageToMeetGoal.setVisible(isOn && !hasReachedGoal);
    }

    /**
     * Calculates the reading progress for the books goal only
     * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
     * @param booksReadThisYear the number of books that have already been read by th end of the year
     * @return a String that displays whether the goal was met, or whether the user is ahead or
     * behind schedule
     */
    @VisibleForTesting
    String calculateProgress(int booksToReadThisYear, int booksReadThisYear) {
        LOGGER.log(Level.INFO, "\nBooks to read this year: " + booksToReadThisYear);
        LOGGER.log(Level.INFO, "Books read this year: " + booksReadThisYear);

        String schedule = "";
        int booksStillToRead = booksToReadThisYear - booksReadThisYear;
        LOGGER.log(Level.INFO, "Books still to read: " + booksStillToRead);

        if (booksStillToRead <= 0) {
            schedule = TARGET_MET;
        } else {
            int howManyBehindOrAhead =
                    CalculateReadingGoal.howFarAheadOrBehindSchedule(booksToReadThisYear,
                            booksReadThisYear);
            schedule = String.format("You are %d "+ StringUtils.pluralize("book",
                    howManyBehindOrAhead) + " %s schedule",
                    howManyBehindOrAhead, CalculateReadingGoal.behindOrAheadSchedule(booksReadThisYear,
                            CalculateReadingGoal.shouldHaveRead(booksToReadThisYear)));
        }
        return schedule;
    }

    /**
     * Calculates how many books a user needs to read on average to meet their goal for the books goal only
     * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
     * @param booksReadThisYear the number of books that have already been read till date
     * @return a String that displays the number of books user needs to read on average to meet
     * their goal (if not met).
     */
    private String calculateBooksToRead(int booksToReadThisYear, int booksReadThisYear) {
        int booksStillToRead = booksToReadThisYear - booksReadThisYear;
        int weekOfYear = DateUtils.getCurrentWeekNumberOfYear();
        int weeksLeftInYear = DateUtils.calculateWeeksLeftInYearFromCurrentWeek(weekOfYear);
        double booksStillToReadAWeek = Math.ceil((double) booksStillToRead / weeksLeftInYear);

        String bookReadingRate = "";
        if (booksStillToRead > 0) {
            bookReadingRate = "You need to read " + booksStillToReadAWeek +
                    StringUtils.pluralize(" book", (int) booksStillToReadAWeek) +
                    " a week on average to achieve your goal";
        }

        return bookReadingRate;
    }
}

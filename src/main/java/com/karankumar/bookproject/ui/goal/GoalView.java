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
import com.karankumar.bookproject.backend.service.GoalService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vaadin view that represents the reading goal page (the number of books or pages a user wants to have read by the end
 * of the year)
 */
@Route(value = "goal", layout = MainView.class)
@PageTitle("Goal | Book Project")
public class GoalView extends VerticalLayout {

    private static final Logger LOGGER = Logger.getLogger(GoalView.class.getName());
    private final Button setGoal;
    private final PredefinedShelfService predefinedShelfService;
    private final ProgressBar progressBar;

    private GoalService goalService;
    private H1 readingGoal;

    /**
     * Displays whether a user has met the goal, is ahead or is behind the goal
     */
    private H3 goalProgress;

    private Span progressPercentage;

    private Span booksToRead;

    public GoalView(GoalService goalService, PredefinedShelfService predefinedShelfService) {
        this.goalService = goalService;
        this.predefinedShelfService = predefinedShelfService;

        readingGoal = new H1();
        setGoal = new Button();
        goalProgress = new H3();
        booksToRead = new Span();
        progressBar = new ProgressBar();
        progressBar.setMaxWidth("500px");
        progressPercentage = new Span();
        progressPercentage.getElement().getStyle().set("font-style", "italic");

        configureSetGoal();
        getCurrentGoal();

        add(readingGoal, progressBar, progressPercentage, goalProgress, booksToRead, setGoal);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private void configureSetGoal() {
        setGoal.addClickListener(event -> {
            GoalForm goalForm = new GoalForm();
            add(goalForm);
            goalForm.openForm();

            goalForm.addListener(GoalForm.SaveEvent.class, this::saveGoal);
        });
    }

    private void getCurrentGoal() {
        List<ReadingGoal> goals = goalService.findAll();
        if (goals.size() == 0) {
            readingGoal.setText("Reading goal not set");
            setGoal.setText("Set goal");
        } else {
            updateReadingGoal(goals.get(0).getTarget(), goals.get(0).getGoalType());
        }
    }

    private void updateSetGoalText() {
        setGoal.setText("Update goal");
    }

    private void saveGoal(GoalForm.SaveEvent event) {
        if (event.getReadingGoal() != null) {
            LOGGER.log(Level.INFO, "Retrieved goal from form is not null");
            goalService.save(event.getReadingGoal());
            updateReadingGoal(event.getReadingGoal().getTarget(), event.getReadingGoal().getGoalType());
        } else {
            LOGGER.log(Level.SEVERE, "Retrieved goal from form is null");
        }
    }

    private void updateReadingGoal(int targetToRead, ReadingGoal.GoalType goalType) {
        PredefinedShelf readShelf = null;
        // only books in the read shelf count towards the goal
        for (PredefinedShelf p : predefinedShelfService.findAll()) {
            if (p.getShelfName().equals(PredefinedShelf.ShelfName.READ)) {
                readShelf = p;
                break;
            }
        }
        if (readShelf == null || readShelf.getBooks() == null) {
            return;
        }

        LOGGER.log(Level.INFO, "Read shelf: " + readShelf);

        int booksReadThisYear = 0;
        int pagesReadThisYear = 0;
        boolean lookingForBooks = goalType.equals(ReadingGoal.GoalType.BOOKS);
        for (Book book : readShelf.getBooks()) {
            if (book != null) {
                if (lookingForBooks && book.getDateFinishedReading() != null &&
                        book.getDateFinishedReading().getYear() == LocalDate.now().getYear()) {
                    booksReadThisYear++;
                } else {
                    pagesReadThisYear += book.getNumberOfPages();
                }
            }
        }

        String haveRead = "You have read ";
        String outOf = " out of ";
        if (goalType.equals(ReadingGoal.GoalType.BOOKS)) {
            toggleBooksGoalInfo(true);

            readingGoal.setText(haveRead + booksReadThisYear + outOf + + targetToRead + " books");
            goalProgress.setText(calculateProgress(targetToRead, booksReadThisYear));
            updateProgressBarValue(targetToRead, booksReadThisYear);
        } else {
            toggleBooksGoalInfo(false);

            readingGoal.setText(haveRead + pagesReadThisYear + outOf + targetToRead + " pages");
            updateProgressBarValue(targetToRead, pagesReadThisYear);
        }

        updateSetGoalText();
    }

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
    private String calculateProgress(int booksToReadThisYear, int booksReadThisYear) {
        LOGGER.log(Level.INFO, "\nBooks to read this year: " + booksToReadThisYear);
        LOGGER.log(Level.INFO, "Books read this year: " + booksReadThisYear);

        String schedule = "";
        int booksStillToRead = booksToReadThisYear - booksReadThisYear;
        LOGGER.log(Level.INFO, "Books still to read: " + booksStillToRead);

        if (booksStillToRead <= 0) {
            schedule = "Congratulations for reaching your target!";
        } else {

            LocalDate now = LocalDate.now();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int weekOfYear = now.get(weekFields.weekOfWeekBasedYear());
            LOGGER.log(Level.INFO, "Week of year: " + weekOfYear);
            int weeksLeftInYear = 52 - weekOfYear;
            LOGGER.log(Level.INFO, "Weeks left in year: " + weeksLeftInYear);

            double booksStillToReadAWeek = Math.ceil((double) booksStillToRead / weeksLeftInYear);
            booksToRead.setText("You need to read " + booksStillToReadAWeek +
                    " books a week on average to achieve your goal");

            int booksToReadAWeekFromStartOfYear = (int) Math.ceil(booksToReadThisYear / 52);
            LOGGER.log(Level.INFO, "Books to read a week from the start of the year: " + booksToReadAWeekFromStartOfYear);

            int shouldHaveRead = booksToReadAWeekFromStartOfYear * weekOfYear;
            LOGGER.log(Level.INFO, "Should have read: " + shouldHaveRead);

            boolean behindSchedule = booksReadThisYear < shouldHaveRead;
            String behindOrAhead = behindSchedule ? "behind" : "ahead of ";
            int howManyBehindOrAhead = Math.abs(shouldHaveRead - booksReadThisYear);

            schedule = String.format("You are %d books %s schedule", howManyBehindOrAhead, behindOrAhead);
        }

        return schedule;
    }

    private void updateProgressBarValue(int toRead, int read) {
        double progress = ((double) read / toRead);
        progress = Math.min(progress, 1.0);
        progressBar.setValue(progress);

        double percentage = progress * 100;
        progressPercentage.setText(String.format("%.2f%% completed", percentage));
    }
}

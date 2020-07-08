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

import com.karankumar.bookproject.backend.service.GoalService;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Vaadin view that represents the reading goal page (the number of books or pages a user wants to have read by the end
 * of the year)
 */
@Route(value = "goal", layout = MainView.class)
@PageTitle("Goal | Book Project")
public class GoalView extends HorizontalLayout {

    private static final Logger LOGGER = Logger.getLogger(GoalView.class.getName());
    private final Button setGoal;

    private GoalService goalService;
    private H1 booksRead;

    public GoalView(GoalService goalService) {
        this.goalService = goalService;

        booksRead = new H1("Reading goal not set");
        setGoal = new Button("Set goal");
        configureSetGoal();

        VerticalLayout verticalLayout = new VerticalLayout(booksRead, setGoal);
        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
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

    private void saveGoal(GoalForm.SaveEvent event) {
        if (event.getReadingGoal() != null) {
            LOGGER.log(Level.INFO, "Book is not null");
            goalService.save(event.getReadingGoal());
            updateReadingGoal(event.getReadingGoal().getBooksToRead());
        } else {
            LOGGER.log(Level.SEVERE, "Retrieved goal from event is null");
        }
    }

    private void updateReadingGoal(int booksToRead) {
        booksRead.setText("Reading goal: " + booksToRead + " books");
    }
}

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

package com.karankumar.bookproject.ui.goal.events;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.ui.goal.ReadingGoalForm;
import com.vaadin.flow.component.ComponentEvent;

/**
 * Vaadin's event bus system. A registered listener can be notified when a save or delete event
 * is fired
 */
public abstract class GoalEvent extends ComponentEvent<ReadingGoalForm> {
    private final ReadingGoal readingGoal;

    protected GoalEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
        super(source, false);
        this.readingGoal = readingGoal;
    }

    public ReadingGoal getReadingGoal() {
        return readingGoal;
    }
}

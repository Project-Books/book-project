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

package com.karankumar.bookproject.ui.events;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.ui.goal.ReadingGoalForm;
import com.vaadin.flow.component.ComponentEvent;

public class ComponentGoalEvent {
    private ComponentGoalEvent() {}

    public abstract static class GoalFormEvent extends ComponentEvent<ReadingGoalForm> {
        private final ReadingGoal readingGoal;

        protected GoalFormEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, false);
            this.readingGoal = readingGoal;
        }

        public ReadingGoal getReadingGoal() {
            return readingGoal;
        }
    }
}

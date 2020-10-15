package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.ReadingGoal;

public class DeleteGoalEvent {
    // TODO: implement deleting reading goal
    public static class DeleteEvent extends ComponentGoalEvent.GoalFormEvent {
        DeleteEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }

}

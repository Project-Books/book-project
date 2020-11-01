package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.ReadingGoal;

public class SaveGoalEvent {
    public static class SaveEvent extends ComponentGoalEvent.GoalFormEvent {
        SaveEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }
}

package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.ReadingGoal;

public class SaveGoalEvent extends ComponentGoalEvent.GoalFormEvent{

        SaveGoalEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }


package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
public class DeleteGoalEvent extends ComponentGoalEvent.GoalFormEvent{
    // TODO: implement deleting reading goal

        DeleteGoalEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }


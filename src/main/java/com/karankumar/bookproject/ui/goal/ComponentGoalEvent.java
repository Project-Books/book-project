package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.vaadin.flow.component.ComponentEvent;

public class ComponentGoalEvent {  public abstract static class GoalFormEvent extends ComponentEvent<ReadingGoalForm> {
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

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
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.ui.goal.events.SaveGoalEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.shared.Registration;
import lombok.extern.java.Log;

import java.util.logging.Level;

/**
 * A Vaadin view that contains a form for setting a new reading goal
 */
@Log
public class ReadingGoalForm extends VerticalLayout {
    private static final String BOOKS_TO_READ = "Books to read";
    private static final String PAGES_TO_READ = "Pages to read";
    private final Dialog newGoalDialog;

    @VisibleForTesting final Binder<ReadingGoal> binder =
            new BeanValidationBinder<>(ReadingGoal.class);
    @VisibleForTesting final RadioButtonGroup<ReadingGoal.GoalType> chooseGoalType;
    @VisibleForTesting final IntegerField targetToRead;
    @VisibleForTesting Button saveButton;

    public ReadingGoalForm() {
        targetToRead = createTargetGoalField();
        saveButton = createSaveButton();

        FormLayout formLayout = new FormLayout();
        newGoalDialog = createGoalDialog();
        newGoalDialog.add(formLayout);
        add(newGoalDialog);

        chooseGoalType = createGoalTypeFormField();

        formLayout.addFormItem(chooseGoalType, "Goal type");
        formLayout.addFormItem(targetToRead, "To read");
        formLayout.add(new HorizontalLayout(saveButton));

        configureBinder();
    }

    private Dialog createGoalDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        return dialog;
    }

    private IntegerField createTargetGoalField() {
        IntegerField field = new IntegerField();
        field.setPlaceholder(BOOKS_TO_READ); // default value
        field.setClearButtonVisible(true);
        field.setMin(1);
        field.setHasControls(true);
        field.setMinWidth("13em");
        return field;
    }

    private Button createSaveButton() {
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setMinWidth("13em");
        save.addClickListener(click -> validateOnSave());
        return save;
    }

    private RadioButtonGroup<ReadingGoal.GoalType> createGoalTypeFormField() {
        RadioButtonGroup<ReadingGoal.GoalType> goalTypeRadioButtonGroup = new RadioButtonGroup<>();
        goalTypeRadioButtonGroup.setItems(ReadingGoal.GoalType.values());

        goalTypeRadioButtonGroup.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                if (event.getValue().equals(ReadingGoal.GoalType.BOOKS)) {
                    targetToRead.setPlaceholder(BOOKS_TO_READ);
                } else {
                    targetToRead.setPlaceholder(PAGES_TO_READ);
                }
            }
        });

        return goalTypeRadioButtonGroup;
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            ReadingGoal.GoalType goalType = this.chooseGoalType.getValue();
            if (goalType != null && targetToRead.getValue() != null) {
                saveReadingGoal(goalType);
            }
        } else {
            BinderValidationStatus<ReadingGoal> status = binder.validate();
            if (status.hasErrors()) {
                LOGGER.log(Level.SEVERE, "Invalid binder");
            }
        }
    }

    private void saveReadingGoal(ReadingGoal.GoalType goalType) {
        binder.setBean(new ReadingGoal(targetToRead.getValue(), goalType));
        LOGGER.log(Level.INFO, "Setting the bean");
        fireEvent(new SaveGoalEvent(this, binder.getBean()));
        confirmSavedGoal(binder.getBean().getTarget(), binder.getBean().getGoalType());
    }

    private void confirmSavedGoal(int target, ReadingGoal.GoalType goalType) {
        newGoalDialog.close();
        String notificationMessage =
                String.format("Set your reading goal of %d %s", target,
                        goalType.toString().
                                toLowerCase());
        new Notification(notificationMessage, 3000).open();
    }

    public void openForm() {
        newGoalDialog.open();
    }

    private void configureBinder() {
        binder.forField(chooseGoalType)
              .asRequired("Please select a goal type")
              .bind(ReadingGoal::getGoalType, ReadingGoal::setGoalType);

        binder.forField(targetToRead)
              .asRequired("Please enter in a target goal")
              .withValidator(target -> target >= 1, "Target must be at least 1")
              .bind(ReadingGoal::getTarget, ReadingGoal::setTarget);
    }

    public <T extends ComponentEvent<?>> Registration addListener(
            Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

package com.karankumar.bookproject.ui.goal;

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
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

    @VisibleForTesting
    final Binder<ReadingGoal> binder = new BeanValidationBinder<>(ReadingGoal.class);
    @VisibleForTesting
    final RadioButtonGroup<ReadingGoal.GoalType> chooseGoalType;
    @VisibleForTesting
    final IntegerField targetToRead;
    @VisibleForTesting
    Button saveButton;

    public ReadingGoalForm() {
        targetToRead = createTargetGoalField();
        saveButton = createSaveButton();

        FormLayout formLayout = new FormLayout();
        newGoalDialog = new Dialog();
        newGoalDialog.add(formLayout);
        newGoalDialog.setCloseOnOutsideClick(true);
        add(newGoalDialog);

        chooseGoalType = createGoalTypeFormField();

        formLayout.addFormItem(chooseGoalType, "Goal type");
        formLayout.addFormItem(targetToRead, "To read");
        formLayout.add(new HorizontalLayout(saveButton));
        chooseGoalType.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                if (event.getValue().equals(ReadingGoal.GoalType.BOOKS)) {
                    targetToRead.setPlaceholder(BOOKS_TO_READ);
                } else {
                    targetToRead.setPlaceholder(PAGES_TO_READ);
                }
            }
        });

        configureBinder();
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
        return goalTypeRadioButtonGroup;
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            ReadingGoal.GoalType goalType = this.chooseGoalType.getValue();
            if (goalType != null && targetToRead.getValue() != null) {
                binder.setBean(new ReadingGoal(targetToRead.getValue(), goalType));
                LOGGER.log(Level.INFO, "Setting the bean");
                fireEvent(new SaveEvent(this, binder.getBean()));
                confirmSavedGoal(binder.getBean().getTarget(), binder.getBean().getGoalType());
            }
        } else {
            BinderValidationStatus<ReadingGoal> status = binder.validate();
            if (status.hasErrors()) {
                LOGGER.log(Level.SEVERE, "Invalid binder");
            }
        }
    }

    private void confirmSavedGoal(int target, ReadingGoal.GoalType goalType) {
        newGoalDialog.close();
        String notificationMessage =
                String.format("Set your reading goal of %d %s", target, goalType.toString().toLowerCase());
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

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Vaadin's event bus system. A registered listener can be notified when a save or delete event is fired
     */
    public static abstract class GoalFormEvent extends ComponentEvent<ReadingGoalForm> {
        private ReadingGoal readingGoal;

        protected GoalFormEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, false);
            this.readingGoal = readingGoal;
        }

        public ReadingGoal getReadingGoal() {
            return readingGoal;
        }
    }

    public static class SaveEvent extends GoalFormEvent {
        SaveEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }

    public static class DeleteEvent extends GoalFormEvent {
        DeleteEvent(ReadingGoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }
}

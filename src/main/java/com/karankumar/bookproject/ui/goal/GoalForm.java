package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Vaadin view that contains a form for setting a new reading goal
 */
public class GoalForm extends VerticalLayout {

    private static final Logger LOGGER = Logger.getLogger(GoalForm.class.getName());

    private final Dialog newGoalDialog;
    private final Binder<ReadingGoal> binder = new BeanValidationBinder<>(ReadingGoal.class);
    private final IntegerField booksToRead;
    private final Button saveButton;

    public GoalForm() {
        booksToRead = new IntegerField();
        configureBooksToRead();

        saveButton = new Button("Save");
        configureSaveButton(saveButton);

        VerticalLayout verticalLayout = new VerticalLayout(booksToRead, saveButton);
        newGoalDialog = new Dialog();
        newGoalDialog.add(verticalLayout);

        add(newGoalDialog);

        configureBinder();
    }

    private void configureBooksToRead() {
        booksToRead.setPlaceholder("Books to read");
        booksToRead.setClearButtonVisible(true);
        booksToRead.setMin(1);
        booksToRead.setHasControls(true);
        booksToRead.setMinWidth("13em");
    }

    private void configureSaveButton(Button save) {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setMinWidth("13em");
        save.addClickListener(click -> validateOnSave());
    }

    private void validateOnSave() {
        if (binder.isValid()) {

            if (binder.getBean() == null) {
                LOGGER.log(Level.SEVERE, "Binder reading goal bean is null");
                if (booksToRead.getValue() != null) {
                    binder.setBean(new ReadingGoal(booksToRead.getValue()));
                    LOGGER.log(Level.INFO, "Setting the bean");
                }
            } else {
                LOGGER.log(Level.INFO, "Binder reading goal bean is not null");
            }

            fireEvent(new SaveEvent(this, binder.getBean()));
            LOGGER.log(Level.INFO, "Not Null valid binder");

            confirmSavedGoal();
        } else {
            LOGGER.log(Level.SEVERE, "Null valid binder");
        }
    }

    private void confirmSavedGoal() {
        newGoalDialog.close();
        Notification notification = new Notification("Set your reading goal", 3000);
        notification.open();
    }

    public void openForm() {
        newGoalDialog.open();
    }

    private void configureBinder() {
        binder.forField(booksToRead)
              .asRequired("Please enter a reading goal")
              .bind(ReadingGoal::getBooksToRead, ReadingGoal::setBooksToRead);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events
    public static abstract class GoalFormEvent extends ComponentEvent<GoalForm> {
        private ReadingGoal readingGoal;

        protected GoalFormEvent(GoalForm source, ReadingGoal readingGoal) {
            super(source, false);
            this.readingGoal = readingGoal;
        }

        public ReadingGoal getReadingGoal() {
            return readingGoal;
        }
    }

    public static class SaveEvent extends GoalFormEvent {
        SaveEvent(GoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }

    public static class DeleteEvent extends GoalFormEvent {
        DeleteEvent(GoalForm source, ReadingGoal readingGoal) {
            super(source, readingGoal);
        }
    }
}

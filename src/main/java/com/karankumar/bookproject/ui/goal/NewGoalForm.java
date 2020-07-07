package com.karankumar.bookproject.ui.goal;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

/**
 * A Vaadin view that contains a form for setting a new reading goal
 */
public class NewGoalForm extends VerticalLayout {

    private final Dialog newGoalDialog;

    public NewGoalForm() {
        IntegerField booksToRead = new IntegerField();
        booksToRead.setPlaceholder("Books to read");
        booksToRead.setClearButtonVisible(true);
        booksToRead.setMin(1);
        booksToRead.setHasControls(true);
        booksToRead.setMinWidth("13em");

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setMinWidth("13em");

        VerticalLayout verticalLayout = new VerticalLayout(booksToRead, save);
        newGoalDialog = new Dialog();
        newGoalDialog.add(verticalLayout);

        add(newGoalDialog);
    }

    public void openForm() {
        newGoalDialog.open();
    }
}

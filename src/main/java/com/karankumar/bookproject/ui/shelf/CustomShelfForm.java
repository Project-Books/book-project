package com.karankumar.bookproject.ui.shelf;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CustomShelfForm extends VerticalLayout {
    private final Dialog dialog;

    public CustomShelfForm() {
        FormLayout formLayout = new FormLayout();
        dialog = new Dialog();
        dialog.add(formLayout);
        dialog.setCloseOnOutsideClick(true);
        add(dialog);

        TextField shelfName = createShelfNameField();
        formLayout.addFormItem(shelfName, "Shelf name");
        formLayout.add(createSaveButton());
    }

    private TextField createShelfNameField() {
        TextField shelfNameField = new TextField();
        shelfNameField.setClearButtonVisible(true);
        shelfNameField.setPlaceholder("Enter shelf name");
        shelfNameField.setMinWidth("13em");
        return shelfNameField;
    }

    private Button createSaveButton() {
        Button save = new Button();
        save.setText("Save shelf");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setDisableOnClick(true);
        return save;
    }

    public void addShelf() {
        dialog.open();
    }
}

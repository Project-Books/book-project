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

package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.CustomShelfUtils;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;
import org.hibernate.sql.Delete;

import java.util.List;

public class CustomShelfForm extends VerticalLayout {
    private final Dialog dialog;

    private final CustomShelfService customShelfService;
    private final PredefinedShelfService predefinedShelfService;
    private final BooksInShelfView owner;

    private Binder<CustomShelf> binder = new BeanValidationBinder<>(CustomShelf.class);
    private final TextField shelfNameField = new TextField();
    private Button delete;
    private String deletingShelf;

    public CustomShelfForm(CustomShelfService customShelfService,
                           PredefinedShelfService predefinedShelfService,
                           BooksInShelfView owner) {
        FormLayout formLayout = new FormLayout();
        dialog = new Dialog();
        dialog.add(new H3("Add custom shelf"), formLayout);
        dialog.setCloseOnOutsideClick(true);
        add(dialog);

        this.customShelfService = customShelfService;
        this.predefinedShelfService = predefinedShelfService;
        this.owner = owner;

        bindFormFields();

        configureShelfNameField();
        formLayout.addFormItem(shelfNameField, "Shelf name");
        formLayout.add(createSaveButton());
        formLayout.add(createDeleteButton());
    }

    private void bindFormFields() {
        binder.forField(shelfNameField)
              .asRequired("Please enter a shelf name")
              .withValidator(isUniqueShelfName(),
                      "This shelf name already exists. Please enter a new shelf name")
              .bind(CustomShelf::getShelfName, CustomShelf::setShelfName);
    }

    private SerializablePredicate<? super String> isUniqueShelfName() {
        return shelfName -> !customShelfNameAlreadyUsed(shelfName) &&
                !customShelfNameMatchesPredefinedShelfName(shelfName);
    }

    private boolean customShelfNameAlreadyUsed(String customShelfName) {
        List<String> customShelfNames = new CustomShelfUtils(customShelfService).getCustomShelfNames();
        return customShelfNames.contains(customShelfName);
    }

    private boolean customShelfNameMatchesPredefinedShelfName(String shelfName) {
        List<String> predefinedShelfNames =
                new PredefinedShelfUtils(predefinedShelfService).getPredefinedShelfNamesAsStrings();
        return predefinedShelfNames.contains(shelfName);
    }

    private void configureShelfNameField() {
        shelfNameField.setClearButtonVisible(true);
        shelfNameField.setPlaceholder("Enter shelf name");
        shelfNameField.setMinWidth("13em");
    }

    private Button createSaveButton() {
        Button save = new Button();
        save.setText("Save shelf");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> validateOnSave());
        return save;
    }

    private Button createDeleteButton() {
        delete = new Button();
        delete.setText("Delete shelf");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(event -> validateOnDelete());
        return delete;
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            setCustomShelfBean();
            fireEvent(new SaveEvent(this, binder.getBean()));
            closeForm();
            if (binder.getBean() != null) {
                showSaveConfirmation();
                owner.setEditEnabled(false);
            } else {
                showSaveError();
            }
        }
    }

    private void validateOnDelete() {
        CustomShelf removed = this.getCustomShelf(deletingShelf);
        fireEvent(new DeleteEvent(this, removed));
        binder.removeBean();
        closeForm();
        if (binder.getBean() == null) {
            this.showDeleteConfirmation();
            owner.whichShelf.updateShelfList();
            owner.setEditEnabled(false);
        } else {
            this.showDeleteError();
        }
    }

    private void setCustomShelfBean() {
        CustomShelf customShelf;
        if (deletingShelf.equals("")) {
            customShelf = new CustomShelf(shelfNameField.getValue());
        } else {
            customShelf = this.getCustomShelf(deletingShelf);
            customShelf.setShelfName(shelfNameField.getValue());
        }
        binder.setBean(customShelf);
    }

    private void closeForm() {
        dialog.close();
    }

    private void showSaveConfirmation() {
        Notification.show("Saved shelf: " + binder.getBean().getShelfName());
    }

    private void showSaveError() {
        Notification.show("Could not save shelf.");
    }

    private void showDeleteConfirmation() {
        Notification.show("Deleted shelf: " + deletingShelf);
    }

    private void showDeleteError() {
        Notification.show("Could not delete shelf.");
    }

    public void addShelf() {
        delete.setVisible(false);
        deletingShelf = "";
        shelfNameField.setValue(deletingShelf);
        dialog.open();
    }

    public void editShelf(String chosenShelf) {
        delete.setVisible(true);
        deletingShelf = chosenShelf;
        shelfNameField.setValue(deletingShelf);
        dialog.open();
    }

    public CustomShelf getCustomShelf(String shelfToFind) {
        return customShelfService.findAll(deletingShelf).get(0);
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class CustomShelfFormEvent extends ComponentEvent<CustomShelfForm> {
        private CustomShelf customShelf;

        protected CustomShelfFormEvent(CustomShelfForm source, CustomShelf customShelf) {
            super(source, false);
            this.customShelf = customShelf;
        }

        public CustomShelf getCustomShelf() {
            return customShelf;
        }
    }

    public static class SaveEvent extends CustomShelfForm.CustomShelfFormEvent {
        SaveEvent(CustomShelfForm source, CustomShelf customShelf) {
            super(source, customShelf);
        }
    }

    // TODO: implement deleting a custom shelf
    public static class DeleteEvent extends CustomShelfForm.CustomShelfFormEvent {
        DeleteEvent(CustomShelfForm source, CustomShelf customShelf) {
            super(source, customShelf);
            source.customShelfService.delete(customShelf);
        }
    }
}

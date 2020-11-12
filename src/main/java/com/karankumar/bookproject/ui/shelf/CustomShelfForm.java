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

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;

import java.util.List;

@CssImport(value = "./styles/shelf-form-styles.css")
public class CustomShelfForm extends VerticalLayout {
    private final Dialog dialog;

    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;

    private final Binder<CustomShelf> binder = new BeanValidationBinder<>(CustomShelf.class);

    @VisibleForTesting
    final TextField shelfNameField = new TextField();
    @VisibleForTesting
    final Button saveButton = new Button();

    public CustomShelfForm(PredefinedShelfService predefinedShelfService, CustomShelfService customShelfService) {
        FormLayout formLayout = new FormLayout();
        dialog = new Dialog();
        dialog.add(new H3("Add custom shelf"), formLayout);
        dialog.setCloseOnOutsideClick(true);
        dialog.addDialogCloseActionListener(dialogCloseActionEvent -> closeForm());
        add(dialog);

        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;

        bindFormFields();

        configureShelfNameField();
        formLayout.addFormItem(shelfNameField, "Shelf name");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );
        formLayout.add(createSaveButton());
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
        return customShelfService.getCustomShelfNames().contains(customShelfName);
    }

    private boolean customShelfNameMatchesPredefinedShelfName(String shelfName) {
        List<String> predefinedShelfNames =
                predefinedShelfService.getPredefinedShelfNamesAsStrings();
        return predefinedShelfNames.contains(shelfName);
    }

    private void configureShelfNameField() {
        shelfNameField.setClearButtonVisible(true);
        shelfNameField.setPlaceholder("Enter shelf name");
        shelfNameField.addClassName("shelfFormInputField");
        shelfNameField.setValueChangeMode(ValueChangeMode.EAGER);
        shelfNameField.addValueChangeListener(event -> saveButton.setEnabled(binder.isValid()));
    }

    private Button createSaveButton() {
        saveButton.setText("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setDisableOnClick(true);
        saveButton.addClassName("shelfFormSaveButton");
        saveButton.addClickListener(event -> validateOnSave());
        saveButton.setEnabled(binder.isValid());
        return saveButton;
    }

    private void validateOnSave() {
        if (binder.isValid()) {
            setCustomShelfBean();
            fireEvent(new SaveEvent(this, binder.getBean()));
            closeForm();
            if (binder.getBean() != null) {
                showSaveConfirmation();
            } else {
                showSaveError();
            }
        }
    }

    private void setCustomShelfBean() {
        binder.setBean(customShelfService.createCustomShelf(shelfNameField.getValue()));
    }

    @VisibleForTesting
    void closeForm() {
        shelfNameField.clear();
        shelfNameField.setInvalid(false);
        dialog.close();
    }

    private void showSaveConfirmation() {
        Notification.show("Saved shelf: " + binder.getBean().getShelfName());
    }

    private void showSaveError() {
        Notification.show("Could not save shelf.");
    }

    public void addShelf() {
        dialog.open();
        addClassNameToForm();
    }

    private void addClassNameToForm() {
        UI.getCurrent().getPage()
                .executeJs("document.getElementById(\"overlay\")" +
                            ".shadowRoot" +
                            ".getElementById('overlay')" +
                            ".classList.add('shelfFormOverlay');\n");
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class CustomShelfFormEvent extends ComponentEvent<CustomShelfForm> {
        private final CustomShelf customShelf;

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
        }
    }
}

package com.karankumar.bookproject.ui.registration;

import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class RegistrationForm extends FormLayout {
    private final UserService userService;
    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    private final TextField usernameField = new TextField("Username");
    private final EmailField emailField = new EmailField("Email Address");
    private final PasswordField passwordField = new PasswordField("Password");
    private final PasswordField passwordConfirmationField = new PasswordField("Confirm Password");
    private final Button registerButton = new Button("Register");

    public RegistrationForm(UserService userService) {
        this.userService = userService;

        usernameField.setRequired(true);
        emailField.setRequiredIndicatorVisible(true);
        passwordField.setRequired(true);
        passwordConfirmationField.setRequired(true);

        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(buttonClickEvent -> {
            User user = User.builder().build();

            if (binder.writeBeanIfValid(user)) {
                this.userService.register(user);
            }
        });

        binder.bind(usernameField, "username");
        binder.bind(emailField, "email");
        binder.forField(passwordField)
              .withValidator(password -> {
                          boolean valid = passwordConfirmationField.getValue().equals(password);
                          passwordConfirmationField.setInvalid(!valid);
                          return valid;
                      },
                      "The passwords do not match")
              .bind("password");
        binder.forField(passwordConfirmationField)
              .withValidator(password -> {
                          boolean valid = passwordField.getValue().equals(password);
                          passwordField.setInvalid(!valid);
                          return valid;
                      },
                      "The passwords do not match")
              .bind("passwordConfirmation");

        add(
                usernameField,
                emailField,
                passwordField,
                passwordConfirmationField,
                registerButton
        );
    }
}

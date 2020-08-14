package com.karankumar.bookproject.ui.registration;

import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;

public class RegistrationForm extends FormLayout {
    private final UserService userService;
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    private final TextField usernameField = new TextField("Username");
    private final EmailField emailField = new EmailField("Email Address");
    private final PasswordField passwordField = new PasswordField("Password");
    private final PasswordField passwordConfirmationField = new PasswordField("Confirm Password");
    private final Text passwordHint = new Text(
            "The password must be at least 8 characters long and consist of at least one lowercase letter, one uppercase letter, one digit, and one special character from @#$%^&+=");

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

        binder.forField(usernameField)
              .withValidator(userService::usernameIsNotInUse,
                      "A user with this username does already exist")
              .bind("username");
        binder.forField(emailField)
              .withValidator(new EmailValidator("Please enter a correct email address"))
              .withValidator(userService::emailIsNotInUse,
                      "A user with this email address already exists")
              .bind("email");

        var passwordBinding =
                binder.forField(passwordField)
                      .withValidator(
                              password -> passwordConfirmationField.getValue().equals(password),
                              "The passwords do not match")
                      .bind("password");
        var passwordConfirmationBinding =
                binder.forField(passwordConfirmationField)
                      .withValidator(password -> passwordField.getValue()
                                                              .equals(password),
                              "The passwords do not match")
                      .bind("passwordConfirmation");
        passwordField.addValueChangeListener(e -> passwordConfirmationBinding.validate());
        passwordConfirmationField.addValueChangeListener(e -> passwordBinding.validate());

        add(
                usernameField,
                emailField,
                passwordField,
                passwordConfirmationField,
                passwordHint,
                registerButton
        );
    }
}

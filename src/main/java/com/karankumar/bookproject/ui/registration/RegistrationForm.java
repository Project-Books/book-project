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

package com.karankumar.bookproject.ui.registration;

import com.karankumar.bookproject.backend.constraints.PasswordStrength;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import com.karankumar.bookproject.ui.login.LoginView;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;
import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;

import com.nulabinc.zxcvbn.Zxcvbn;

@Log
@CssImport(value = "./styles/custom-bar-styles.css", themeFor = "vaadin-progress-bar")
public class RegistrationForm extends FormLayout {
    private final UserService userService;
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    private final TextField usernameField = new TextField("Username");
    private final EmailField emailField = new EmailField("Email Address");
    private final PasswordField passwordField = new PasswordField("Password");
    private final ProgressBar passwordStrengthMeter = new ProgressBar();
    private final String blankPasswordMessage = "Password is blank";
    private final Text passwordStrengthDescriptor = new Text(blankPasswordMessage);
    private final PasswordField passwordConfirmationField = new PasswordField("Confirm Password");
    private final Button registerButton = new Button("Register");
    private final Span errorMessage = new Span();

    // Flag for disabling first run for password validation
    private boolean enablePasswordValidation = false;

    public RegistrationForm(UserService userService) {
        this.userService = userService;

        usernameField.setRequired(true);
        usernameField.setId("username");

        emailField.setRequiredIndicatorVisible(true);
        emailField.setId("email");

        passwordField.setRequired(true);
        passwordField.setId("password");

        passwordConfirmationField.setRequired(true);
        passwordConfirmationField.setId("password-confirmation");

        errorMessage.setId("error-message");

        registerButton.setId("register");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(buttonClickEvent -> {
            User user = User.builder()
                            .build();

            if (binder.writeBeanIfValid(user)) {
                try {
                    this.userService.register(user);
                    getUI().ifPresent(ui -> ui.navigate(BooksInShelfView.class));
                } catch (Exception e) {
                    LOGGER.severe("Could not register the user " + user);
                    e.printStackTrace();

                    errorMessage.setText(
                            "A server error occurred when registering. Please try again later.");
                }
            } else {
                errorMessage.setText("There are errors in the registration form.");
            }
        });

        addFieldValidations(userService);
        
        passwordField.addValueChangeListener(e -> {
            int passwordScore = new Zxcvbn().measure(passwordField.getValue()).getScore();
            passwordStrengthMeter.setValue((passwordScore + 1) / 5.0);
            if(passwordField.isEmpty()) {
        	passwordStrengthDescriptor.setText(blankPasswordMessage);
        	passwordStrengthMeter.setValue(0);
            } else {
        	setPasswordStrengthMeterColor(passwordScore);
        	passwordStrengthDescriptor.setText("Password Strength: " + PasswordStrength.values()[passwordScore]);
            }
        });

        this.setMaxWidth("360px");
        this.getStyle()
            .set("margin", "0 auto");
        this.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );

        add(
                new H2("Register"),
                usernameField,
                emailField,
                passwordField,
                passwordStrengthMeter,
                passwordStrengthDescriptor,
                passwordConfirmationField,
                errorMessage,
                registerButton,
                new Button("Go back to Login",
                        e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)))
        );
    }

    private void addFieldValidations(UserService userService) {
        binder.forField(usernameField)
              .withValidator(userService::usernameIsNotInUse,
                      "A user with this username does already exist")
              .bind("username");

        binder.forField(emailField)
              .withValidator(new EmailValidator("Please enter a correct email address"))
              .withValidator(userService::emailIsNotInUse,
                      "A user with this email address already exists")
              .bind("email");

        binder.forField(passwordField)
              .withValidator(this::passwordValidator)
              .bind("password");

        passwordConfirmationField.addValueChangeListener(e -> {
            // The user has modified the second field, now we can validate and show errors.
            enablePasswordValidation = true;
            binder.validate();
        });
        
        binder.setStatusLabel(errorMessage);
        errorMessage.getStyle()
                    .set("color", "var(--lumo-error-text-color)");
    }

    private ValidationResult passwordValidator(String password, ValueContext ctx) {
        if (!enablePasswordValidation) {
            // user hasn't visited the field yet, so don't validate just yet
            return ValidationResult.ok();
        }

        String passwordConfirmation = passwordConfirmationField.getValue();

        if (StringUtils.equals(password, passwordConfirmation)) {
            return ValidationResult.ok();
        }

        return ValidationResult.error("Passwords do not match");
    }
    
    private void setPasswordStrengthMeterColor(int passwordScore) {
	switch(passwordScore){
        case 0:
            passwordStrengthMeter.setClassName("weak-indicator");
            break;
        case 1:
            passwordStrengthMeter.setClassName("fair-indicator");
            break;
        case 2:
            passwordStrengthMeter.setClassName("good-indicator");
            break;
        case 3:
            passwordStrengthMeter.setClassName("strong-indicator");
            break;
        case 4:
            passwordStrengthMeter.setClassName("very-strong-indicator");
            break;
        default:
            throw new IllegalArgumentException("The password score has to lie between 0 and 5 (exclusive)");
        }
    }
}

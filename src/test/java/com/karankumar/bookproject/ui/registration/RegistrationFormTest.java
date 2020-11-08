/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.registration;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.RoleRepository;
import com.karankumar.bookproject.backend.repository.UserRepository;
import com.karankumar.bookproject.ui.MockSpringServlet;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.SpringServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.github.mvysny.kaributesting.v10.LocatorJ._setValue;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WebAppConfiguration
@Transactional
@DisplayName("RegistrationForm should")
class RegistrationFormTest {
    private final static User VALID_TEST_USER = User.builder()
                                                    .username("validTestUser")
                                                    .email("valid@testemail.com")
                                                    .password("asdfASDF1234=")
                                                    .build();

    private static Routes routes;

    private final ApplicationContext ctx;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    RegistrationFormTest(UserRepository userRepository, RoleRepository roleRepository,
                         ApplicationContext ctx) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.ctx = ctx;
    }

    @BeforeAll
    public static void discoverRoutes() {
        MockVaadin.setup();
        routes = new Routes().autoDiscoverViews("com.karankumar.bookproject.ui");
    }

    @BeforeEach
    public void setup() {
        roleRepository.save(new Role("USER"));
        final SpringServlet servlet = new MockSpringServlet(routes, ctx);
        MockVaadin.setup(UI::new, servlet);

        UI.getCurrent().navigate(RegistrationView.class);
    }

    @Test
    void showErrorOnPasswordFieldNotMatchingTheRules() {
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));

        _setValue(passwordField, "asdf");

        assertThat(passwordField.getErrorMessage()).isNotBlank();
    }

    @Test
    void showErrorOnWrongPasswordConfirmation() {
        // given
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));
        PasswordField passwordConfirmationField =
                _get(PasswordField.class, spec -> spec.withId("password-confirmation"));

        // when
        _setValue(passwordField, "asdfASDF1234=");
        _setValue(passwordConfirmationField, "somethingelse");

        // then
        assertThat(passwordField.getErrorMessage()).isNotBlank();
    }

    @Test
    void acceptPasswordFieldMatchingTheRules() {
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));

        _setValue(passwordField, "asdfASDF1234=");

        assertThat(passwordField.getErrorMessage()).isBlank();
    }

    @Test
    void showErrorWhenUsernameFieldLessThanFiveChars() {
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, "asdf");

        assertThat(usernameField.getErrorMessage()).isNotBlank();
    }

    @Test
    void showErrorWhenUsernameInUse() {
        userRepository.save(VALID_TEST_USER);
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, VALID_TEST_USER.getUsername());

        assertThat(usernameField.getErrorMessage()).isNotBlank();
    }

    @Test
    void acceptValidUsername() {
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, "asdfgh");

        assertThat(usernameField.getErrorMessage()).isBlank();
    }

    @Test
    void showErrorWhenEmailIsInvalid() {
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        _setValue(emailField, "thisisnotanemail");

        assertThat(emailField.getErrorMessage()).isNotBlank();
    }

    @Test
    void showErrorWhenEmailInUse() {
        // given
        userRepository.save(VALID_TEST_USER);
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        // when
        _setValue(emailField, VALID_TEST_USER.getEmail());

        // then
        assertThat(emailField.getErrorMessage()).isNotBlank();
    }

    @Test
    void acceptValidEmail() {
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        _setValue(emailField, "asdf@asdf.de");

        assertThat(emailField.getErrorMessage()).isBlank();
    }

    @Test
    void notSendWithError() {
        // given
        Span errorMessage = _get(Span.class, spec -> spec.withId("error-message"));
        Button registerButton = _get(Button.class, spec -> spec.withId("register"));

        // when
        _click(registerButton);

        // then
        assertThat(errorMessage.getText()).contains("There are errors in the registration form.");
    }

    @Test
    void sendWithoutError() {
        // given
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));
        PasswordField passwordConfirmationField =
                _get(PasswordField.class, spec -> spec.withId("password-confirmation"));
        Button registerButton = _get(Button.class, spec -> spec.withId("register"));

        // when
        _setValue(usernameField, "testusername");
        _setValue(emailField, "testusername@test.mail");
        _setValue(passwordField, "asdfASDF1234=");
        _setValue(passwordConfirmationField, "asdfASDF1234=");
        _click(registerButton);

        // then
        assertThat(userRepository.findByUsername("testusername")).isPresent();
    }

    @Test
    void showErrorWhenPasswordIsTooLong() {
        // given
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));

        // when
        _setValue(passwordField, generateInvalidPassword());

        // then
        assertThat(passwordField.getErrorMessage()).isNotBlank();
    }

    private String generateInvalidPassword() {
        return ".".repeat(RegistrationForm.MAX_PASSWORD_LENGTH);
    }
}

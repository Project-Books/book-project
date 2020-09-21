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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.github.mvysny.kaributesting.v10.LocatorJ._setValue;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WebAppConfiguration
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
    void registrationForm_passwordFieldNotMatchingTheRules_hasError() {
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));

        _setValue(passwordField, "asdf");

        assertThat(passwordField.getErrorMessage()).isNotBlank();
    }

    @Test
    void registrationForm_passwordConfirmationWrong_hasError() {
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));
        PasswordField passwordConfirmationField =
                _get(PasswordField.class, spec -> spec.withId("password-confirmation"));

        _setValue(passwordField, "asdfASDF1234=");
        _setValue(passwordConfirmationField, "somethingelse");

        assertThat(passwordField.getErrorMessage()).isNotBlank();
    }

    @Test
    void registrationForm_passwordFieldMatchingTheRules_successful() {
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));

        _setValue(passwordField, "asdfASDF1234=");

        assertThat(passwordField.getErrorMessage()).isBlank();
    }

    @Test
    void registrationForm_usernameFieldLessThanFiveChars_hasError() {
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, "asdf");

        assertThat(usernameField.getErrorMessage()).isNotBlank();
    }

    @Test
    void registrationForm_usernameInUse_hasError() {
        userRepository.save(VALID_TEST_USER);
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, VALID_TEST_USER.getUsername());

        assertThat(usernameField.getErrorMessage()).isNotBlank();
    }

    @Test
    void registrationForm_usernameCorrect_successful() {
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, "asdfgh");

        assertThat(usernameField.getErrorMessage()).isBlank();
    }

    @Test
    void registrationForm_emailInvalid_hasError() {
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        _setValue(emailField, "thisisnotanemail");

        assertThat(emailField.getErrorMessage()).isNotBlank();
    }

    @Test
    void registrationForm_emailInUse_hasError() {
        userRepository.save(VALID_TEST_USER);
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        _setValue(emailField, VALID_TEST_USER.getEmail());

        assertThat(emailField.getErrorMessage()).isNotBlank();
    }

    @Test
    void registrationForm_emailCorrect_successful() {
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        _setValue(emailField, "asdf@asdf.de");

        assertThat(emailField.getErrorMessage()).isBlank();
    }

    @Test
    void registrationForm_withError_cannotBeSent() {
        Span errorMessage = _get(Span.class, spec -> spec.withId("error-message"));
        Button registerButton = _get(Button.class, spec -> spec.withId("register"));

        _click(registerButton);

        assertThat(errorMessage.getText()).contains("There are errors in the registration form.");
    }

    @Test
    void registrationForm_withoutError_getsSent() {
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));
        PasswordField passwordField = _get(PasswordField.class, spec -> spec.withId("password"));
        PasswordField passwordConfirmationField =
                _get(PasswordField.class, spec -> spec.withId("password-confirmation"));
        Button registerButton = _get(Button.class, spec -> spec.withId("register"));

        _setValue(usernameField, "testusername");
        _setValue(emailField, "testusername@test.mail");
        _setValue(passwordField, "asdfASDF1234=");
        _setValue(passwordConfirmationField, "asdfASDF1234=");
        _click(registerButton);

        assertThat(userRepository.findByUsername("testusername")).isPresent();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}

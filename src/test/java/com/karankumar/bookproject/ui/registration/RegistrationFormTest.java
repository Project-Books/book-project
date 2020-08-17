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
    private final static User TEST_USER = User.builder()
                                              .username("testuser")
                                              .email("testemail")
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

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
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
        PasswordField passwordConfirmationField = _get(PasswordField.class, spec -> spec.withId("password-confirmation"));


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
        userRepository.save(TEST_USER);
        TextField usernameField = _get(TextField.class, spec -> spec.withId("username"));

        _setValue(usernameField, TEST_USER.getUsername());

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
        userRepository.save(TEST_USER);
        EmailField emailField = _get(EmailField.class, spec -> spec.withId("email"));

        _setValue(emailField, TEST_USER.getEmail());

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
        PasswordField passwordConfirmationField = _get(PasswordField.class, spec -> spec.withId("password-confirmation"));
        Button registerButton = _get(Button.class, spec -> spec.withId("register"));

        _setValue(usernameField, "testusername");
        _setValue(emailField, "testusername@test.mail");
        _setValue(passwordField, "asdfASDF1234=");
        _setValue(passwordConfirmationField, "asdfASDF1234=");
        _click(registerButton);

        assertThat(userRepository.findByUsername("testusername").isPresent()).isTrue();
    }

}

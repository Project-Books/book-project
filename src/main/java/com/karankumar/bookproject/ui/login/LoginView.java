package com.karankumar.bookproject.ui.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@Route("login")
@PageTitle("Login | Book Project")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        // centre
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        loginForm.setAction("login");

        add(
                new H1("Book Project"),
                loginForm
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .getOrDefault("error", Collections.emptyList())
                .isEmpty()) {
            loginForm.setError(true);

        }
    }
}

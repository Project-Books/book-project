package com.karankumar.bookproject.ui.deleteAccount;

import com.karankumar.bookproject.backend.service.UserService;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.settings.SettingsView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.java.Log;

@Route(value = "deleteAccount", layout = MainView.class)
@PageTitle("Delete Account | Book Project")
@Log
public class DeleteAccountView extends VerticalLayout {
    private static final String DELETE_ACCOUNT = "Delete account";
    private static final String EXPORT = "Export account data";

    public DeleteAccountView(UserService userService)   {
        VerticalLayout verticalLayout = new VerticalLayout();
        VerticalLayout mainFunctionality = new VerticalLayout();

        Icon backIcon = new Icon(VaadinIcon.ARROW_LEFT);
        verticalLayout.add(backIcon);

        backIcon.getStyle().set("cursor", "pointer");
        backIcon.addClickListener(
                event -> getUI().ifPresent(ui -> ui.navigate(SettingsView.class)));


        mainFunctionality.add(new H1(DELETE_ACCOUNT));
        mainFunctionality.add(new H3("Warning: this action is irreversible"));
        mainFunctionality.add(new Text("If you'd like to first export your data, please do so below. " +
                "This gives you a chance to save your data in case you'd ever like to create an account again."));
        Button exportAccountButton = new Button(EXPORT, click -> {

        });
        mainFunctionality.add(exportAccountButton);

        mainFunctionality.add(new Text("If you're sure you want to delete your account, confirm deletion by entering your password in the field below"));

        PasswordField passwordField = new PasswordField();
        mainFunctionality.add(passwordField);

        Button deleteAccountButton = new Button(DELETE_ACCOUNT, click -> {

        });
        mainFunctionality.add(deleteAccountButton);
        mainFunctionality.setAlignItems(Alignment.CENTER);
        verticalLayout.add(mainFunctionality);
        Button cancelButton = new Button("Cancel",   event -> getUI().ifPresent(ui -> ui.navigate(SettingsView.class)));
        verticalLayout.add(cancelButton);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, cancelButton);
        add(verticalLayout);
    }

}

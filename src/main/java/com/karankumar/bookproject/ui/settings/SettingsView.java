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

package com.karankumar.bookproject.ui.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karankumar.bookproject.backend.dto.GoodreadsBookImport;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.ImportService;
import com.karankumar.bookproject.backend.service.UserService;
import com.karankumar.bookproject.backend.util.CsvUtils;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.components.AppFooter;
import com.karankumar.bookproject.ui.components.dialog.DeleteAccountDialog;
import com.karankumar.bookproject.ui.components.dialog.ResetShelvesDialog;
import com.karankumar.bookproject.ui.components.toggle.SwitchToggle;
import com.karankumar.bookproject.ui.deleteAccount.DeleteAccountView;
import com.karankumar.bookproject.ui.registration.RegistrationView;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.java.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings | Book Project")
@Log
public class SettingsView extends HorizontalLayout {

    private static final String ENABLE_DARK_MODE = "Enable dark mode";
    private static final String APPEARANCE = "Appearance:";
    private static final String ACCOUNT = "Account:";
    private static final String MY_BOOKS = "My books:";
    private static final String DISABLE_DARK_MODE = "Disable dark mode";
    private static final String EMAIL = "Email: ";
    private static final String PASSWORD = "Password: ";

    private static SwitchToggle darkModeToggle;
    private static boolean darkModeOn = false;
    private static final Label darkModeLabel = new Label(ENABLE_DARK_MODE);

    private static final Label emailLabel = new Label(EMAIL);
    private static final Label passwordLabel = new Label(PASSWORD);

    private static final String DELETE_ACCOUNT = "Delete account";
    private static DeleteAccountDialog deleteAccountDialog;

    private static final H3 accountHeading = new H3(ACCOUNT);
    private static final H3 appearanceHeading = new H3(APPEARANCE);
    private static final H3 myBooksHeading = new H3(MY_BOOKS);
    private static final HtmlComponent lineBreak = new HtmlComponent("br");

    // Clear Shelves
    private ResetShelvesDialog resetShelvesDialog;
    private static final String CLEAR_SHELVES = "Reset shelves";

    private static final String EXPORT_BOOKS = "Export";
    private static final Anchor exportBooksAnchor = new Anchor();

    private static final String IMPORT_FROM_GOODREADS = "Import from Goodreads";
    private static final String UPLOAD_DROP_LABEL = "Upload a file in .csv format";
    private final MemoryBuffer importGoodreadsMemoryBuffer = new MemoryBuffer();
    private final Upload importGoodreadsUpload = new Upload(importGoodreadsMemoryBuffer);

    private final BookService bookService;
    private final transient ImportService importService;

    private final UserService userService;
    static {
        Account();
        Appearance();
    }

    private static void Appearance()   {
        configureDarkModeToggle();
        createExportBooksAnchor();
    }
    private static void Account()   {
        configureDarkModeToggle();
    }

    private static void configureDarkModeToggle() {
        darkModeToggle = new SwitchToggle();
        darkModeToggle.addClickListener(e -> {

            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                darkModeOn = false;
            } else {
                themeList.add(Lumo.DARK);
                darkModeOn = true;
            }
            updateDarkModeLabel();
        });
    }

    private static void createExportBooksAnchor() {
        exportBooksAnchor.getElement().setAttribute("download", true);
        exportBooksAnchor.add(new Button(EXPORT_BOOKS, new Icon(VaadinIcon.DOWNLOAD_ALT)));
    }

    SettingsView(BookService bookService, ImportService importService, UserService userService) {
        this.bookService = bookService;
        this.importService = importService;

        this.userService = userService;

        setDarkModeState();

        HorizontalLayout pageLayout = new HorizontalLayout();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(darkModeLabel, darkModeToggle);

        configureExportBooksAnchor();
        configureImportGoodreadsUpload();

        createImportGoodreadsUpload();

        Text email = new Text(userService.getCurrentUser().getEmail());
        Icon editEmail = new Icon(VaadinIcon.PENCIL);
        Icon saveEmail = new Icon(VaadinIcon.PENCIL);
        HorizontalLayout emailLayout = new HorizontalLayout();
        emailLayout.add(emailLabel, email, editEmail);
        editEmail.addClickListener(event -> {
            TextField changeEmail = new TextField();
            emailLayout.replace(email, changeEmail);
            emailLayout.replace(editEmail, saveEmail);
            saveEmail.addClickListener(e -> {
                String newEmail = changeEmail.getValue();
                userService.getCurrentUser().setEmail(newEmail);
                email.setText(newEmail);
                emailLayout.replace(changeEmail, email);
                emailLayout.replace(saveEmail, editEmail);
            });
        });



        Text password = new Text(userService.getCurrentUser().getPassword());
        Icon editPassword = new Icon(VaadinIcon.PENCIL);
        Icon savePassword = new Icon(VaadinIcon.PENCIL);
        HorizontalLayout passwordLayout = new HorizontalLayout(passwordLabel, password, editPassword);
        editPassword.addClickListener(event -> {
            PasswordField changePassword = new PasswordField();
            passwordLayout.replace(password, changePassword);
            passwordLayout.replace(editPassword, savePassword);
            savePassword.addClickListener(e -> {
               userService.getCurrentUser().setPassword(changePassword.getValue());
               password.setText(changePassword.getValue());
               passwordLayout.replace(changePassword, password);
               passwordLayout.replace(savePassword, editPassword);
            });
        });

        VerticalLayout accountLayout = new VerticalLayout(accountHeading, emailLayout, passwordLayout, deleteAccountButton());
        accountLayout.setAlignItems(Alignment.CENTER);
        VerticalLayout verticalLayout = new VerticalLayout(
                appearanceHeading,
                horizontalLayout,
                lineBreak,
                myBooksHeading,
                createClearShelvesButton(),
                exportBooksAnchor,
                importGoodreadsUpload,
                new AppFooter()
        );
        verticalLayout.setAlignItems(Alignment.CENTER);
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        pageLayout.add(accountLayout, verticalLayout);

        add(pageLayout);
    }

    private void createImportGoodreadsUpload() {
        importGoodreadsUpload.setMaxFiles(1);
        importGoodreadsUpload.setAcceptedFileTypes(CsvUtils.TEXT_CSV);
        importGoodreadsUpload.getElement().setAttribute("import-goodreads", true);
        importGoodreadsUpload.setDropLabel(new Label(UPLOAD_DROP_LABEL));
        importGoodreadsUpload.setUploadButton(new Button(IMPORT_FROM_GOODREADS));
    }

    private Button createClearShelvesButton() {
        return new Button(CLEAR_SHELVES, click -> {
            resetShelvesDialog = new ResetShelvesDialog(bookService);
            resetShelvesDialog.openDialog();
        });
    }

    private Button deleteAccountButton()    {
        return new Button(DELETE_ACCOUNT,
            e -> getUI().ifPresent(ui -> ui.navigate(DeleteAccountView.class)));
    };

    private void setDarkModeState() {
        if (darkModeOn) {
            updateDarkModeLabel();
            darkModeToggle.setChecked(true);
        } else {
            darkModeToggle.setChecked(false);
        }
    }

    private static void updateDarkModeLabel() {
        darkModeLabel.setText(darkModeOn ? DISABLE_DARK_MODE : ENABLE_DARK_MODE);
    }

    private void configureExportBooksAnchor() {
        String jsonExportURI = generateJsonResource();
        exportBooksAnchor.setHref(jsonExportURI);
    }

    private void configureImportGoodreadsUpload() {
        importGoodreadsUpload.addSucceededListener(succeededEvent -> {
            try {
                List<GoodreadsBookImport> goodreadsBookImports = CsvUtils.read(
                        importGoodreadsMemoryBuffer.getInputStream(),
                        GoodreadsBookImport.class
                );

                importService.importGoodreadsBooks(goodreadsBookImports);
            } catch (IOException e) {
                LOGGER.severe("Error in reading input file, error: " + e);
            }
        });
    }

    private String generateJsonResource() {
        try {
            byte[] jsonRepresentationForBooks = bookService.getJsonRepresentationForBooksAsString()
                                                           .getBytes();

            final StreamResource resource = new StreamResource("bookExport.json",
                    () -> new ByteArrayInputStream(jsonRepresentationForBooks));
            final StreamRegistration registration = UI.getCurrent()
                                                    .getSession()
                                                    .getResourceRegistry()
                                                    .registerResource(resource);

            return registration.getResourceUri().toString();
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Cannot create json resource." + e);
            return "";
        }
    }
}

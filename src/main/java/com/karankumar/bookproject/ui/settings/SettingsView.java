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
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.components.dialog.ResetShelvesDialog;
import com.karankumar.bookproject.ui.components.toggle.SwitchToggle;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.java.Log;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings | Book Project")
@Log
public class SettingsView extends HorizontalLayout {

    private static final String ENABLE_DARK_MODE = "Enable dark mode";
    private static final String APPEARANCE = "Appearance:";
    private static final String MY_BOOKS = "My books:";
    private static final String DISABLE_DARK_MODE = "Disable dark mode";

    private static SwitchToggle darkModeToggle;
    private static boolean darkModeOn = false;
    private static final Label darkModeLabel = new Label(ENABLE_DARK_MODE);

    private static final H3 appearanceHeading = new H3(APPEARANCE);
    private static final H3 myBooksHeading = new H3(MY_BOOKS);
    private static final HtmlComponent lineBreak = new HtmlComponent("br");

    // Clear Shelves
    private ResetShelvesDialog resetShelvesDialog;
    private static final String CLEAR_SHELVES = "Reset shelves";

    private static final String EXPORT_BOOKS = "Export";
    private static final Anchor exportBooksAnchor = new Anchor();

    private final BookService bookService;

    static {
        configureDarkModeToggle();
        createExportBooksAnchor();
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

    SettingsView(BookService bookService) {
        this.bookService = bookService;

        setDarkModeState();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(darkModeLabel, darkModeToggle);

        configureExportBooksAnchor();

        VerticalLayout verticalLayout = new VerticalLayout(
                appearanceHeading,
                horizontalLayout,
                lineBreak,
                myBooksHeading,
                createClearShelvesButton(),
                exportBooksAnchor
        );

        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private Button createClearShelvesButton() {
        return new Button(CLEAR_SHELVES, click -> {
            resetShelvesDialog = new ResetShelvesDialog(bookService);
            resetShelvesDialog.openDialog();
        });
    }

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

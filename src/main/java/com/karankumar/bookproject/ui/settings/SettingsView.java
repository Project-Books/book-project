/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.settings;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.components.toggle.PaperToggle;
import com.karankumar.bookproject.ui.components.dialog.ResetShelvesDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings | Book Project")
@Log
public class SettingsView extends HorizontalLayout {

    private static final String enable = "Enable dark mode";
    private static final String disable = "Disable dark mode";
    private static PaperToggle paperToggle;
    private static Label darkModeLabel = new Label(enable);
    private static boolean darkModeOn = false;

    // -------------- Clear Shelves ----------------------
    private static ResetShelvesDialog resetShelvesDialog;
    private static final String CLEAR_SHELVES = "Clear Shelves";
    private static Button clearShelveButton; // Not labeled 'private' because it is tested in SettingsViewTest
    private static BookService bookService;

    static {
        paperToggle = new PaperToggle();
        paperToggle.addClickListener( e -> {

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


        clearShelveButton = new Button(CLEAR_SHELVES, click -> {
            resetShelvesDialog = new ResetShelvesDialog(bookService);
        });
    }

    SettingsView(@Autowired BookService bookService) {

        SettingsView.bookService = bookService;

        // Darkmode Layout
        if (darkModeOn) {
            updateDarkModeLabel();
            paperToggle.setChecked(true);
        } else {
            paperToggle.setChecked(false);
        }


        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(darkModeLabel, paperToggle);

        VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, clearShelveButton);

        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }


    private static void updateDarkModeLabel() {
        if (darkModeLabel != null) {
            darkModeLabel.setText(darkModeOn ? disable : enable);
        }
    }
}

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


import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.java.Log;

import java.util.List;
import java.util.logging.Level;


/**
 * Vaadin view for the settings page
 */
@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings | Book Project")
@Log
public class SettingsView extends HorizontalLayout {

    // -------------- Enable Dark/Light Mode ----------------------
    private static final String enable = "Enable dark mode";
    private static final String disable = "Disable dark mode";
    private static final Button toggleDarkmode;
    private static boolean darkModeOn = false;


    // -------------- Clear Shelves ----------------------
    private static final String clearShelves = "Clear Shelves";
    private static final String shelvesAlreadyEmpty = "Shelves are empty";
    private static Button clearShelvesButton; // Not labeled 'private' because it is tested in SettingsViewTest
    private static boolean shelvesEmpty = false;
    private static BookService bookService;
    private static Dialog dialog;
    private static Button confirmButton;
    private static Button cancelButton;

    static {
        toggleDarkmode = new Button(enable, click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                darkModeOn = false;
            } else {
                themeList.add(Lumo.DARK);
                darkModeOn = true;
            }
            updateDarkModeButtonText();
        });


        clearShelvesButton = new Button(clearShelves, click -> {

            dialog = new Dialog();
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);

            Label messageLabel = new Label();

            confirmButton = new Button("Confirm", event -> {
                deleteAllBooks();
                List<Book> books = bookService.findAll();
                if(books.isEmpty()){
                    shelvesEmpty = true;
                    messageLabel.setText("Confirmed!");
                    dialog.close();
                }
                else {
                    LOGGER.log(Level.INFO, "Did not delete all books as instructed.");
                }
            });

            cancelButton = new Button("Cancel", event -> {
                messageLabel.setText("Cancelled...");
                dialog.close();
            });

            dialog.add(confirmButton, cancelButton);
            dialog.open();
        });
    }

    public SettingsView(BookService bookService) {

        // Clear Shelves
        SettingsView.bookService = bookService;


        // Darkmode Layout
        if (darkModeOn) {
            updateDarkModeButtonText();
        }

        if(shelvesEmpty){
            updateClearShelvesButtonText();
        }

        VerticalLayout verticalLayout = new VerticalLayout(toggleDarkmode, clearShelvesButton);
        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private static void updateDarkModeButtonText() {
        if (toggleDarkmode != null) {
            toggleDarkmode.setText(darkModeOn ? disable : enable);
        }
    }

    private static void updateClearShelvesButtonText() {
        if (clearShelvesButton != null) {
            clearShelvesButton.setText(shelvesEmpty ? shelvesAlreadyEmpty : clearShelves);
        }
    }

    private static void deleteAllBooks(){
        LOGGER.log(Level.INFO, "Deleting all books...");
        bookService.deleteAll();
        LOGGER.log(Level.INFO, "Deleted all books.");
    }

    public static boolean isEmpty(){
        return bookService.count() == 0;
    }
}
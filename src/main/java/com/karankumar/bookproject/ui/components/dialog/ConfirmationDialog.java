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

package com.karankumar.bookproject.ui.components.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * A general purpose dialog to be extended for customization.
 * All dialogs that request confirmation from a user should extend this class.
 */
abstract class ConfirmationDialog extends Dialog {
    private final String descriptionText;
    protected Button confirmButton;
    protected Button cancelButton;

    ConfirmationDialog(String dialogText) {
        descriptionText = dialogText;
    }

    public void openDialog() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        final Label messageLabel = new Label();
        messageLabel.setText(descriptionText);
        add(messageLabel);

        confirmButton = new Button("Confirm", event -> {
            save();
            close();
        });

        cancelButton = new Button("Cancel", event -> {
            close();
        });

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.add(confirmButton, cancelButton);
        add(hLayout);
        open();
    }

    abstract void save();
}

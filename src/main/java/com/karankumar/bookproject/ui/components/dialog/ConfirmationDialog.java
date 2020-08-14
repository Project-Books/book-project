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



package com.karankumar.bookproject.ui.components.dialog;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Tag("confirmation-dialog")
abstract class ConfirmationDialog extends Component {

    /*
    ConfirmationDialog is a general purpose dialog to be extended for customization.
    All dialogs that request confirmation from a user should extend this class.
     */

    protected static Dialog dialog;
    private static String descriptionText;
    Button confirmButton;
    Button cancelButton;

    ConfirmationDialog(String dialogText){
        descriptionText = dialogText;
    }

    void openDialog(){
        dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        final Label messageLabel = new Label();
        messageLabel.setText(descriptionText);
        dialog.add(messageLabel);

         confirmButton = new Button("Confirm", event -> {

            save();
            dialog.close();
        });

        cancelButton = new Button("Cancel", event -> {
            messageLabel.setText("Cancelled...");
            dialog.close();
        });

        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.add(confirmButton, cancelButton);
        dialog.add(hLayout);
        dialog.open();
    }



    abstract void save();

}

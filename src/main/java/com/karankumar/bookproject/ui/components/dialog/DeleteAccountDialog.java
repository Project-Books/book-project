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

import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.service.UserService;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.java.Log;

@Log
public class DeleteAccountDialog extends ConfirmationDialog {
    private static UserService userService;
	private static User user;

    public DeleteAccountDialog(UserService userService, User user) {
        super("Are you sure you want to delete your account? There is no going back.");
        DeleteAccountDialog.userService = userService;
        DeleteAccountDialog.user = user;
    }

    @Override
    void save() {
        //VaadinSession.getCurrent().getSession().invalidate();
        //getUI().get().getPage().executeJs("window.location.href='logout.html");


        userService.deleteUser(user);
    }
}
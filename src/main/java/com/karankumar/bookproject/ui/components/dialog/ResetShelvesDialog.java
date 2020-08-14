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

import com.karankumar.bookproject.backend.service.BookService;
import java.util.logging.Level;

import com.vaadin.flow.component.Tag;
import lombok.extern.java.Log;

@Log
@Tag("reset-shelves-dialog")
public class ResetShelvesDialog extends ConfirmationDialog {


    private static BookService bookService;

    public ResetShelvesDialog(BookService bookService) {
        super(" Are you sure you want to do delete all of the books in the shelves? There is no going back. ");
        ResetShelvesDialog.bookService = bookService;
    }

    public void open(){
        openDialog();
    }

    @Override
    void save() {
        LOGGER.log(Level.INFO, "Deleting all books...");
        bookService.deleteAll();
        LOGGER.log(Level.INFO, "Deleted all books.");
    }

    boolean isEmpty(){
        return bookService.count() == 0;
    }

}


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

package com.karankumar.bookproject.ui.shelf.listener;

import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.ui.shelf.CustomShelfForm;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class CustomShelfListener {
    private final CustomShelfService customShelfService;

    public CustomShelfListener(CustomShelfService customShelfService) {
        this.customShelfService = customShelfService;
    }

    public void bind(CustomShelfForm customShelfForm) {
        customShelfForm.addListener(CustomShelfForm.SaveEvent.class, this::saveCustomShelf);
    }

    private void saveCustomShelf(CustomShelfForm.SaveEvent event) {
        if (event.getCustomShelf() != null) {
            customShelfService.save(event.getCustomShelf());
            LOGGER.log(Level.INFO, "Custom shelf saved");
        } else {
            LOGGER.log(Level.SEVERE, "Custom shelf value is null");
        }
    }
}

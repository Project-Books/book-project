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

package com.karankumar.bookproject.ui.shelf.component;

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.utils.ShelfUtils;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.extern.java.Log;

import javax.transaction.NotSupportedException;
import java.util.logging.Level;

@Log
public class BookShelfComboBox {
    private final ComboBox<String> allShelvesList;
    private final CustomShelfService customShelfService;

    public BookShelfComboBox(CustomShelfService customShelfService) {
        this.allShelvesList = new ComboBox<>();
        this.customShelfService = customShelfService;

        allShelvesList.setPlaceholder("Select shelf");
        updateShelfList();
        allShelvesList.setRequired(true);
    }

    public void updateShelfList() {
        allShelvesList.setItems(ShelfUtils.findAllShelfNames(customShelfService.findAll()));
    }

    public void bind(BooksInShelfView view) {
        allShelvesList.addValueChangeListener(event -> {
            String chosenShelf = event.getValue();
            if (chosenShelf == null) {
                LOGGER.log(Level.FINE, "No choice selected");
                return;
            }

            view.setChosenShelf(chosenShelf);
            view.updateGrid();

            try {
                view.showOrHideGridColumns(chosenShelf);
            } catch (NotSupportedException e) {
                e.printStackTrace();
            }
        });
    }

    public void addToLayout(HorizontalLayout layout) {
        layout.add(allShelvesList);
    }

    @VisibleForTesting
    public ComboBox<String> getAllShelvesList() {
        return allShelvesList;
    }
}

/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ShelfList extends VerticalLayout {

    private Grid<Shelf> grid = new Grid<>(Shelf.class);
    private ShelfService shelfService;

    public ShelfList(ShelfService shelfService) {
        this.shelfService = shelfService;
        addClassName("shelf-list");
        configureGrid();
        add(grid);

        grid.setItems(shelfService.findAll());
    }

    private void configureGrid() {
        grid.setColumns("name");
    }

}

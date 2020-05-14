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

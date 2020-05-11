package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


/**
 * @author karan on 09/05/2020
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView(ShelfService shelfService) {
        add(new BookForm(shelfService));
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}

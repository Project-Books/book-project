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
//        VerticalLayout bookForm = new BookForm(shelfService);

        // centre the book form
//        bookForm.setAlignItems(Alignment.CENTER);
//        bookForm.setJustifyContentMode(JustifyContentMode.CENTER);

//        add(bookForm);
//        add(new BookForm(shelfService));
//        add(new ShelfList(shelfService));

        add(new BookForm(shelfService));
    }
}

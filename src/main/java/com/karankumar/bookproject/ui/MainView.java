package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


/**
 * @author karan on 09/05/2020
 */
@Route("")
@PageTitle("Home | Book Project")
public class MainView extends VerticalLayout {

    public MainView(ShelfService shelfService) {
//        add(new BookForm(shelfService));

//        add(new ShelfList(shelfService));

        add(new BooksInShelf(shelfService));
    }
}

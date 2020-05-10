package com.karankumar.bookproject.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


/**
 * @author karan on 09/05/2020
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new BookForm());
    }
}

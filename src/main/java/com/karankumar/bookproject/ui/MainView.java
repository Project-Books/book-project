package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route("")
@PageTitle("Home | Book Project")
public class MainView extends VerticalLayout {

    public MainView(BookService bookService, ShelfService shelfService) {
        FormLayout bookForm = new BookForm(shelfService);
        VerticalLayout booksInShelf = new BooksInShelf(bookService, shelfService);
        add(booksInShelf, bookForm);
    }
}

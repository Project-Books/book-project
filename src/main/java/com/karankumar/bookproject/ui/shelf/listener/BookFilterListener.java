package com.karankumar.bookproject.ui.shelf.listener;

import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;

public class BookFilterListener {
    private final BooksInShelfView view;

    public BookFilterListener(BooksInShelfView view) {
        this.view = view;
    }

    private HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> authorFilterListener() {
        return eventFilterAuthorName -> {
            if (eventFilterAuthorName.getValue() != null) {
                view.setBookFilterAuthor(eventFilterAuthorName.getValue());
            }
            view.updateGrid();
        };
    }

    private HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> titleFilterListener() {
        return event -> {
            if (event.getValue() != null) {
                view.setBookFilterTitle(event.getValue());
            }
            view.updateGrid();
        };
    }

    public void bind(TextField filterByTitle, TextField filterByAuthor) {
        filterByTitle.addValueChangeListener(titleFilterListener());
        filterByAuthor.addValueChangeListener(authorFilterListener());
    }
}
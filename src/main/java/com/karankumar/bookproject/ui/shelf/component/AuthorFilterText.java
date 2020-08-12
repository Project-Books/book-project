package com.karankumar.bookproject.ui.shelf.component;

import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class AuthorFilterText {
    private final TextField text;

    public AuthorFilterText() {
        text = new TextField();

        text.setPlaceholder("Filter by Author Name");
        text.setClearButtonVisible(true);
        text.setValueChangeMode(ValueChangeMode.LAZY);
    }

    public void bind(BooksInShelfView view) {
        text.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                view.setBookFilterAuthor(event.getValue());
            }
            view.updateGrid();
        });
    }

    public void addToLayout(HorizontalLayout layout) {
        layout.add(text);
    }
}

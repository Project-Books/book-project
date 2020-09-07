package com.karankumar.bookproject.ui.book.components;

import com.karankumar.bookproject.backend.entity.Genre;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;

public class BookGenreComboBox {

    private final ComboBox<Genre> component;

    public BookGenreComboBox() {
        this.component = new ComboBox<>();
    }

    public void configure() {
        component.setItems(Genre.values());
        component.setPlaceholder("Choose a book genre");
    }

    public void add(FormLayout layout) {
        layout.addFormItem(component, "Book genre");
    }

    public Genre getValue() {
        return component.getValue();
    }

    public ComboBox<Genre> getComponent() {
        return component;
    }
}

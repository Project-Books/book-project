package com.karankumar.bookproject.ui.book.components;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

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

    public void setValue(Genre genre) {
        component.setValue(genre);
    }

    public boolean isEmpty() {
        return component.isEmpty();
    }

    public void bind(Binder<Book> binder) {
        binder.forField(component)
              .bind(Book::getGenre, Book::setGenre);
    }
}

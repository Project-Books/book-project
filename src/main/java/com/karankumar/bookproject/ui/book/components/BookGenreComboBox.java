package com.karankumar.bookproject.ui.book.components;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.ui.book.components.form.item.FormItem;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;

public class BookGenreComboBox extends FormItem<ComboBox<Genre>> {

    public BookGenreComboBox() {
        super(new ComboBox<>());
    }

    @Override
    public void configure() {
        ComboBox<Genre> component = getField();
        component.setItems(Genre.values());
        component.setPlaceholder("Choose a book genre");
    }

    @Override
    protected String getLabel(){
        return "Book genre";
    }

    @Override
    public void bind(Binder<Book> binder, ComboBox<Genre> fieldToCompare) {
        binder.forField(super.getField())
                .bind(Book::getGenre, Book::setGenre);
    }

    public Genre getValue() {
        return super.getField().getValue();
    }

    public void setValue(Genre genre) {
        super.getField().setValue(genre);
    }

    public boolean isEmpty() {
        return super.getField().isEmpty();
    }
}

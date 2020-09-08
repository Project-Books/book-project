package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;

import java.util.Objects;

public class PredefinedShelves extends FormItem<ComboBox<PredefinedShelf.ShelfName>> {

    public PredefinedShelves() {
        super(new ComboBox<>());
    }

    @Override
    public void configure() {
        ComboBox field = super.getField();

        field.setRequired(true);
        field.setPlaceholder("Choose a shelf");
        field.setClearButtonVisible(true);
        field.setItems(PredefinedShelf.ShelfName.values());
    }

    @Override
    protected String getLabel() {
        return "Book shelf *";
    }

    @Override
    public void bind(Binder<Book> binder, ComboBox<PredefinedShelf.ShelfName> fieldToCompare) {
        binder.forField(super.getField())
                .withValidator(Objects::nonNull, BookFormErrors.SHELF_ERROR)
                .bind("predefinedShelf.predefinedShelfName");
    }

    public PredefinedShelf.ShelfName getValue() {
        return super.getField().getValue();
    }
}

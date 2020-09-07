package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

public class AuthorLastName extends FormItem<TextField> {
    public AuthorLastName() {
        super(new TextField());
    }

    @Override
    public void configure() {
        TextField field = getField();

        field.setPlaceholder("Enter the author's last name");
        field.setClearButtonVisible(true);
        field.setRequired(true);
        field.setRequiredIndicatorVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Author's last name *";
    }

    @Override
    public void bind(Binder<Book> binder, TextField fieldToCompare) {
        binder.forField(super.getField())
              .withValidator(authorPredicate(), BookFormErrors.LAST_NAME_ERROR)
              .bind("author.lastName");
    }

    private SerializablePredicate<String> authorPredicate() {
        return name -> (name != null && !name.isEmpty());
    }
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.karankumar.bookproject.ui.book.BookFormValidators;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

public class AuthorFirstName extends FormItem<TextField> {
    public AuthorFirstName() {
        super(new TextField());
    }

    @Override
    public void configure() {
        TextField field = super.getField();

        field.setPlaceholder("Enter the author's first name");
        field.setClearButtonVisible(true);
        field.setRequired(true);
        field.setRequiredIndicatorVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Author's first name *";
    }

    @Override
    public void bind(Binder<Book> binder, TextField fieldToCompare) {
        binder.forField(super.getField())
                .withValidator(authorPredicate(), BookFormErrors.FIRST_NAME_ERROR)
                .bind("author.firstName");
    }

    private SerializablePredicate<String> authorPredicate() {
        //TODO: StringUtils.isNotEmpty() kullanılmalı
        return name -> (name != null && !name.isEmpty());
    }
}

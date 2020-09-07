package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class BookTitle extends FormItem<TextField> {
    public BookTitle() {
        super(new TextField());
    }

    @Override
    public void configure() {
        TextField field = super.getField();

        field.setPlaceholder("Enter a book title");
        field.setClearButtonVisible(true);
        field.setRequired(true);
        field.setRequiredIndicatorVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Book title *";
    }

    @Override
    public void bind(Binder<Book> binder, TextField fieldToCompare) {
        binder.forField(super.getField())
              .asRequired(BookFormErrors.BOOK_TITLE_ERROR)
              .bind(Book::getTitle, Book::setTitle);
    }
}

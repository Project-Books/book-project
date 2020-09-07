package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.textfield.TextArea;

public class BookReview extends FormItem<TextArea> {
    public BookReview() {
        super(new TextArea());
    }

    @Override
    public void configure() {
        TextArea field = super.getField();

        field.setPlaceholder("Enter your review for the book");
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Book review";
    }
}

package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.components.form.item.visible.factory.BookReviewStrategyFactory;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;

public class BookReview extends VisibleFormItem<TextArea> {
    public BookReview(BookReviewStrategyFactory factory) {
        super(new TextArea(), factory);
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

    @Override
    public void bind(Binder<Book> binder, TextArea fieldToCompare) {
        binder.forField(super.getField())
              .bind(Book::getBookReview, Book::setBookReview);
    }
}

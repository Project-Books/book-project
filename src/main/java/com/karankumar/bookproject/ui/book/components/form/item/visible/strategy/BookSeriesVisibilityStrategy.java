package com.karankumar.bookproject.ui.book.components.form.item.visible.strategy;

import com.karankumar.bookproject.backend.entity.Book;
import com.vaadin.flow.data.binder.Binder;

public class BookSeriesVisibilityStrategy extends FieldExistenceStrategy<Binder<Book>> {
    public BookSeriesVisibilityStrategy(Binder<Book> field) {
        super(field);
    }

    @Override
    public boolean isFieldExist(Binder<Book> binder) {
        return binder.getBean() != null && binder.getBean().getSeriesPosition() != null;
    }
}

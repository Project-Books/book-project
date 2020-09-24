package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.components.form.item.visible.factory.PagesReadStrategyFactory;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;

public class PagesRead extends VisibleFormItem<IntegerField> {

    public PagesRead(PagesReadStrategyFactory factory) {
        super(new IntegerField(), factory);
    }

    @Override
    public void configure() {
        IntegerField field = super.getField();

        field.setPlaceholder("Enter number of pages read");
        field.setMin(1);
        field.setHasControls(true);
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Pages read";
    }

    @Override
    public void bind(Binder<Book> binder, IntegerField fieldToCompare) {
        binder.forField(super.getField())
              .bind(Book::getPagesRead, Book::setPagesRead);
    }
}

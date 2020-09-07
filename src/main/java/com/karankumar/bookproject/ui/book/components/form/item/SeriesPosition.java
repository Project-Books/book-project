package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

public class SeriesPosition extends FormItem<IntegerField> {

    public SeriesPosition() {
        super(new IntegerField());
    }

    @Override
    public void configure() {
        IntegerField field = super.getField();

        field.setPlaceholder("Enter series position");
        field.setMin(1);
        field.setHasControls(true);
    }

    @Override
    protected String getLabel() {
        return "Series number";
    }

    @Override
    public void bind(Binder<Book> binder, IntegerField fieldToCompare) {
        binder.forField(super.getField())
              .withValidator(isNumberPositive(), BookFormErrors.SERIES_POSITION_ERROR)
              .bind(Book::getSeriesPosition, Book::setSeriesPosition);
    }

    private SerializablePredicate<Integer> isNumberPositive() {
        return number -> (number == null || number > 0);
    }
}

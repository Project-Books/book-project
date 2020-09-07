package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

public class Rating extends VisibleFormItem<NumberField> {
    public Rating() {
        super(new NumberField());
    }

    @Override
    public void configure() {
        NumberField field = super.getField();

        field.setHasControls(true);
        field.setPlaceholder("Enter a rating");
        field.setMin(0);
        field.setMax(10);
        field.setStep(0.5f);
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Book rating";
    }

    @Override
    public void bind(Binder<Book> binder, NumberField fieldToCompare) {
        binder.forField(super.getField())
              .withConverter(new DoubleToRatingScaleConverter())
              .bind(Book::getRating, Book::setRating);
    }
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.textfield.NumberField;

public class Rating extends FormItem<NumberField> {
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
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.textfield.IntegerField;

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
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.textfield.IntegerField;

public class PagesRead extends FormItem<IntegerField> {

    public PagesRead() {
        super(new IntegerField());
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
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.textfield.IntegerField;

public class PageCount extends FormItem<IntegerField> {

    public PageCount(){
        super(new IntegerField());
    }

    @Override
    public void configure() {
        IntegerField field = super.getField();

        field.setPlaceholder("Enter number of pages");
        field.setMin(1);
        field.setHasControls(true);
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Number of pages";
    }
}

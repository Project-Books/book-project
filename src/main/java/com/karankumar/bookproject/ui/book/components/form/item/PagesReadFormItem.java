package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;

public class PagesReadFormItem extends FormItem<IntegerField>{

    public PagesReadFormItem() {
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
    public void add(FormLayout formLayout) {
        super.add(formLayout, "Pages read");
    }
}

package com.karankumar.bookproject.ui.book.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;

public class PagesReadFormItem {
    private final IntegerField field;
    private FormLayout.FormItem item;

    public PagesReadFormItem() {
        field = new IntegerField();
    }

    public IntegerField getField() {
        return field;
    }

    public void configure() {
        field.setPlaceholder("Enter number of pages read");
        field.setMin(1);
        field.setHasControls(true);
        field.setClearButtonVisible(true);
    }

    public void add(FormLayout formLayout) {
        item = formLayout.addFormItem(field, "Pages read");
    }

    public void makeVisible() {
        item.setVisible(true);
    }

    public void makeInvisible() {
        item.setVisible(false);
    }

    public boolean isVisible() {
        return item.isVisible();
    }
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;

public class ReadingStartDateFormItem extends FormItem<DatePicker> {

    public ReadingStartDateFormItem() {
        super(new DatePicker());
    }

    @Override
    public void configure() {
        DatePicker field = super.getField();
        field.setClearButtonVisible(true);
        field.setPlaceholder("Enter a date");
    }

    @Override
    public void add(FormLayout layout) {
        super.add(layout, "Date started");
    }
}

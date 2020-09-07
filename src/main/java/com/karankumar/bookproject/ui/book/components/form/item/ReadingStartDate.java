package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.datepicker.DatePicker;

public class ReadingStartDate extends FormItem<DatePicker> {

    public ReadingStartDate() {
        super(new DatePicker());
    }

    @Override
    public void configure() {
        DatePicker field = super.getField();

        field.setClearButtonVisible(true);
        field.setPlaceholder("Enter a date");
    }

    @Override
    protected String getLabel() {
        return "Date started";
    }
}

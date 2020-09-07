package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.datepicker.DatePicker;

public class ReadingEndDate extends FormItem<DatePicker> {

    public ReadingEndDate() {
        super(new DatePicker());
    }

    @Override
    public void configure() {
        super.getField().setClearButtonVisible(true);
        super.getField().setPlaceholder("Enter a date");
    }

    @Override
    protected String getLabel() {
        return "Date finished";
    }
}

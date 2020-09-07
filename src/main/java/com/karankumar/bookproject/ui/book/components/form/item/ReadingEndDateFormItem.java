package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.datepicker.DatePicker;

public class ReadingEndDateFormItem extends FormItem<DatePicker> {

    public ReadingEndDateFormItem() {
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

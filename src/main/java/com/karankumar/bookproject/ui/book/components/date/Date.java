/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.book.components.date;

import com.karankumar.bookproject.ui.book.components.FormItem;
import com.karankumar.bookproject.ui.book.form.BookForm;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.LocalDate;

public abstract class Date extends FormItem<DatePicker> {
    private final String label;

    public Date(String label) {
        super(new DatePicker());
        this.label = label;
    }

    @Override
    public void configure() {
        DatePicker date = super.getField();
        date.setClearButtonVisible(true);
        date.setPlaceholder(BookForm.ENTER_DATE);
    }

    @Override
    public String getLabel() {
        return label;
    }

    public LocalDate getValue() {
        return super.getField().getValue();
    }
}

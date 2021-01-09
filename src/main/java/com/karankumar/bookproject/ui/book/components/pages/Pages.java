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

package com.karankumar.bookproject.ui.book.components.pages;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.form.BookForm;
import com.karankumar.bookproject.ui.book.components.FormItem;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;

public abstract class Pages extends FormItem<IntegerField> {
    private final String placeholder;

    public Pages(String label, String placeholder) {
        super(new IntegerField(), label);
        this.placeholder = placeholder;
    }

    @Override
    public void configure() {
        IntegerField pages = super.getField();
        pages.setPlaceholder(placeholder);
        pages.setMin(1);
        pages.setMax(Book.MAX_PAGES);
        pages.setHasControls(true);
        pages.setClearButtonVisible(true);
    }

    public Integer getValue() {
        return super.getField().getValue();
    }
}

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

package com.karankumar.bookproject.ui.book;

import com.karankumar.bookproject.backend.entity.book.Book;
import com.vaadin.flow.function.SerializablePredicate;

import java.time.LocalDate;

public final class BookFormValidators {
    private BookFormValidators() {}

    static SerializablePredicate<Integer> positiveNumberPredicate() {
        return number -> (number == null || number > 0);
    }

    static SerializablePredicate<String> authorPredicate() {
        return name -> (name != null && !name.isEmpty());
    }

    static SerializablePredicate<LocalDate> datePredicate() {
        return date -> !(date != null && date.isAfter(LocalDate.now()));
    }

    static SerializablePredicate<Integer> maxPagesPredicate() {
        return number -> (number == null || number <= Book.MAX_PAGES);
    }
}

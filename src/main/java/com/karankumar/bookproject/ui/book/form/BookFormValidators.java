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

package com.karankumar.bookproject.ui.book.form;

import com.karankumar.bookproject.backend.entity.Book;
import com.vaadin.flow.function.SerializablePredicate;

import java.time.LocalDate;

import static com.karankumar.bookproject.backend.util.DateUtils.isDateInFuture;

public final class BookFormValidators {
    private BookFormValidators() {}

    public static SerializablePredicate<Integer> isNumberPositive() {
        return number -> (number == null || number > 0);
    }

    public static SerializablePredicate<String> isNameNonEmpty() {
        return name -> (name != null && !name.isEmpty());
    }

    static SerializablePredicate<LocalDate> isNotInFuture() {
        return date -> {
            // date is optional, so it is allowed to be null
            if (date != null) {
                return !isDateInFuture(date);
            }
            return true;
        };
    }

    static SerializablePredicate<Integer> isLessThanOrEqualToMaxPages() {
        return number -> (number == null || number <= Book.MAX_PAGES);
    }

    static SerializablePredicate<LocalDate> isEndDateAfterStartDate(LocalDate dateStarted) {
        return endDate -> {
            if (dateStarted == null || endDate == null) {
                // allowed since these are optional fields
                return true;
            }
            return (endDate.isEqual(dateStarted) || endDate.isAfter(dateStarted));
        };
    }
}

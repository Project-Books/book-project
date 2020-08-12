package com.karankumar.bookproject.ui.book;

import com.vaadin.flow.function.SerializablePredicate;

import java.time.LocalDate;

public class BookFormValidators {
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
}

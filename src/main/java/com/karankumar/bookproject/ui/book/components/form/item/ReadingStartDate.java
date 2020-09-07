package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.karankumar.bookproject.ui.book.BookFormValidators;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

import java.time.LocalDate;

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

    @Override
    public void bind(Binder<Book> binder, DatePicker fieldToCompare) {
        binder.forField(super.getField())
              .withValidator(datePredicate(), String.format(BookFormErrors.AFTER_TODAY_ERROR, "started"))
              .bind(Book::getDateStartedReading, Book::setDateStartedReading);
    }

    private SerializablePredicate<LocalDate> datePredicate() { //TODO: Rename it to => isStartDateBeforeNow() or i or isDateBeforeNow() or isBeforeNow()
        return date -> !(date != null && date.isAfter(LocalDate.now()));  // TODO: Can it be simplified as LocalDate.now().isBefore(date);
    }
}

package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.karankumar.bookproject.ui.book.components.form.item.visible.factory.ReadingEndDateStrategyFactory;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

import java.time.LocalDate;

public class ReadingEndDate extends VisibleFormItem<DatePicker> {

    public ReadingEndDate(ReadingEndDateStrategyFactory factory) {
        super(new DatePicker(), factory);
    }

    @Override
    public void configure() {
        DatePicker field = super.getField();

        field.setClearButtonVisible(true);
        field.setPlaceholder("Enter a date");
    }

    @Override
    protected String getLabel() {
        return "Date finished";
    }

    @Override
    //TODO: Sanki readingEndDate ve readingStartDate birlestirilebilir tek bir ReadingDate altında
    //TODO: needs better solution
    //TODO: Decorator pattern can be applied ?
    //TODO: Decorator pattern can be also applied to VisibleFormField
    public void bind(Binder<Book> binder, DatePicker readingStartDate) {
        binder.forField(super.getField())
                .withValidator(isEndDateAfterStartDate(readingStartDate), BookFormErrors.FINISH_DATE_ERROR)
                .withValidator(isBeforeNow(), String.format(BookFormErrors.AFTER_TODAY_ERROR, "finished"))
                .bind(Book::getDateFinishedReading, Book::setDateFinishedReading);
    }

    private SerializablePredicate<LocalDate> isEndDateAfterStartDate(DatePicker readingStartDate) {
        return endDate -> {
            LocalDate dateStarted = readingStartDate.getValue();

            if (dateStarted == null || endDate == null) {
                // allowed since these are optional fields
                return true;
            }
            return (endDate.isEqual(dateStarted) || endDate.isAfter(dateStarted));
        };
    }

    private SerializablePredicate<LocalDate> isBeforeNow() {
        return date -> !(date != null && date.isAfter(LocalDate.now()));
    }
}

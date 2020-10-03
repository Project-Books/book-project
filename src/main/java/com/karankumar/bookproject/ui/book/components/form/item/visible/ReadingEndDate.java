package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

import javax.transaction.NotSupportedException;
import java.time.LocalDate;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;

public class ReadingEndDate extends VisibleFormItem<DatePicker> {
    private final HideStrategy hideStrategy;
    private final ShowStrategy showStrategy;

    public ReadingEndDate(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(new DatePicker());
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
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

    @Override
    protected VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == READING || name == DID_NOT_FINISH || name == TO_READ) {
            return hideStrategy;
        }

        if (name == READ) {
            return showStrategy;
        }

        throw super.unsupportedVisibilityStrategy(name);
    }
}

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

public class ReadingStartDate extends VisibleFormItem<DatePicker> {
    private final HideStrategy hideStrategy;
    private final ShowStrategy showStrategy;

    public ReadingStartDate(HideStrategy hideStrategy, ShowStrategy showStrategy) {
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

    @Override
    protected VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == TO_READ) {
            return hideStrategy;
        }

        if (name == READING || name == DID_NOT_FINISH || name == READ) {
            return showStrategy;
        }

        throw super.unsupportedVisibilityStrategy(name);
    }
}

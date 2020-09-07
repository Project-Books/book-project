package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.BookFormErrors;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.SerializablePredicate;

public class PageCount extends VisibleFormItem<IntegerField> {

    public PageCount(){
        super(new IntegerField());
    }

    @Override
    public void configure() {
        IntegerField field = super.getField();

        field.setPlaceholder("Enter number of pages");
        field.setMin(1);
        field.setHasControls(true);
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Number of pages";
    }

    @Override
    public void bind(Binder<Book> binder, IntegerField fieldToCompare) {
        binder.forField(super.getField())
              .withValidator(isNumberPositive(), BookFormErrors.PAGE_NUMBER_ERROR)
              .bind(Book::getNumberOfPages, Book::setNumberOfPages);
    }

    private SerializablePredicate<Integer> isNumberPositive() {
        return number -> (number == null || number > 0);
    }
}

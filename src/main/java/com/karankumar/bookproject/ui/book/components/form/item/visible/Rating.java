package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.DoubleToRatingScaleConverter;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;

public class Rating extends VisibleFormItem<NumberField> {
    private final HideStrategy hideStrategy;
    private final ShowStrategy showStrategy;

    public Rating(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(new NumberField());

        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    @Override
    public void configure() {
        NumberField field = super.getField();

        field.setHasControls(true);
        field.setPlaceholder("Enter a rating");
        field.setMin(0);
        field.setMax(10);
        field.setStep(0.5f);
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Book rating";
    }

    @Override
    public void bind(Binder<Book> binder, NumberField fieldToCompare) {
        binder.forField(super.getField())
                .withConverter(new DoubleToRatingScaleConverter())
                .bind(Book::getRating, Book::setRating);
    }

    @Override
    protected VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == TO_READ || name == READING || name == DID_NOT_FINISH) {
            return hideStrategy;
        }

        if (name == READ) {
            return showStrategy;
        }

        throw super.unsupportedVisibilityStrategy(name);
    }
}

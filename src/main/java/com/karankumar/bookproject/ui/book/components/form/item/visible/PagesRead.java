package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.DID_NOT_FINISH;

public class PagesRead extends VisibleFormItem<IntegerField> {
    private final HideStrategy hideStrategy;
    private final ShowStrategy showStrategy;

    public PagesRead(ShowStrategy showStrategy, HideStrategy hideStrategy) {
        super(new IntegerField());
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    @Override
    public void configure() {
        IntegerField field = super.getField();

        field.setPlaceholder("Enter number of pages read");
        field.setMin(1);
        field.setHasControls(true);
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Pages read";
    }

    @Override
    public void bind(Binder<Book> binder, IntegerField fieldToCompare) {
        binder.forField(super.getField())
              .bind(Book::getPagesRead, Book::setPagesRead);
    }

    @Override
    protected VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == TO_READ || name == READING || name == READ) {
            return hideStrategy;
        }

        if (name == DID_NOT_FINISH) {
            return showStrategy;
        }

        throw super.unsupportedVisibilityStrategy(name);
    }
}

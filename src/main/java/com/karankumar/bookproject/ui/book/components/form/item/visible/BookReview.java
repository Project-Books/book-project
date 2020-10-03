package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;

public class BookReview extends VisibleFormItem<TextArea> {
    private final HideStrategy hideStrategy;
    private final ShowStrategy showStrategy;

    public BookReview(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(new TextArea());
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    @Override
    public void configure() {
        TextArea field = super.getField();

        field.setPlaceholder("Enter your review for the book");
        field.setClearButtonVisible(true);
    }

    @Override
    protected String getLabel() {
        return "Book review";
    }

    @Override
    public void bind(Binder<Book> binder, TextArea fieldToCompare) {
        binder.forField(super.getField())
              .bind(Book::getBookReview, Book::setBookReview);
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

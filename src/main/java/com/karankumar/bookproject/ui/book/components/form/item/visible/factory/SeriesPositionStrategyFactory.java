package com.karankumar.bookproject.ui.book.components.form.item.visible.factory;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.SeriesPosition;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.BookSeriesVisibilityStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.FieldExistenceStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;
import com.vaadin.flow.data.binder.Binder;

import javax.transaction.NotSupportedException;

public class SeriesPositionStrategyFactory extends VisibleFormItemStrategyFactory {
    private final FieldExistenceStrategy<Binder<Book>> fieldExistenceStrategy;

    public SeriesPositionStrategyFactory(BookSeriesVisibilityStrategy bookSeriesVisibilityStrategy) {
        super(SeriesPosition.class);
        this.fieldExistenceStrategy = bookSeriesVisibilityStrategy;
    }

    @Override
    public VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        return fieldExistenceStrategy;
    }
}

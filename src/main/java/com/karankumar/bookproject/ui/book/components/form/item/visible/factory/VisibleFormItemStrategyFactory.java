package com.karankumar.bookproject.ui.book.components.form.item.visible.factory;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.VisibleFormItem;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;

import javax.transaction.NotSupportedException;

public abstract class VisibleFormItemStrategyFactory {
    protected final Class<? extends VisibleFormItem> type;

    public VisibleFormItemStrategyFactory(Class<? extends VisibleFormItem> type) {
        this.type = type;
    }

    public abstract VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException;

    protected NotSupportedException exception(PredefinedShelf.ShelfName name) {
        return new NotSupportedException("Shelf " + name + " not yet supported");
    }
}

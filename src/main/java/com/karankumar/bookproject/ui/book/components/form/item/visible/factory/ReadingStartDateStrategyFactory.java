package com.karankumar.bookproject.ui.book.components.form.item.visible.factory;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.ReadingStartDate;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

public class ReadingStartDateStrategyFactory extends VisibleFormItemStrategyFactory {
    private final VisibilityStrategy hideStrategy;
    private final VisibilityStrategy showStrategy;

    public ReadingStartDateStrategyFactory(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(ReadingStartDate.class);
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    @Override
    public VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == TO_READ) {
            return hideStrategy;
        }

        if (name == READING || name == DID_NOT_FINISH || name == READ) {
            return showStrategy;
        }

        throw super.exception(name);
    }
}

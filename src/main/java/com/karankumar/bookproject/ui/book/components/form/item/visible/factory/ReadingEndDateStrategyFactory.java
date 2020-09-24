package com.karankumar.bookproject.ui.book.components.form.item.visible.factory;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.ReadingEndDate;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

public class ReadingEndDateStrategyFactory extends VisibleFormItemStrategyFactory {
    private final VisibilityStrategy hideStrategy;
    private final VisibilityStrategy showStrategy;

    public ReadingEndDateStrategyFactory(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(ReadingEndDate.class);
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    @Override
    public VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == READING || name == DID_NOT_FINISH || name == TO_READ) {
            return hideStrategy;
        }

        if (name == READ) {
            return showStrategy;
        }

        throw super.exception(name);
    }
}

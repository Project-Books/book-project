package com.karankumar.bookproject.ui.book.components.form.item.visible.factory;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.PagesRead;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

public class PagesReadStrategyFactory extends VisibleFormItemStrategyFactory {

    private final VisibilityStrategy hideStrategy;
    private final VisibilityStrategy showStrategy;

    public PagesReadStrategyFactory(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(PagesRead.class);
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    public VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == TO_READ || name == READING || name == READ) {
            return hideStrategy;
        }
        
        if (name == DID_NOT_FINISH) {
            return showStrategy;
        }

        throw super.exception(name);
    }
}

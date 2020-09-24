package com.karankumar.bookproject.ui.book.components.form.item.visible.factory;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.visible.Rating;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.HideStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.ShowStrategy;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;

import javax.transaction.NotSupportedException;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.*;

public class RatingStrategyFactory extends VisibleFormItemStrategyFactory {
    private final VisibilityStrategy hideStrategy;
    private final VisibilityStrategy showStrategy;

    //TODO: BookReviewStrategyFactor + RatingStrategyFactor ikisi de aynı işi yapmak gerekiyor.
    //TODO: Class degiskeniniss disaridan almak gerekebilir belki
    //Todo: class degiskenine gerek de kalmayabilir ...
    //TODO: Factory yerine decorator mı kullansaydık ? Yani TO_READ READING vs gibi bunlar ise bunlar da dön mü deseydik acaba ?
    //TODO: en kolayı sanırsam bu haliyle bırakmak 
    public RatingStrategyFactory(HideStrategy hideStrategy, ShowStrategy showStrategy) {
        super(Rating.class);
        this.hideStrategy = hideStrategy;
        this.showStrategy = showStrategy;
    }

    @Override
    public VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName name) throws NotSupportedException {
        if (name == TO_READ || name == READING || name == DID_NOT_FINISH) {
            return hideStrategy;
        }

        if (name == READ) {
            return showStrategy;
        }

        throw super.exception(name);
    }
}

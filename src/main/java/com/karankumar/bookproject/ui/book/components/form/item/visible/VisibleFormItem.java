package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.FormItem;
import com.karankumar.bookproject.ui.book.components.form.item.visible.factory.VisibleFormItemStrategyFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

import javax.transaction.NotSupportedException;

public abstract class VisibleFormItem<T extends Component> extends FormItem<T> {
    private FormLayout.FormItem item;
    private final VisibleFormItemStrategyFactory strategyFactory;

    protected VisibleFormItem(T field, VisibleFormItemStrategyFactory strategyFactory) {
        super(field);
        this.strategyFactory = strategyFactory;
    }

    @Override
    public void add(FormLayout layout) { //TODO: bunları da aslında en temizi constructordan alıp halletmek gerekiyor ama hayırlısı
        item = layout.addFormItem(super.getField(), getLabel());
    }

    public void display(PredefinedShelf.ShelfName shelfName) throws NotSupportedException {
        strategyFactory.getVisibilityStrategy(shelfName).display(item);
    }

    public void show() {
        item.setVisible(true);
    }

    public void hide() {    //TODO: Muhtemelen hide() ve show() methodlarının uçurulması gerekiyor aslında
        item.setVisible(false);
    }

    public boolean isVisible() {
        return item.isVisible();
    }

}

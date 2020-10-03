package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.ui.book.components.form.item.FormItem;
import com.karankumar.bookproject.ui.book.components.form.item.visible.strategy.VisibilityStrategy;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

import javax.transaction.NotSupportedException;

public abstract class VisibleFormItem<T extends Component> extends FormItem<T> {
    protected FormLayout.FormItem item;

    protected VisibleFormItem(T field) {
        super(field);
    }

    @Override
    public void add(FormLayout layout) { //TODO: bunları da aslında en temizi constructordan alıp halletmek gerekiyor ama hayırlısı
        item = layout.addFormItem(super.getField(), getLabel());
    }

    public void display(PredefinedShelf.ShelfName shelfName) throws NotSupportedException {
        getVisibilityStrategy(shelfName).display(item);
    }

    protected abstract VisibilityStrategy getVisibilityStrategy(PredefinedShelf.ShelfName shelfName) throws NotSupportedException;

    protected NotSupportedException unsupportedVisibilityStrategy(PredefinedShelf.ShelfName name) {
        return new NotSupportedException("Shelf " + name + " not yet supported");
    }

    public boolean isVisible() {
        return item.isVisible();
    }
}

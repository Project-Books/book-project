package com.karankumar.bookproject.ui.book.components.form.item.visible;

import com.karankumar.bookproject.ui.book.components.form.item.FormItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

public abstract class VisibleFormItem<T extends Component> extends FormItem<T> {
    private FormLayout.FormItem item;

    public VisibleFormItem(T field) {
        super(field);
    }

    @Override
    public void add(FormLayout layout) {
        item = layout.addFormItem(super.getField(), getLabel());
    }

    public void show() {
        item.setVisible(true);
    }

    public void hide() {
        item.setVisible(false);
    }

    public boolean isVisible() {
        return item.isVisible();
    }

}

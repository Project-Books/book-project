package com.karankumar.bookproject.ui.book.components.form.item;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;

public abstract class FormItem<T extends Component> {
    private final T field;
    private FormLayout.FormItem item;

    protected FormItem(T field) {
        this.field = field;
    }

    public abstract void configure();

    public void add(FormLayout layout) {
        this.item = layout.addFormItem(field, getLabel());
    }

    protected abstract String getLabel();

    public T getField() {
        return field;
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

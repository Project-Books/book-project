package com.karankumar.bookproject.ui.book.components.form.item.visible.strategy;

import com.vaadin.flow.component.formlayout.FormLayout;

public abstract class FieldExistenceStrategy<T> implements VisibilityStrategy {
    private final T field;

    public FieldExistenceStrategy(T field) {
        this.field = field;
    }

    @Override
    public void display(FormLayout.FormItem formItem) {
        if (isFieldExist(field)) {
            show(formItem);
        } else {
            hide(formItem);
        }
    }

    protected abstract boolean isFieldExist(T field);

    private void hide(FormLayout.FormItem formItem) {
        formItem.setVisible(false);
    }

    private void show(FormLayout.FormItem formItem) {
        formItem.setVisible(true);
    }
}

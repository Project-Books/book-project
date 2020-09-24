package com.karankumar.bookproject.ui.book.components.form.item.visible.strategy;

import com.vaadin.flow.component.formlayout.FormLayout;

public class HideStrategy implements VisibilityStrategy {
    @Override
    public void display(FormLayout.FormItem formItem) {
        formItem.setVisible(false);
    }
}

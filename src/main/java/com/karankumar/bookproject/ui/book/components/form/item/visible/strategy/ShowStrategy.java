package com.karankumar.bookproject.ui.book.components.form.item.visible.strategy;

import com.vaadin.flow.component.formlayout.FormLayout;

public class ShowStrategy implements VisibilityStrategy {
    @Override
    public void display(FormLayout.FormItem formItem) {
        formItem.setVisible(true);
    }
}

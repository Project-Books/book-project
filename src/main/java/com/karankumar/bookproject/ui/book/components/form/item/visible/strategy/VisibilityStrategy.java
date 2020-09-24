package com.karankumar.bookproject.ui.book.components.form.item.visible.strategy;

import com.karankumar.bookproject.ui.book.components.form.item.visible.VisibleFormItem;
import com.vaadin.flow.component.formlayout.FormLayout;

public interface VisibilityStrategy {
    public void display(FormLayout.FormItem formItem);
}

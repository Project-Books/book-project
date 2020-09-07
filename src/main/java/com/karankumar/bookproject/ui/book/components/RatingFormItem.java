package com.karankumar.bookproject.ui.book.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;

public class RatingFormItem {
    private final NumberField rating;
    private FormLayout.FormItem item;

    public RatingFormItem() {
        this.rating = new NumberField();
    }

    public void configure() {
        rating.setHasControls(true);
        rating.setPlaceholder("Enter a rating");
        rating.setMin(0);
        rating.setMax(10);
        rating.setStep(0.5f);
        rating.setClearButtonVisible(true);
    }

    public void add(FormLayout layout) {
        this.item = layout.addFormItem(rating, "Book rating");
    }

    public NumberField getRating() {
        return rating;
    }

    public void makeVisible() {
        item.setVisible(true);
    }

    public void makeInvisible() {
        item.setVisible(false);
    }

    public boolean isVisible() {
        return item.isVisible();
    }
}

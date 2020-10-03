package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.utils.CustomShelfUtils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public class CustomShelfFormItem extends FormItem<ComboBox<String>> {
    private final CustomShelfService customShelfService;

    public CustomShelfFormItem(CustomShelfService customShelfService) {
        super(new ComboBox<>());
        this.customShelfService = customShelfService;
    }

    @Override
    public void add(FormLayout formLayout) {
        formLayout.addFormItem(super.getField(), getLabel());
    }

    @Override
    public void configure() {
        ComboBox<String> customShelfField = super.getField();
        customShelfField.setPlaceholder("Choose a shelf");
        customShelfField.setClearButtonVisible(true);

        CustomShelfUtils customShelfUtils = new CustomShelfUtils(customShelfService);
        customShelfField.setItems(customShelfUtils.getCustomShelfNames());
    }

    @Override
    protected String getLabel() {
        return "Secondary shelf";
    }

    @Override
    public void bind(Binder<Book> binder, ComboBox<String> fieldToCompare) {
        binder.forField(super.getField())
              .bind("customShelf.shelfName");
    }

    public List<CustomShelf> getAllShelves() {
        return customShelfService.findAll(this.getValue());
    }

    public String getValue() {
        return super.getField().getValue();
    }
}

package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;

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

    //TODO: Bundan daha iyi bir çözüm sunmak gerekebilir
    //TODO: Since only one subclass will pass field different than null
    //TODO: it should override this method. It's better to pass null in bind(binder) of every sub class
    public void bind(Binder<Book> binder) {
        bind(binder, null);
    }

    public abstract void bind(Binder<Book> binder, T fieldToCompare);
}

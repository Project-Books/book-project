package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.ui.book.components.form.item.visible.SeriesPosition;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.binder.Binder;

public class InSeries extends FormItem<Checkbox> {
    public InSeries() {
        super(new Checkbox());
    }

    @Override
    public void configure() {
        getField().setValue(false);
    }

    // TODO: sanki subclass yavastan ise yaramiyor gibi ekstra ozellikler eklemem gerekiyor.
    // TODO: ya da kullanmadıgım ozellikler oluyor ...
    // TODO: Favor compositon over inheritance
    // TODO: Decorator Pattern
    // TODO: Strategy pattern uygulayabiliriz diyecegim ama code-reuse yok gibi bir sey sanki pek dogru olmaz
    public void configure(SeriesPosition seriesPosition) {
        configure();

        addValueChangeListener(event -> {
            if (Boolean.TRUE.equals(event.getValue())) {
                seriesPosition.show();
            } else {
                seriesPosition.hide();
                seriesPosition.getField().clear();
            }
        });
    }

    private void addValueChangeListener(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>> listener) {
        getField().addValueChangeListener(listener);
    }

    @Override
    protected String getLabel() {
        return "Is in series?";
    }

    @Override
    public void bind(Binder<Book> binder, Checkbox fieldToCompare) {
        // Not implemented since it's not used ...
        // TODO: bu kötü bir şey kullanmayacağı bir şeyi implemente ettiriyorum şuanda ...
        // TODO: buraları ayrı bir interface'e çıkmak mı yoksa bind ettirmek mi ?
    }

    public void setValue(Boolean value) {
        getField().setValue(value);
    }
}

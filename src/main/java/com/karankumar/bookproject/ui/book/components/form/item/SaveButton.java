package com.karankumar.bookproject.ui.book.components.form.item;

import com.karankumar.bookproject.backend.entity.Book;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.data.binder.Binder;

public class SaveButton extends FormItem<Button> {

    public SaveButton() {
        super(new Button());
    }

    @Override
    public void configure() {
        Button field = super.getField();

        field.setText("Add book");
        field.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        field.setDisableOnClick(true);
    }

    @Override
    protected String getLabel() {
        return null;
    }

    @Override
    public void bind(Binder<Book> binder, Button fieldToCompare) {
        binder.addStatusChangeListener(event -> super.getField().setEnabled(binder.isValid()));
    }

    //TODO: Listenerlar bayagi bir artıyor bunu belki get ettirterek yapabiliriz ya da decorator ile listener eklemesini saglayabiliriz ...
    public void addListener(ComponentEventListener<ClickEvent<Button>> listener) {
        getField().addClickListener(listener);
    }

    public void resetText() {
        super.getField().setText("Add book");
    }

    public void setAddText() {
        super.getField().setText("Add book");
    }

    public void setUpdateText() {
        super.getField().setText("Update book");
    }

    //TODO: Parentta yer alıp burada yer almayan methodlar var. Daha dogrusu hic kullanılmayan cagirilmayan oralara belki bir seyler yapılabilir
    //TODO: Bir de sadece bunun icin gecerli olan eklenen bazı methodlar var. Belki de yavastan decorator pattern dusunebiliriz ne dersin ?

}

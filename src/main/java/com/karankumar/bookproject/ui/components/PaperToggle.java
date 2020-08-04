package com.karankumar.bookproject.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;


/**
 * A web component for a toggle switch
 */
@Tag("paper-toggle-button")
@NpmPackage(value="@polymer/paper-toggle-button", version = "3.0.1")
@JsModule("@polymer/paper-toggle-button/paper-toggle-button.js")
public class PaperToggle extends Component {

    public Registration addClickListener(ComponentEventListener<PaperToggleClickEvent> listener) {
        return addListener(PaperToggleClickEvent.class, listener);
    }

    public void setChecked(boolean checked){
        getElement().setProperty("checked", checked);
    }
}




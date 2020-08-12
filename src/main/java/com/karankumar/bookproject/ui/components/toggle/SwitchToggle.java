package com.karankumar.bookproject.ui.components.toggle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

@Tag("paper-toggle-button")
@NpmPackage(value="@polymer/paper-toggle-button", version = "3.0.1")
@JsModule("@polymer/paper-toggle-button/paper-toggle-button.js")
public class SwitchToggle extends Component {
    public Registration addClickListener(ComponentEventListener<SwitchToggleClickEvent> listener) {
        return addListener(SwitchToggleClickEvent.class, listener);
    }

    public void setChecked(boolean checked){
        getElement().setProperty("checked", checked);
    }
}

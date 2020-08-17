/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

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
    public Registration addClickListener(ComponentEventListener<SwitchToggleEvent> listener) {
        return addListener(SwitchToggleEvent.class, listener);
    }

    public void setChecked(boolean checked){
        getElement().setProperty("checked", checked);
    }
}

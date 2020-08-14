package com.karankumar.bookproject.ui.components.utils;

import com.vaadin.flow.component.HasSize;

public class ComponentUtil {
    private ComponentUtil() {}

    public static void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("23em");
        }
    }
}

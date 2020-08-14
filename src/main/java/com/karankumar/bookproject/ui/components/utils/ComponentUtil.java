package com.karankumar.bookproject.ui.components.utils;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;

public class ComponentUtil {
    private ComponentUtil() {}

    public static void setComponentMinWidth(HasSize[] components) {
        for (HasSize h : components) {
            h.setMinWidth("23em");
        }
    }

    public static void clearComponentFields(HasValue... components) {
        for (HasValue component : components) {
            if (component != null && !component.isEmpty()) {
                component.clear();
            }
        }
    }
}

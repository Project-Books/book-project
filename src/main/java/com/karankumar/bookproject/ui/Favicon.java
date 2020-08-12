package com.karankumar.bookproject.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;

import java.util.HashMap;

@Route("favicon")
public class Favicon extends Div implements RouterLayout, PageConfigurator {

    @Override
    public void configurePage(InitialPageSettings settings) {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("rel", "shortcut icon");
        attributes.put("type", "image/png");
        settings.addLink("icons/icon.png", attributes);
    }
}

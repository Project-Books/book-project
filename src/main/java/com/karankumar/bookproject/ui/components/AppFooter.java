package com.karankumar.bookproject.ui.components;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class AppFooter extends Footer {
    public AppFooter() {
        add(
                new Hr(),
                addDisclaimer()
        );
        setSizeFull();
    }

    private FlexLayout addDisclaimer() {
        FlexLayout layout = new FlexLayout(
                new Text("Please note that this is for demonstration purposes only. " +
                        "Your data may not persist, so we recommend exporting regularly."),
                new HtmlComponent("br"),
                new Text("For the best experience, please view this website on a desktop or a " +
                        "laptop. This web app is not yet optimised for mobile.")
        );
        layout.setSizeFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return layout;
    }
}

package com.karankumar.bookproject.ui.components;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AppFooter extends Footer {
    public AppFooter() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addAndExpand(addDisclaimer());
        add(verticalLayout);
        setSizeFull();
    }

    private FlexLayout addDisclaimer() {
        VerticalLayout v = new VerticalLayout(
                new Hr(),
                new Text("Please note that this is for demonstration purposes only. " +
                        "Your data may not persist, so we recommend exporting regularly."),
                new HtmlComponent("br"),
                new Text("For the best experience, please view this website on a desktop or a " +
                        "laptop. This web app is not yet optimised for mobile.")
        );
        v.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        v.setAlignItems(FlexComponent.Alignment.CENTER);

        FlexLayout layout = new FlexLayout(v);
        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.getElement().getStyle().set("order", "999");
        return layout;
    }
}

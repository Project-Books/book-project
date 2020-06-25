package com.karankumar.bookproject.ui.goal;

import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "goal", layout = MainView.class)
@PageTitle("Goal | Book Project")
public class GoalView extends HorizontalLayout {
    public GoalView() {
        H3 text = new H3("Coming soon");
        VerticalLayout verticalLayout = new VerticalLayout(text);
        verticalLayout.setAlignItems(Alignment.CENTER);

        add(verticalLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }
}

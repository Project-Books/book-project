package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "statistics", layout = MainView.class)
@PageTitle("Statistics | Book Project")
public class StatisticsView extends HorizontalLayout {
    public StatisticsView() {
    }
}

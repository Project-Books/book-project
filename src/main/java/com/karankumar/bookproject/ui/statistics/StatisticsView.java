package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.CalculateBookStatistics;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Route(value = "statistics", layout = MainView.class)
@PageTitle("Statistics | Book Project")
@Log
public class StatisticsView extends HorizontalLayout {
    public StatisticsView(PredefinedShelfService predefinedShelfService) {
        CalculateBookStatistics bookStatistics = new CalculateBookStatistics(predefinedShelfService);
        LOGGER.log(Level.INFO, "Avg page length: " + bookStatistics.calculateAveragePageLength());
    }

}

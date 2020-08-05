package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.GenreStatistics;
import com.karankumar.bookproject.backend.statistics.PageStatistics;
import com.karankumar.bookproject.backend.statistics.RatingStatistics;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.java.Log;

@Route(value = "statistics", layout = MainView.class)
@PageTitle("Statistics | Book Project")
@Log
public class StatisticsView extends VerticalLayout {
    public StatisticsView(PredefinedShelfService predefinedShelfService) {
        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
        GenreStatistics genreStatistics = new GenreStatistics(predefinedShelfService);
        PageStatistics pageStatistics = new PageStatistics(predefinedShelfService);

        double averageRating = ratingStatistics.calculateAverageRatingGiven();
        String averageRatingOutOf10 = String.format("%s/10", averageRating);
        add(configureStatistic("Average rating given", averageRatingOutOf10));

        Book mostLikedBook = ratingStatistics.findMostLikedBook();
        String mostLikedBookStatistic =
            formatStatistic(mostLikedBook.getTitle(), mostLikedBook.getRating().toString(), "rating");
        add(configureStatistic("Most liked book", mostLikedBookStatistic));

        Book leastLikedBook = ratingStatistics.findLeastLikedBook();
        String leastLikedBookRating =
            formatStatistic(leastLikedBook.getTitle(), leastLikedBook.getRating().toString(), "rating");
        add(configureStatistic("Least liked book", leastLikedBookRating));

        Genre mostReadGenre = genreStatistics.findMostReadGenre();
        add(configureStatistic("Most read genre", mostReadGenre.toString()));

        Genre leastLikedGenre = genreStatistics.findLeastLikedGenre();
        add(configureStatistic("Least liked genre read", leastLikedGenre.toString()));

        int averagePageLength = pageStatistics.calculateAveragePageLength();
        add(configureStatistic("Average page length", String.format("%d pages", averagePageLength)));

        Book longestBook = pageStatistics.findBookWithMostPages();
        String longestBookStatistic =
                formatStatistic(longestBook.getTitle(), String.valueOf(longestBook.getNumberOfPages()), "pages");
        add(configureStatistic("Longest book read", longestBookStatistic));

        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private Div configureStatistic(String caption, String statistic) {
        Div div = new Div();
        H3 averageRatingGiven = new H3(caption + ":");
        VerticalLayout verticalLayout = new VerticalLayout(averageRatingGiven, new Text(statistic));
        verticalLayout.setAlignItems(Alignment.CENTER);
        div.add(verticalLayout);
        return div;
    }

    private String formatStatistic(String bookTitle, String statistic, String unit) {
        return String.format("%s (%s %s)", bookTitle, statistic, unit);
    }
}

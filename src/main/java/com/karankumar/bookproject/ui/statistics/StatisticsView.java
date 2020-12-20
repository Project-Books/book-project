/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui.statistics;

import com.helger.commons.annotation.VisibleForTesting;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.statistics.GenreStatistics;
import com.karankumar.bookproject.backend.statistics.PageStatistics;
import com.karankumar.bookproject.backend.statistics.RatingStatistics;
import com.karankumar.bookproject.backend.statistics.YearStatistics;
import com.karankumar.bookproject.ui.MainView;
import com.karankumar.bookproject.ui.components.AppFooter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import lombok.extern.java.Log;

import java.text.DecimalFormat;
import java.util.Optional;


@Route(value = "statistics", layout = MainView.class)
@PageTitle("Statistics | Book Project")
@Log
public class StatisticsView extends VerticalLayout {

    public StatisticsView(PredefinedShelfService predefinedShelfService) {
        for (StatisticType statistic : StatisticType.values()) {
            String caption = statistic.getCaption();
            Optional<String> value = statistic.calculateStatistic(predefinedShelfService);
            value.ifPresent(val -> add(configureStatistic(caption, val)));
        }
        add(new AppFooter());
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    private Div configureStatistic(String caption, String statistic) {
        Div div = new Div();
        H3 averageRatingGiven = new H3(caption);
        VerticalLayout verticalLayout = new VerticalLayout(averageRatingGiven, new Text(statistic));
        verticalLayout.setAlignItems(Alignment.CENTER);
        div.add(verticalLayout);
        return div;
    }

    @VisibleForTesting
    static String formatStatistic(String bookTitle, String statistic, String unit) {
        if (statistic.endsWith(unit)) {
            return String.format("%s (%s)", bookTitle, statistic);
        } else {
            return String.format("%s (%s %s)", bookTitle, statistic, unit);
        }
    }

    public enum StatisticType {
        AVERAGE_RATING("Average rating given:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
                Optional<Double> averageRating =
                        Optional.ofNullable(ratingStatistics.calculateAverageRatingGiven());
                return  averageRating.map(rating ->
                        String.format("%s/10", new DecimalFormat("#.00").format(rating)));
            }
        },
        MOST_LIKED_BOOK("Most liked book:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
                Optional<Book> mostLikedBook = Optional.ofNullable(ratingStatistics.findMostLikedBook());
                return mostLikedBook.map(book ->
                        formatStatistic(book.getTitle(), book.getRating().toString(), "rating"));
            }
        },
        LEAST_LIKED_BOOK("Least liked book:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
                Optional<Book> leastLikedBook =
                        Optional.ofNullable(ratingStatistics.findLeastLikedBook());
                return leastLikedBook.map(book ->
                        formatStatistic(book.getTitle(), book.getRating().toString(), "rating"));
            }
        },
        LEAST_LIKED_BOOK_THIS_YEAR("Least liked book this year:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                YearStatistics yearStatistics = new YearStatistics(predefinedShelfService);
                Optional<Book> leastLikedBook =
                        yearStatistics.findLeastLikedBookThisYear();
                return leastLikedBook.map(book ->
                        formatStatistic(book.getTitle(), book.getRating().toString(), "rating"));
            }
        },
        MOST_LIKED_BOOK_THIS_YEAR("Most liked book this year:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                YearStatistics yearStatistics = new YearStatistics(predefinedShelfService);
                Optional<Book> mostLikedBook =
                        yearStatistics.findMostLikedBookThisYear();
                return mostLikedBook.map(book ->
                        formatStatistic(book.getTitle(), book.getRating().toString(), "rating"));
            }
        },
        AVERAGE_RATING_THIS_YEAR("Average rating given this year:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                YearStatistics yearStatistics = new YearStatistics(predefinedShelfService);
                Optional<Double> averageRatingGivenThisYear =
                        yearStatistics.calculateAverageRatingGivenThisYear();
                return averageRatingGivenThisYear.map(rating ->
                        String.format("%s/10", new DecimalFormat("#.00").format(rating)));
            }
        },
        MOST_READ_GENRE("Most read genre:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                GenreStatistics genreStatistics = new GenreStatistics(predefinedShelfService);
                Optional<BookGenre> mostReadGenre =
                        Optional.ofNullable(genreStatistics.findMostReadGenre());
                return mostReadGenre.map(BookGenre::toString);
            }
        },
        MOST_LIKED_GENRE("Most liked genre read:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                GenreStatistics genreStatistics = new GenreStatistics(predefinedShelfService);
                Optional<BookGenre> mostLikedGenre =
                        Optional.ofNullable(genreStatistics.findMostLikedGenre());
                return mostLikedGenre.map(BookGenre::toString);
            }
        },
        LEAST_LIKED_GENRE("Least liked genre read:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                GenreStatistics genreStatistics = new GenreStatistics(predefinedShelfService);
                Optional<BookGenre> leastLikedGenre =
                        Optional.ofNullable(genreStatistics.findLeastLikedGenre());
                return leastLikedGenre.map(BookGenre::toString);
            }
        },
        AVERAGE_PAGE_LENGTH("Average page length:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                PageStatistics pageStatistics = new PageStatistics(predefinedShelfService);
                Optional<Double> averagePageLength =
                        Optional.ofNullable(pageStatistics.calculateAveragePageLength());
                return averagePageLength.map(pageLength -> String.format("%f pages", pageLength));
            }
        },
        LONGEST_BOOK("Longest book read:") {
            @Override
            public Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService) {
                PageStatistics pageStatistics = new PageStatistics(predefinedShelfService);
                Optional<Book> longestBook =
                        Optional.ofNullable(pageStatistics.findBookWithMostPages());
                return longestBook.map(book ->
                        formatStatistic(book.getTitle(), String.valueOf(book.getNumberOfPages()),
                        "pages")
                );
            }
        };

        @Getter private final String caption;

        StatisticType(String caption) {
            this.caption = caption;
        }

        public abstract Optional<String> calculateStatistic(PredefinedShelfService predefinedShelfService);
    }
}

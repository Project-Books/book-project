package com.karankumar.bookproject.ui.statistics;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import com.karankumar.bookproject.ui.MainView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Set;

@Route(value = "statistics", layout = MainView.class)
@PageTitle("Statistics | Book Project")
public class StatisticsView extends HorizontalLayout {
    private final Set<Book> readShelfBooks;

    public StatisticsView(PredefinedShelfService predefinedShelfService) {
        PredefinedShelf readShelf = new PredefinedShelfUtils(predefinedShelfService).findReadShelf();
        readShelfBooks = readShelf.getBooks();
    }

    private void calculateMostReadGenres() {
        // TODO
    }

    private void calculateAverageRatingGiven() {
        // TODO
    }

    private void findMostLikedBook() {
        // TODO
    }

    private void findLeastLikedBook() {
        // TODO
    }

    private void findBookWithMostPages() {
        // TODO
    }

    private void calculateAveragePageLength() {
        // TODO
    }

    private void findMostLikedGenres() {
        // TODO
    }

    private void findLeastLikedGenres() {
        // TODO
    }

}

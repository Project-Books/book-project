package com.karankumar.bookproject.ui.shelf.visibility;

import com.karankumar.bookproject.ui.shelf.BookGrid;

import static com.karankumar.bookproject.ui.shelf.component.BookGridColumn.*;

public class ReadBookVisibility implements BookVisibilityStrategy {
    @Override
    public void toggleColumnVisibility(BookGrid bookGrid) {
        bookGrid.toggleColumnVisibility(RATING_KEY, true);
        bookGrid.toggleColumnVisibility(DATE_STARTED_KEY, true);
        bookGrid.toggleColumnVisibility(DATE_FINISHED_KEY, true);
        bookGrid.toggleColumnVisibility(PAGES_READ_KEY, false);
    }
}
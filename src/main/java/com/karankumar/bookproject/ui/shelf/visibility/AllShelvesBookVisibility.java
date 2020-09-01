package com.karankumar.bookproject.ui.shelf.visibility;

import com.karankumar.bookproject.ui.shelf.BookGrid;
import com.karankumar.bookproject.ui.shelf.component.BookGridColumn;

public class AllShelvesBookVisibility implements BookVisibilityStrategy {
    @Override
    public void toggleColumnVisibility(BookGrid bookGrid) {
        bookGrid.toggleColumnVisibility(BookGridColumn.RATING_KEY, true);
        bookGrid.toggleColumnVisibility(BookGridColumn.DATE_STARTED_KEY, true);
        bookGrid.toggleColumnVisibility(BookGridColumn.DATE_FINISHED_KEY, true);
        bookGrid.toggleColumnVisibility(BookGridColumn.PAGES_READ_KEY, true);
    }
}
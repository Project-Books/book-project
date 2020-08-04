package com.karankumar.bookproject.ui.shelf.visibility;

import com.karankumar.bookproject.ui.shelf.BookGrid;

public interface BookVisibilityStrategy {
    void toggleColumnVisibility(BookGrid bookGrid);
}
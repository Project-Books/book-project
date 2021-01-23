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

package com.karankumar.bookproject.backend.util;

import com.karankumar.bookproject.backend.model.CustomShelf;
import com.karankumar.bookproject.backend.model.PredefinedShelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ShelfUtils {
    private ShelfUtils() {}

    public static final String ALL_BOOKS_SHELF = "All books";

    public static List<String> findAllShelfNames(List<CustomShelf> allCustomShelves) {
        List<String> shelves = new ArrayList<>();
        for (PredefinedShelf.ShelfName predefinedShelfName : PredefinedShelf.ShelfName.values()) {
            shelves.add(predefinedShelfName.toString());
        }

        for (CustomShelf customShelf : allCustomShelves) {
            shelves.add(customShelf.getShelfName());
        }
        return shelves;
    }

    public static boolean isAllBooksShelf(String shelfName) {
        return Objects.equals(shelfName, ALL_BOOKS_SHELF);
    }
}

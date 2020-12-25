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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;

import lombok.extern.java.Log;

@Log
public class PredefinedShelfUtils {
    private PredefinedShelfUtils() {
    }

    public static PredefinedShelf.ShelfName getPredefinedShelfName(String predefinedShelfName) {
        switch (predefinedShelfName) {
            case "To read":
                return PredefinedShelf.ShelfName.TO_READ;
            case "Reading":
                return PredefinedShelf.ShelfName.READING;
            case "Read":
                return PredefinedShelf.ShelfName.READ;
            case "Did not finish":
                return PredefinedShelf.ShelfName.DID_NOT_FINISH;
            default:
                return null;
        }
    }

    public static boolean isPredefinedShelf(String shelfName) {
        return Arrays.stream(ShelfName.values())
                     .map(ShelfName::toString)
                     .anyMatch(shelfName::equalsIgnoreCase);
    }

    /**
     * Fetches all of the books in the chosen predefined shelves
     */
    public static Set<Book> getBooksInPredefinedShelves(List<PredefinedShelf> predefinedShelves) {
        return predefinedShelves.stream()
                                .map(PredefinedShelf::getBooks)
                                .collect(HashSet::new, Set::addAll, Set::addAll);
    }
}

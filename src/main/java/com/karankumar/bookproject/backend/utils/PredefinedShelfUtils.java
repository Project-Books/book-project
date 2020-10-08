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

package com.karankumar.bookproject.backend.utils;

import static com.karankumar.bookproject.backend.utils.ShelfUtils.isAllBooksShelf;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;
import com.karankumar.bookproject.backend.entity.Shelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import lombok.extern.java.Log;

@Log
public class PredefinedShelfUtils {
    private final PredefinedShelfService predefinedShelfService;

    public PredefinedShelfUtils(PredefinedShelfService predefinedShelfService) {
        this.predefinedShelfService = predefinedShelfService;
    }

    public Optional<PredefinedShelf.ShelfName> getPredefinedShelfName(String predefinedShelfName) {
        switch (predefinedShelfName) {
            case "To read":
                return of(PredefinedShelf.ShelfName.TO_READ);
            case "Reading":
                return of(PredefinedShelf.ShelfName.READING);
            case "Read":
                return of(PredefinedShelf.ShelfName.READ);
            case "Did not finish":
                return of(PredefinedShelf.ShelfName.DID_NOT_FINISH);
            default:
                return empty();
        }
    }

    public List<String> getPredefinedShelfNamesAsStrings() {
       return predefinedShelfService.findAll().stream()
               .map(Shelf::getShelfName)
               .collect(Collectors.toList());
    }

    public static boolean isPredefinedShelf(String shelfName) {
        return Arrays.stream(ShelfName.values())
                     .map(ShelfName::toString)
                     .anyMatch(shelfName::equalsIgnoreCase);
    }

    /**
     * Fetches all of the books in the chosen predefined shelf
     */
    public Set<Book> getBooksInChosenPredefinedShelf(String chosenShelf) {
        Set<Book> books;
        if (isAllBooksShelf(chosenShelf)) {
            return getBooksInAllPredefinedShelves();
        }

        return getPredefinedShelfName(chosenShelf)
                .map(predefinedShelfName -> predefinedShelfService.findByPredefinedShelfName(predefinedShelfName))
                .map(repository -> repository.getBooks())
                .orElse(new HashSet<>());

    }

    public Set<Book> getBooksInAllPredefinedShelves() {
        return getBooksInPredefinedShelves(predefinedShelfService.findAll());
    }

    /**
     * Fetches all of the books in the chosen predefined shelves
     */
    public Set<Book> getBooksInPredefinedShelves(List<PredefinedShelf> predefinedShelves) {
        return predefinedShelves.stream()
                .map(PredefinedShelf::getBooks)
                .collect(HashSet::new, Set::addAll, Set::addAll);
    }
}

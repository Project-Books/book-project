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

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.Shelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Log
public class PredefinedShelfUtils {
    private final PredefinedShelfService predefinedShelfService;

    public PredefinedShelfUtils(PredefinedShelfService predefinedShelfService) {
        this.predefinedShelfService = predefinedShelfService;
    }

    /**
     * Convenience method for accessing the read shelf
     */
    public PredefinedShelf findToReadShelf() {
        return findPredefinedShelf(PredefinedShelf.ShelfName.TO_READ);
    }

    public PredefinedShelf findReadingShelf() {
        return findPredefinedShelf(PredefinedShelf.ShelfName.READING);
    }

    /**
     * Convenience method for accessing the read shelf
     */
    public PredefinedShelf findReadShelf() {
        return findPredefinedShelf(PredefinedShelf.ShelfName.READ);
    }

    public PredefinedShelf findPredefinedShelf(PredefinedShelf.ShelfName shelfName) {
        LOGGER.log(Level.INFO, "Shelves: " + predefinedShelfService.findAll());
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf ->
                                             shelf.getPredefinedShelfName().equals(shelfName))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }

    public PredefinedShelf.ShelfName getPredefinedShelfName(String predefinedShelfName) {
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

    public List<String> getPredefinedShelfNamesAsStrings() {
       return predefinedShelfService.findAll().stream()
               .map(Shelf::getShelfName)
               .collect(Collectors.toList());
    }

    public static boolean isPredefinedShelf(String shelfName) {
        List<String> predefinedShelfNames = new ArrayList<>();
        for (PredefinedShelf.ShelfName predefinedShelfName : PredefinedShelf.ShelfName.values()) {
            predefinedShelfNames.add(predefinedShelfName.toString());
        }
        return predefinedShelfNames.contains(shelfName);
    }

    /**
     * Fetches all of the books in the chosen predefined shelf
     */
    public Set<Book> getBooksInChosenPredefinedShelf(String chosenShelf) {
        PredefinedShelf.ShelfName predefinedShelfName = getPredefinedShelfName(chosenShelf);
        Set<Book> books;
        List<PredefinedShelf> predefinedShelves = predefinedShelfService.findAll(predefinedShelfName);
        if (predefinedShelves.isEmpty()) {
            books = new HashSet<>();
        } else {
            PredefinedShelf customShelf = predefinedShelves.get(0);
            books = customShelf.getBooks();
        }
        return books;
    }
}

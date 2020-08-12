package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.Shelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
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
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(shelfName))
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
}

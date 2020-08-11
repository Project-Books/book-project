package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import lombok.extern.java.Log;

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

    private PredefinedShelf findPredefinedShelf(PredefinedShelf.ShelfName shelfName) {
        LOGGER.log(Level.INFO, "Shelves: " + predefinedShelfService.findAll());
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(shelfName))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }

    public PredefinedShelf.ShelfName getPredefinedShelfName(String predefinedShelfName) {
        switch (predefinedShelfName) {
            case "TO_READ":
                return PredefinedShelf.ShelfName.TO_READ;
            case "READING":
                return PredefinedShelf.ShelfName.READING;
            case "READ":
                return PredefinedShelf.ShelfName.READ;
            case "DID_NOT_FINISH":
                return PredefinedShelf.ShelfName.DID_NOT_FINISH;
            default:
                return null;
        }
    }
}

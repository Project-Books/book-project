package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import java.util.stream.Collectors;

public class PredefinedShelfUtils {
    private final PredefinedShelfService predefinedShelfService;

    public PredefinedShelfUtils(PredefinedShelfService predefinedShelfService) {
        this.predefinedShelfService = predefinedShelfService;
    }

    /**
     * Helper method that find a shelf with a particular name
     * @param shelfName the name of the shelf to look for
     * @return the shelf that matches the shelf name provided
     */
    public PredefinedShelf findPredefinedShelf(PredefinedShelf.ShelfName shelfName) {
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(shelfName))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }

    /**
     * @return the read shelf if it can be found, null otherwise.
     */
    public PredefinedShelf findReadShelf() {
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(PredefinedShelf.ShelfName.READ))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }
}

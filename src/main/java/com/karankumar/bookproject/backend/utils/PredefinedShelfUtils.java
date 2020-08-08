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
        return predefinedShelfService.findAll()
                                     .stream()
                                     .filter(shelf -> shelf.getPredefinedShelfName().equals(shelfName))
                                     .collect(Collectors.toList())
                                     .get(0); // there should only be one
    }
}

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
     * Only books in the read shelf count towards the goal
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

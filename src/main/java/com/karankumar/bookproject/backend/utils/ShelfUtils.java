package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;

import java.util.ArrayList;
import java.util.List;

public class ShelfUtils {
    private ShelfUtils() {}

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
}

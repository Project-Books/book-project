package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.service.CustomShelfService;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomShelfUtils {
    private final CustomShelfService customShelfService;

    public CustomShelfUtils(CustomShelfService customShelfService) {
        this.customShelfService = customShelfService;
    }

    public List<@NotNull String> getCustomShelfNames() {
        return customShelfService.findAll()
                                 .stream()
                                 .map(CustomShelf::getShelfName)
                                 .collect(Collectors.toList());
    }

    /**
     * Gets all of the books in the specified custom shelf
     */
    public Set<Book> getBooksInChosenCustomShelf(String shelfName) {
        Set<Book> books;
        List<CustomShelf> customShelves = customShelfService.findAll(shelfName);
        if (customShelves.isEmpty()) {
            books = new HashSet<>();
        } else {
            CustomShelf customShelf = customShelves.get(0);
            books = customShelf.getBooks();
        }
        return books;
    }
}

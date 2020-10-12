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
        return customShelfService.findAllForLoggedInUser()
                                 .stream()
                                 .map(CustomShelf::getShelfName)
                                 .collect(Collectors.toList());
    }

    /**
     * Gets all of the books in the specified custom shelf
     */
    public Set<Book> getBooksInCustomShelf(String shelfName) {
        CustomShelf customShelf = customShelfService.findByShelfNameAndLoggedInUser(shelfName);
        if (customShelf == null) {
            return new HashSet<>();
        }
        return customShelf.getBooks();
    }
}

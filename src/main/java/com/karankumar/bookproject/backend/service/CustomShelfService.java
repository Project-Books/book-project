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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.Shelf;
import com.karankumar.bookproject.backend.repository.CustomShelfRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log
public class CustomShelfService {
    private final CustomShelfRepository customShelfRepository;
    private final UserService userService;

    public CustomShelfService(CustomShelfRepository customShelfRepository, UserService userService) {
        this.customShelfRepository = customShelfRepository;
        this.userService = userService;
    }

    public CustomShelf createCustomShelf(String shelfName) {
        return new CustomShelf(shelfName, userService.getCurrentUser());
    }

    public CustomShelf findById(Long id) {
        return customShelfRepository.getOne(id);
    }

    public List<CustomShelf> findAllForLoggedInUser() {
        return customShelfRepository.findAllByUser(userService.getCurrentUser());
    }

    public CustomShelf findByShelfNameAndLoggedInUser(String shelfName) {
        return customShelfRepository.findByShelfNameAndUser(shelfName, userService.getCurrentUser());
    }

    public void save(@NonNull CustomShelf customShelf) {
        customShelfRepository.save(customShelf);
    }

    public void delete(@NonNull CustomShelf customShelf) {
        customShelfRepository.delete(customShelf);
    }

    public void deleteAll() {
        customShelfRepository.deleteAll();
    }

    public Long count() {
        return customShelfRepository.count();
    }

    public Collection<CustomShelf> findAll() {
        return customShelfRepository.findAll();
    }

    public List<CustomShelf> findAll(String shelfName) {
        if (shelfName == null) {
            return customShelfRepository.findAll();
        } else {
            return customShelfRepository.findByShelfName(shelfName);
        }
    }

    public List<@NotNull String> getCustomShelfNames() {
        return customShelfRepository.findAll()
                .stream()
                .map(CustomShelf::getShelfName)
                .collect(Collectors.toList());
    }

    /**
     * Gets all of the books in the specified custom shelf
     */
    public Set<Book> getBooksInCustomShelf(String shelfName) {
        Set<Book> books;
        List<CustomShelf> customShelves = this.findAll(shelfName);
        if (customShelves.isEmpty()) {
            books = new HashSet<>();
        } else {
            CustomShelf customShelf = customShelves.get(0);
            books = customShelf.getBooks();
        }
        return books;
    }

    public Shelf getCustomShelfByName(String shelfName) {
        return customShelfRepository.findByShelfName(shelfName).get(0);
    }
}

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

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import com.karankumar.bookproject.backend.repository.UserCreatedShelfRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log
public class UserCreatedShelfService {
    private final UserCreatedShelfRepository userCreatedShelfRepository;
    private final UserService userService;

    public UserCreatedShelfService(UserCreatedShelfRepository userCreatedShelfRepository, UserService userService) {
        this.userCreatedShelfRepository = userCreatedShelfRepository;
        this.userService = userService;
    }

    public UserCreatedShelf createCustomShelf(String shelfName) {
        return new UserCreatedShelf(shelfName, userService.getCurrentUser());
    }

    public Optional<UserCreatedShelf> findById(@NonNull Long id) {
        return userCreatedShelfRepository.findById(id);
    }

    public List<UserCreatedShelf> findAllForLoggedInUser() {
        return userCreatedShelfRepository.findAllByUser(userService.getCurrentUser());
    }

    public Optional<UserCreatedShelf> findByShelfNameAndLoggedInUser(@NonNull String shelfName) {
        return userCreatedShelfRepository.findByShelfNameAndUser(shelfName, userService.getCurrentUser());
    }

    public UserCreatedShelf save(@NonNull UserCreatedShelf userCreatedShelf) {
        if (shelfNameExists(userCreatedShelf.getShelfName())) {
            throw new IllegalArgumentException(
                    String.format("Given shelfName %s already exists",
                            userCreatedShelf.getShelfName())
            );
        }
        return userCreatedShelfRepository.save(userCreatedShelf);
    }

    public void delete(@NonNull UserCreatedShelf userCreatedShelf) {
        userCreatedShelfRepository.delete(userCreatedShelf);
    }

    public void deleteAll() {
        userCreatedShelfRepository.deleteAll();
    }

    public Long count() {
        return userCreatedShelfRepository.count();
    }

    public List<UserCreatedShelf> findAll() {
        return userCreatedShelfRepository.findAll();
    }

    public List<UserCreatedShelf> findAll(String shelfName) {
        if (shelfName == null) {
            return findAll();
        }
        return userCreatedShelfRepository.findByShelfName(shelfName);
    }

    public List<@NotNull String> getCustomShelfNames() {
        return userCreatedShelfRepository.findAll()
                                         .stream()
                                         .map(UserCreatedShelf::getShelfName)
                                         .collect(Collectors.toList());
    }

    /**
     * Gets all of the books in the specified custom shelf
     */
    public Set<Book> getBooksInCustomShelf(@NonNull String shelfName) {
        Set<Book> books;
        List<UserCreatedShelf> customShelves = this.findAll(shelfName);
        if (customShelves.isEmpty()) {
            books = new HashSet<>();
        } else {
            UserCreatedShelf userCreatedShelf = customShelves.get(0);
            books = userCreatedShelf.getBooks();
        }
        return books;
    }

    public Optional<Shelf> getCustomShelfByName(@NonNull String shelfName) {
        return Optional.ofNullable(userCreatedShelfRepository.findByShelfName(shelfName).get(0));
    }

    public UserCreatedShelf findOrCreate(@NonNull String shelfName) {
        Assert.hasText(shelfName, "Shelf Name cannot be empty");
        return findByShelfNameAndLoggedInUser(shelfName)
        		.orElseGet(() -> save(createCustomShelf(shelfName)));
    }

    private boolean shelfNameExists(String shelfName) {
        if (PredefinedShelfService.isPredefinedShelf(shelfName.trim())) {
            return true;
        }

        return userCreatedShelfRepository.shelfNameExists(shelfName);
    }
}

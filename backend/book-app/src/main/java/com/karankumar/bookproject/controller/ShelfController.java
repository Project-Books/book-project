/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.controller;

import com.karankumar.bookproject.model.PredefinedShelf;
import com.karankumar.bookproject.model.Shelf;
import com.karankumar.bookproject.model.UserCreatedShelf;
import com.karankumar.bookproject.service.PredefinedShelfService;
import com.karankumar.bookproject.service.ShelfNameExistsException;
import com.karankumar.bookproject.service.UserCreatedShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/shelf")
public class ShelfController {
    private final UserCreatedShelfService userCreatedShelfService;
    private final PredefinedShelfService predefinedShelfService;

    @Autowired
    public ShelfController(
            UserCreatedShelfService userCreatedShelfService,
            PredefinedShelfService predefinedShelfService
    ) {
        this.userCreatedShelfService = userCreatedShelfService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Shelf> all() {
        List<PredefinedShelf> predefinedShelves = predefinedShelfService.findAllForLoggedInUser();
        List<UserCreatedShelf> userCreatedShelves = userCreatedShelfService.findAllForLoggedInUser();

        return Stream.concat(
                predefinedShelves.stream(),
                userCreatedShelves.stream()
        ).collect(Collectors.toList());
    }

    @PostMapping("/{shelfName}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedShelf create(@PathVariable String shelfName) {
        try {
            UserCreatedShelf customShelf = userCreatedShelfService.createCustomShelf(shelfName);
            return userCreatedShelfService.save(customShelf);
        } catch (ShelfNameExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Shelf name already exists"
            );
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Shelf name cannot be null or empty"
            );
        }
    }

    @PutMapping("/{shelfName}/{newShelfName}")
    @ResponseStatus(HttpStatus.OK)
    public UserCreatedShelf rename(@PathVariable String shelfName, @PathVariable String newShelfName) {
        try {
            UserCreatedShelf customShelf = userCreatedShelfService.findByShelfNameAndLoggedInUser(shelfName).get();
            customShelf.setShelfName(newShelfName);
            return userCreatedShelfService.save(customShelf);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Specified shelf does not exist"
            );
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Shelf name cannot be null or empty"
            );
        }
    }

    @DeleteMapping("/{shelfName}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String shelfName) {
        try {
            UserCreatedShelf customShelf = userCreatedShelfService.findByShelfNameAndLoggedInUser(shelfName).get();
            userCreatedShelfService.delete(customShelf);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Specified shelf does not exist"
            );
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Shelf name cannot be null or empty"
            );
        }
    }
}

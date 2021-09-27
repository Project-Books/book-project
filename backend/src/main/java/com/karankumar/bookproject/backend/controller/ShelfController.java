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

package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import com.karankumar.bookproject.backend.service.ShelfNameExistsException;
import com.karankumar.bookproject.backend.service.UserCreatedShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/shelf")
public class ShelfController {
    private final UserCreatedShelfService userCreatedShelfService;

    @Autowired
    public ShelfController(UserCreatedShelfService userCreatedShelfService) {
        this.userCreatedShelfService = userCreatedShelfService;
    }

    @PostMapping("/{shelfName}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedShelf addShelf(@PathVariable String shelfName) {
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
}

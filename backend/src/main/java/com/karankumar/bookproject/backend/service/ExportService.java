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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.dto.ExportBookDto;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.UserCreatedShelf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExportService {
    private final PredefinedShelfService predefinedShelfService;
    private final UserCreatedShelfService userCreatedShelfService;

    public ExportService(PredefinedShelfService predefinedShelfService,
                         UserCreatedShelfService userCreatedShelfService) {
        this.predefinedShelfService = predefinedShelfService;
        this.userCreatedShelfService = userCreatedShelfService;
    }

    @Transactional
    public ExportBookDto exportUserBookData() {
        // Get User's books on predefined Shelf
        ExportBookDto exportBookDto = new ExportBookDto();
        List<PredefinedShelf> userPredefinedShelves = predefinedShelfService.findAllForLoggedInUser();
        exportBookDto.setPredefinedShelfBook(
                predefinedShelfService.getBooksInPredefinedShelves(userPredefinedShelves)
        );

        List<UserCreatedShelf> userCreatedShelves = userCreatedShelfService.findAllForLoggedInUser();
        exportBookDto.setUserCreatedShelfBook(
                userCreatedShelfService.getBooksInUserCreatedShelves(userCreatedShelves)
        );
        return exportBookDto;
    }
}

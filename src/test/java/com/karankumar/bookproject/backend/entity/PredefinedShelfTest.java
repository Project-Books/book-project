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

package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.annotations.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class PredefinedShelfTest {
    @Test
    void testBooksWithoutPredefinedShelfShouldStillExist(
            @Autowired PredefinedShelfService predefinedShelfService) {
        List<PredefinedShelf> shelves = predefinedShelfService.findAll();

        assertEquals(4, shelves.size());
        assertEquals(PredefinedShelf.ShelfName.TO_READ, shelves.get(0).getPredefinedShelfName());
        assertEquals(PredefinedShelf.ShelfName.READING, shelves.get(1).getPredefinedShelfName());
        assertEquals(PredefinedShelf.ShelfName.READ, shelves.get(2).getPredefinedShelfName());
        assertEquals(PredefinedShelf.ShelfName.DID_NOT_FINISH, shelves.get(3).getPredefinedShelfName());
    }

    @AfterEach
    public void reset(@Autowired BookService bookService) {
        resetBookService(bookService);
    }

    private void resetBookService(BookService bookService) {
        bookService.deleteAll();
    }
}

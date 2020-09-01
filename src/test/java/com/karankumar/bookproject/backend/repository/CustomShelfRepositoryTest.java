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

package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DataJpaIntegrationTest
class CustomShelfRepositoryTest {
    @Autowired private CustomShelfRepository repository;
    private final String test1 = "Test1";

    @BeforeEach
    void setup() {
        repository.save(new CustomShelf(test1));
        repository.save(new CustomShelf("Test2"));
        repository.save(new CustomShelf("Test3"));
    }

    @Test
    void whenShelfExistsFindByShelfNameReturnsOneShelf() {
        List<CustomShelf> shelves = repository.findByShelfName(test1);
        Assertions.assertEquals(1, shelves.size());
    }
}


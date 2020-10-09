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
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.utils.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaIntegrationTest
class PredefinedShelfRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private PredefinedShelfRepository repository;
    private User user;

    @BeforeEach
    void setup() {
        user = SecurityTestUtils.getTestUser(userRepository);
        repository.saveAll(
            Arrays.stream(PredefinedShelf.ShelfName.values())
                  .map(shelfName -> new PredefinedShelf(shelfName, user))
                  .collect(Collectors.toList())
        );
    }

    @Test
    @DisplayName("When a shelf exists, findByPredefinedShelfName correctly returns the shelf")
    void findByShelfNameReturnsOneShelf() {
        PredefinedShelf shelf = repository.findByPredefinedShelfNameAndUser(PredefinedShelf.ShelfName.TO_READ, user);
        assertThat(shelf).isNotNull();

        assertSoftly(
                softly -> {
                    softly.assertThat(shelf.getPredefinedShelfName())
                          .isEqualTo(PredefinedShelf.ShelfName.TO_READ);
                    softly.assertThat(shelf.getBooks()).isNull();
                }
        );
    }
}


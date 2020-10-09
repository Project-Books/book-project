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
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.utils.SecurityTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaIntegrationTest
class CustomShelfRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private CustomShelfRepository repository;
    private final String customShelf1 = "Test1";
    private User user;

    @Test
    @DisplayName("When shelf exists, findByShelfName returns one shelf")
    void findByShelfNameCorrectlyReturnsOneShelf() {
        // given
        saveCustomShelves();

        // when
        CustomShelf shelf = repository.findByShelfNameAndUser(customShelf1, user);

        // then
        assertThat(shelf).isNotNull();
    }

    private void saveCustomShelves() {
        user = SecurityTestUtils.getTestUser(userRepository);
        repository.save(new CustomShelf(customShelf1, user));
        repository.save(new CustomShelf("Test2", user));
        repository.save(new CustomShelf("Test3", user));
    }
}


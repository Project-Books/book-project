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

package com.karankumar.bookproject.book.repository;

import com.karankumar.bookproject.account.repository.UserRepository;
import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.shelf.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.account.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.karankumar.bookproject.shelf.model.PredefinedShelf.ShelfName.TO_READ;
import static com.karankumar.bookproject.util.SecurityTestUtils.getTestUser;
import static com.karankumar.bookproject.util.SecurityTestUtils.insertTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaIntegrationTest
@DisplayName("PredefinedShelfRepository should")
@Disabled
// TODO: fix failing test
class PredefinedShelfRepositoryTest {
  private final UserRepository userRepository;
  private final PredefinedShelfRepository repository;

  private User user;

  @Autowired
  PredefinedShelfRepositoryTest(
      UserRepository userRepository, PredefinedShelfRepository repository) {
    this.userRepository = userRepository;
    this.repository = repository;
  }

  @BeforeEach
  void setup() {
    user = getTestUser(userRepository);
    createShelvesForUser(user);
    createShelvesForUser(insertTestUser(userRepository));
  }

  @Test
  void findCorrectShelf() {
    Optional<PredefinedShelf> shelf = repository.findByPredefinedShelfNameAndUser(TO_READ, user);

    assertSoftly(
        softly -> {
          softly.assertThat(shelf).isPresent();
          softly.assertThat(shelf.get().getPredefinedShelfName()).isEqualTo(TO_READ);
          softly.assertThat(shelf.get().getUser().getId()).isEqualTo(user.getId());
        });
  }

  @Test
  @DisplayName("findByPredefinedShelfName correctly returns empty if shelf doesn't exist")
  void findByPredefinedShelfNameAndUserReturnsEmpty() {
    // given
    repository.deleteAll();

    // when
    Optional<PredefinedShelf> shelf = repository.findByPredefinedShelfNameAndUser(TO_READ, user);

    // then
    assertThat(shelf).isEmpty();
  }

  @Test
  @DisplayName("findAllByUser correctly returns shelves for a user")
  void findAllByUser() {
    // when
    List<PredefinedShelf> shelves = repository.findAllByUser(user);
    assertThat(shelves).isNotNull().isNotEmpty();

    // then
    assertSoftly(
        softly ->
            softly
                .assertThat(shelves)
                .allSatisfy(shelf -> assertThat(shelf.getUser().getId()).isEqualTo(user.getId())));
  }

  @Test
  @DisplayName("findAllByUser correctly returns empty list for a user")
  void findAllByUserIsEmpty() {
    // given
    repository.deleteAll();

    // when
    List<PredefinedShelf> shelves = repository.findAllByUser(user);

    // then
    assertThat(shelves).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("countAllByUser correctly counts shelves for a user")
  void countAllByUser() {
    // when
    int count = repository.countAllByUser(user);

    // then
    assertThat(count).isEqualTo(PredefinedShelf.ShelfName.values().length);
  }

  private void createShelvesForUser(User user) {
    repository.saveAll(
        Arrays.stream(PredefinedShelf.ShelfName.values())
            .map(shelfName -> new PredefinedShelf(shelfName, user))
            .collect(Collectors.toList()));
  }
}

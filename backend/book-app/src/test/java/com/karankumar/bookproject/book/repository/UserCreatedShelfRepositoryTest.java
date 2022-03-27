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
import com.karankumar.bookproject.shelf.repository.UserCreatedShelfRepository;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.account.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.karankumar.bookproject.util.SecurityTestUtils.getTestUser;
import static com.karankumar.bookproject.util.SecurityTestUtils.insertTestUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaIntegrationTest
@DisplayName("CustomShelfRepository should")
class UserCreatedShelfRepositoryTest {
  private static final String CUSTOM_SHELF_NAME = "Test1";

  private final UserRepository userRepository;
  private final UserCreatedShelfRepository underTest;

  private User user;

  @Autowired
  UserCreatedShelfRepositoryTest(
      UserRepository userRepository, UserCreatedShelfRepository userCreatedShelfRepository) {
    this.userRepository = userRepository;
    this.underTest = userCreatedShelfRepository;
  }

  @BeforeEach
  void setUp() {
    user = getTestUser(userRepository);
    createShelvesForUser(user);
    createShelvesForUser(insertTestUser(userRepository));
  }

  @Test
  @Disabled
  // TODO: fix failing test
  void findCorrectShelf() {
    Optional<UserCreatedShelf> shelf = underTest.findByShelfNameAndUser(CUSTOM_SHELF_NAME, user);
    assertThat(shelf).isPresent();

    assertSoftly(
        softly -> {
          softly.assertThat(shelf.get().getShelfName()).isEqualTo(CUSTOM_SHELF_NAME);
          softly.assertThat(shelf.get().getUser().getId()).isEqualTo(user.getId());
        });
  }

  @Test
  @DisplayName("findByShelfNameAndUser correctly returns null if shelf doesn't exist")
  @Disabled
  // TODO: fix failing test
  void findByShelfNameAndUserReturnsNull() {
    // given
    underTest.deleteAll();

    // when
    Optional<UserCreatedShelf> shelf = underTest.findByShelfNameAndUser(CUSTOM_SHELF_NAME, user);

    // then
    assertThat(shelf).isEmpty();
  }

  @Test
  @DisplayName("findAllByUser correctly returns shelves for a user")
  @Disabled
  // TODO: fix failing test
  void findAllByUser() {
    List<UserCreatedShelf> shelves = underTest.findAllByUser(user);
    assertThat(shelves).isNotNull().isNotEmpty();

    assertSoftly(
        softly ->
            softly
                .assertThat(shelves)
                .allSatisfy(shelf -> assertThat(shelf.getUser().getId()).isEqualTo(user.getId())));
  }

  @Test
  @DisplayName("findAllByUser correctly returns empty list for a user")
  @Disabled
  // TODO: fix failing test
  void findAllByUserIsEmpty() {
    // given
    underTest.deleteAll();

    // when
    List<UserCreatedShelf> shelves = underTest.findAllByUser(user);

    // then
    assertThat(shelves).isNotNull().isEmpty();
  }

  @ParameterizedTest
  @MethodSource("provideExistingUserCreatedShelfNames")
  @Disabled
  // TODO: fix failing test
  void returnTrueWhenUserCreatedShelfNameExists(String existingShelfName) {
    assertThat(underTest.shelfNameExists(existingShelfName)).isTrue();
  }

  private static Stream<Arguments> provideExistingUserCreatedShelfNames() {
    return Stream.of(
        Arguments.of("Test1"),
        Arguments.of("TEST1"),
        Arguments.of("test1"),
        Arguments.of("  test1   "));
  }

  @Test
  @Disabled
  // TODO: fix failing test
  void returnFalseWhenUserCreatedShelfNameNotExists() {
    assertThat(underTest.shelfNameExists("NotExistingShelfName")).isFalse();
  }

  private void createShelvesForUser(User user) {
    underTest.saveAll(
        Stream.of(CUSTOM_SHELF_NAME, "Test2", "Test3")
            .map(shelfName -> new UserCreatedShelf(shelfName, user))
            .collect(Collectors.toList()));
  }
}

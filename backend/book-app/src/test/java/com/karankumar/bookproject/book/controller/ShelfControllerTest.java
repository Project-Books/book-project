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

package com.karankumar.bookproject.book.controller;

import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.shelf.ShelfNameExistsException;
import com.karankumar.bookproject.shelf.service.UserCreatedShelfService;
import com.karankumar.bookproject.shelf.controller.ShelfController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShelfControllerTest {
  private final ShelfController shelfController;
  private final UserCreatedShelfService mockedUserCreatedShelfService;
  private final PredefinedShelfService mockedPredefinedShelfService;

  ShelfControllerTest() {
    mockedUserCreatedShelfService = mock(UserCreatedShelfService.class);
    mockedPredefinedShelfService = mock(PredefinedShelfService.class);
    shelfController =
        new ShelfController(mockedUserCreatedShelfService, mockedPredefinedShelfService);
  }

  @Test
  void all_returnsEmptyList_whenNoShelvesExist() {
    when(mockedPredefinedShelfService.findAllForLoggedInUser()).thenReturn(new ArrayList<>());
    when(mockedUserCreatedShelfService.findAllForLoggedInUser()).thenReturn(new ArrayList<>());

    assertThat(shelfController.all().size()).isZero();
  }

  @Test
  void all_returnsNonEmptyList_whenPredefinedShelvesExist() {
    // given
    List<PredefinedShelf> predefinedShelves = new ArrayList<>();
    PredefinedShelf predefinedShelf1 = mock(PredefinedShelf.class);
    PredefinedShelf predefinedShelf2 = mock(PredefinedShelf.class);
    predefinedShelves.add(predefinedShelf1);
    predefinedShelves.add(predefinedShelf2);

    // when
    when(mockedPredefinedShelfService.findAllForLoggedInUser()).thenReturn(predefinedShelves);
    when(mockedUserCreatedShelfService.findAllForLoggedInUser()).thenReturn(new ArrayList<>());

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(shelfController.all().size()).isEqualTo(predefinedShelves.size());
          softly
              .assertThat(shelfController.all())
              .isEqualTo(List.of(predefinedShelf1, predefinedShelf2));
        });
  }

  @Test
  void all_returnsNonEmptyList_whenUserCreatedShelvesExist() {
    // given
    List<UserCreatedShelf> userCreatedShelves = new ArrayList<>();
    UserCreatedShelf userCreatedShelf1 = mock(UserCreatedShelf.class);
    UserCreatedShelf userCreatedShelf2 = mock(UserCreatedShelf.class);
    userCreatedShelves.add(userCreatedShelf1);
    userCreatedShelves.add(userCreatedShelf2);

    // when
    when(mockedPredefinedShelfService.findAllForLoggedInUser()).thenReturn(new ArrayList<>());
    when(mockedUserCreatedShelfService.findAllForLoggedInUser()).thenReturn(userCreatedShelves);

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(shelfController.all().size()).isEqualTo(userCreatedShelves.size());
          softly
              .assertThat(shelfController.all())
              .isEqualTo(List.of(userCreatedShelf1, userCreatedShelf2));
        });
  }

  @Test
  void all_returnsNonCombinedList_whenBothPredefinedAndUserCreatedShelvesExist() {
    // given
    List<UserCreatedShelf> userCreatedShelves = new ArrayList<>();
    UserCreatedShelf userCreatedShelf1 = mock(UserCreatedShelf.class);
    UserCreatedShelf userCreatedShelf2 = mock(UserCreatedShelf.class);
    userCreatedShelves.add(userCreatedShelf1);
    userCreatedShelves.add(userCreatedShelf2);
    List<PredefinedShelf> predefinedShelves = new ArrayList<>();
    PredefinedShelf predefinedShelf1 = mock(PredefinedShelf.class);
    PredefinedShelf predefinedShelf2 = mock(PredefinedShelf.class);
    predefinedShelves.add(predefinedShelf1);
    predefinedShelves.add(predefinedShelf2);

    // when
    when(mockedPredefinedShelfService.findAllForLoggedInUser()).thenReturn(predefinedShelves);
    when(mockedUserCreatedShelfService.findAllForLoggedInUser()).thenReturn(userCreatedShelves);

    // then
    assertSoftly(
        softly -> {
          softly
              .assertThat(shelfController.all().size())
              .isEqualTo(userCreatedShelves.size() + predefinedShelves.size());
          softly
              .assertThat(shelfController.all())
              .containsExactlyInAnyOrder(
                  userCreatedShelf1, userCreatedShelf2, predefinedShelf1, predefinedShelf2);
        });
  }

  @Test
  void create_returnsBadRequest_ifShelfNameTaken() {
    // given
    when(mockedUserCreatedShelfService.createCustomShelf(anyString()))
        .thenThrow(new ShelfNameExistsException("anything"));

    // when
    ResponseEntity<String> response = shelfController.create("anything");

    assertSoftly(
        softly -> {
          softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
          softly.assertThat(response.getBody()).isEqualTo("Shelf name already exists");
        });
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "    ", "\t", "\n"})
  void create_throws_ifNoNameOrEmptyNameSpecified(String specifiedName) {
    // given
    when(mockedUserCreatedShelfService.createCustomShelf(any()))
        .thenThrow(new NullPointerException());

    // when
    ResponseEntity<String> response = shelfController.create(specifiedName);

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
          softly.assertThat(response.getBody()).isEqualTo("Shelf name cannot be null or empty");
        });
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "    ", "\t", "\n"})
  void renameShelf_ThrowsIfNoNameOrEmptyNameSpecifiedForLocatingShelf(String oldName) {
    // given
    // Allow calling the real method, because that's where the "lookup by name cannot be null/empty"
    // is validated
    when(mockedUserCreatedShelfService.findByShelfNameAndLoggedInUser(oldName))
        .thenCallRealMethod();
    try {
      // when
      shelfController.rename(oldName, "newName");
      fail("Should have thrown an exception for this empty-string case!");
    } catch (ResponseStatusException e) {
      assertSoftly(
          softly -> {
            softly.assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            softly.assertThat(e.getReason()).isEqualTo("Shelf name cannot be null or empty");
          });
    }
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "    ", "\t", "\n"})
  void renameShelf_ThrowsIfNoNameOrEmptyNameSpecifiedForNewName(String newName) {
    // given
    // Create a *real* UserCreatedShelf instance, because UserCreatedShelf.setShelfName(...) does
    // the validation
    UserCreatedShelf shelf = new UserCreatedShelf("someShelfName", mock(User.class));
    when(mockedUserCreatedShelfService.findByShelfNameAndLoggedInUser(shelf.getShelfName()))
        .thenReturn(Optional.of(shelf));
    try {
      // when
      shelfController.rename(shelf.getShelfName(), newName);
      fail("Should have thrown an exception for this empty-string case!");
    } catch (ResponseStatusException e) {
      assertSoftly(
          softly -> {
            softly.assertThat(e.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            softly.assertThat(e.getReason()).isEqualTo("Shelf name cannot be null or empty");
          });
    }
  }

  @Test
  void renameShelf_ThrowsIfUserCreatedShelfDoesNotExist() {
    // given
    String shelfName = "someShelfName";
    when(mockedUserCreatedShelfService.findByShelfNameAndLoggedInUser(shelfName))
        .thenReturn(Optional.empty());
    try {
      // when
      shelfController.rename(shelfName, shelfName + "2");
      fail("Should have thrown an exception when the specified shelf does not exist!");
    } catch (ResponseStatusException e) {
      assertSoftly(
          softly -> {
            softly.assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            softly.assertThat(e.getReason()).isEqualTo("Specified shelf does not exist");
          });
    }
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", "    ", "\t", "\n"})
  void deleteShelf_returnsBadRequest_ifShelfNameIsBlank(String name) {
    ResponseEntity<String> response = shelfController.delete(name);

    assertSoftly(
        softly -> {
          assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
          assertThat(response.getBody()).isEqualTo("Shelf name cannot be null or empty");
        });
  }

  @Test
  void deleteShelf_returnsBadRequest_ifUserCreatedShelfDoesNotExist() {
    // given
    String shelfName = "someShelfName";
    when(mockedUserCreatedShelfService.findByShelfNameAndLoggedInUser(shelfName))
        .thenReturn(Optional.empty());

    // when
    ResponseEntity<String> response = shelfController.delete(shelfName);

    // then
    assertSoftly(
        softly -> {
          softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
          softly.assertThat(response.getBody()).isEqualTo("Specified shelf does not exist");
        });
  }
}

/*
* The book project lets a user keep track of different books they would like to read, are currently
* reading, have read or did not finish.
* Copyright (C) 2020  Karan Kumar

* This program is free software: you can redistribute it and/or modify it under the terms of the
* GNU General Public License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE.  See the GNU General Public License for more details.

* You should have received a copy of the GNU General Public License along with this program.
* If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.book.service;

import com.karankumar.bookproject.account.service.UserService;
import com.karankumar.bookproject.book.model.PredefinedShelfName;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.shelf.repository.UserCreatedShelfRepository;
import com.karankumar.bookproject.shelf.service.UserCreatedShelfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCreatedShelfServiceTest {
  @Mock private UserCreatedShelfRepository userCreatedShelfRepository;
  private UserCreatedShelfService userCreatedShelfService;

  @BeforeEach
  void setUp() {
    UserService userService = mock(UserService.class);
    userCreatedShelfService = new UserCreatedShelfService(userCreatedShelfRepository, userService);
  }

  @Test
  void findById_throwsException_ifIdIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.findById(null));
    verify(userCreatedShelfRepository, never()).findById(anyLong());
  }

  @Test
  void canFindById() {
    userCreatedShelfService.findById(1L);
    verify(userCreatedShelfRepository).findById(anyLong());
  }

  @Test
  // TODO: fix
  @Disabled
  void canFindAllForLoggedInUser() {
    userCreatedShelfService.findAllForLoggedInUser();
    verify(userCreatedShelfRepository).findAllByUser(any(User.class));
  }

  @Test
  // TODO: fix
  @Disabled
  void findByShelfNameAndLoggedInUser_throwsException_IfShelfNameIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.findByShelfNameAndLoggedInUser(null));
    verify(userCreatedShelfRepository).findByShelfNameAndUser(anyString(), any());
  }

  @Test
  void save_throwsException_ifShelfIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.save(null));
    verify(userCreatedShelfRepository, never()).save(any(UserCreatedShelf.class));
  }

  @ParameterizedTest
  @EnumSource(PredefinedShelfName.class)
  void save_throws_whenNameExistsInPredefinedShelfNames(PredefinedShelfName predefinedShelfName) {
    // given
    UserCreatedShelf createdShelf =
        new UserCreatedShelf(predefinedShelfName.toString(), User.builder().build());

    // when/then
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userCreatedShelfService.save(createdShelf));
  }

  @ParameterizedTest
  @EnumSource(PredefinedShelfName.class)
  void save_throws_whenNameExistsInPredefinedShelfNames_andContainsWhiteSpaces(
      PredefinedShelfName predefinedShelfName) {
    // given
    UserCreatedShelf createdShelf =
        new UserCreatedShelf(
            "\t\n\f\r" + predefinedShelfName.toString() + "\t\n\f\r", User.builder().build());

    // when/then
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userCreatedShelfService.save(createdShelf));
  }

  @Test
  void save_throws_whenUserCreatedShelfNameAlreadyExists() {
    // given
    UserCreatedShelf createdShelf = new UserCreatedShelf("ExistingName", User.builder().build());
    when(userCreatedShelfRepository.shelfNameExists(anyString())).thenReturn(true);

    // when/then
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userCreatedShelfService.save(createdShelf));
  }

  @Test
  void save_whenUserCreatedShelfNameNotExists() {
    // given
    UserCreatedShelf createdShelf = new UserCreatedShelf("NotExistingName", User.builder().build());
    when(userCreatedShelfRepository.shelfNameExists(anyString())).thenReturn(true);

    // when/then
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> userCreatedShelfService.save(createdShelf));
  }

  @Test
  void canSaveNonNullCustomShelf() {
    // given
    User user = User.builder().build();
    UserCreatedShelf userCreatedShelf = new UserCreatedShelf("test", user);

    // when
    userCreatedShelfService.save(userCreatedShelf);

    // then
    ArgumentCaptor<UserCreatedShelf> customShelfArgumentCaptor =
        ArgumentCaptor.forClass(UserCreatedShelf.class);
    verify(userCreatedShelfRepository).save(customShelfArgumentCaptor.capture());
    UserCreatedShelf actual = customShelfArgumentCaptor.getValue();
    assertThat(actual).isEqualTo(userCreatedShelf);
  }

  @Test
  void delete_throwsException_ifShelfIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.delete(null));
    verify(userCreatedShelfRepository, never()).delete(any(UserCreatedShelf.class));
  }

  @Test
  void canDeleteNonNullCustomShelf() {
    // given
    User user = User.builder().build();
    UserCreatedShelf userCreatedShelf = new UserCreatedShelf("test", user);

    // when
    userCreatedShelfService.delete(userCreatedShelf);

    // then
    verify(userCreatedShelfRepository).delete(any(UserCreatedShelf.class));
  }

  @Test
  void canDeleteAll() {
    userCreatedShelfService.deleteAll();
    verify(userCreatedShelfRepository).deleteAll();
  }

  @Test
  void canCount() {
    userCreatedShelfService.count();
    verify(userCreatedShelfRepository).count();
  }

  @Test
  void canFindAll() {
    userCreatedShelfService.findAll();
    verify(userCreatedShelfRepository).findAll();
  }

  @Test
  void findAll_searchesWithoutFilter_ifFilterIsNull() {
    userCreatedShelfService.findAll(null);
    verify(userCreatedShelfRepository).findAll();
  }

  @Test
  void findAll_searchesForFilter_ifFilterIsNotNull() {
    userCreatedShelfService.findAll("test");
    verify(userCreatedShelfRepository).findByShelfName(anyString());
  }

  @Test
  void getBooksInCustomShelf_throwsException_ifShelfNameIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.getBooksInCustomShelf(null));
  }

  @Test
  void getCustomShelByName_throwsException_ifShelfNameIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.getCustomShelfByName(null));
  }

  @Test
  void findOrCreate_throwsException_ifShelfNameIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> userCreatedShelfService.findOrCreate(null));
  }
}

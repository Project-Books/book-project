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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.CustomShelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.CustomShelfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@ExtendWith(MockitoExtension.class)
class CustomShelfServiceTest {
    @Mock private CustomShelfRepository customShelfRepository;
    private CustomShelfService customShelfService;

    @BeforeEach
    void setUp() {
        UserService userService = mock(UserService.class);
        customShelfService = new CustomShelfService(customShelfRepository, userService);
    }

    @Test
    void findById_throwsException_ifIdIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.findById(null));
        verify(customShelfRepository, never()).findById(anyLong());
    }

    @Test
    void canFindById() {
        customShelfService.findById(1L);
        verify(customShelfRepository).findById(anyLong());
    }

    @Test
    // TODO: fix
    @Disabled
    void canFindAllForLoggedInUser() {
        customShelfService.findAllForLoggedInUser();
        verify(customShelfRepository).findAllByUser(any(User.class));
    }

    @Test
    // TODO: fix
    @Disabled
    void findByShelfNameAndLoggedInUser_throwsException_IfShelfNameIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.findByShelfNameAndLoggedInUser(null));
        verify(customShelfRepository).findByShelfNameAndUser(anyString(), any());
    }

    @Test
    void save_throwsException_ifShelfIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.save(null));
        verify(customShelfRepository, never()).save(any(CustomShelf.class));
    }

    @Test
    void canSaveNonNullCustomShelf() {
        // given
        User user = User.builder().build();
        CustomShelf customShelf = new CustomShelf("test", user);

        // when
        customShelfService.save(customShelf);

        // then
        ArgumentCaptor<CustomShelf> customShelfArgumentCaptor =
                ArgumentCaptor.forClass(CustomShelf.class);
        verify(customShelfRepository).save(customShelfArgumentCaptor.capture());
        CustomShelf actual = customShelfArgumentCaptor.getValue();
        assertThat(actual).isEqualTo(customShelf);
    }

    @Test
    void delete_throwsException_ifShelfIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.delete(null));
        verify(customShelfRepository, never()).delete(any(CustomShelf.class));
    }

    @Test
    void canDeleteNonNullCustomShelf() {
        // given
        User user = User.builder().build();
        CustomShelf customShelf = new CustomShelf("test", user);

        // when
        customShelfService.delete(customShelf);

        // then
        verify(customShelfRepository).delete(any(CustomShelf.class));
    }

    @Test
    void canDeleteAll() {
        customShelfService.deleteAll();
        verify(customShelfRepository).deleteAll();
    }

    @Test
    void canCount() {
        customShelfService.count();
        verify(customShelfRepository).count();
    }

    @Test
    void canFindAll() {
        customShelfService.findAll();
        verify(customShelfRepository).findAll();
    }

    @Test
    void findAll_searchesWithoutFilter_ifFilterIsNull() {
        customShelfService.findAll(null);
        verify(customShelfRepository).findAll();
    }

    @Test
    void findAll_searchesForFilter_ifFilterIsNotNull() {
        customShelfService.findAll("test");
        verify(customShelfRepository).findByShelfName(anyString());
    }

    @Test
    void getBooksInCustomShelf_throwsException_ifShelfNameIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.getBooksInCustomShelf(null));
    }

    @Test
    void getCustomShelByName_throwsException_ifShelfNameIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.getCustomShelfByName(null));
    }

    @Test
    void findOrCreate_throwsException_ifShelfNameIsNull() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> customShelfService.findOrCreate(null));
    }

}

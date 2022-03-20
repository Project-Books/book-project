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

import com.karankumar.bookproject.book.model.Tag;
import com.karankumar.bookproject.book.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class TagServiceTest {
  private TagService tagService;
  private TagRepository tagRepository;

  @BeforeEach
  void setUp() {
    tagRepository = mock(TagRepository.class);
    tagService = new TagService(tagRepository);
  }

  @Test
  void findById_throwsNullPointerException_ifIdIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> tagService.findById(null));
    verify(tagRepository, never()).findById(anyLong());
  }

  @Test
  void canFindByNonNullId() {
    // given
    Long id = 1L;

    // when
    tagService.findById(id);

    verify(tagRepository).findById(id);
  }

  @Test
  void findByName_throwsNullPointerException_ifNameIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> tagService.findByName(null));
    verify(tagRepository, never()).findByName(anyString());
  }

  @Test
  void canFindByNonNullName() {
    tagService.findByName("test");
    verify(tagRepository).findByName(anyString());
  }

  @Test
  void canFindAll() {
    tagService.findAll();
    verify(tagRepository).findAll();
  }

  @Test
  void save_throwsNullPointerException_ifTagIsNull() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> tagService.save(null));
    verify(tagRepository, never()).save(any(Tag.class));
  }

  @Test
  void tagNotSavedIfTagNameTaken() {
    // given
    given(tagRepository.findByName(anyString())).willReturn(Optional.of(new Tag("tag name")));
    Tag tag = new Tag("test");

    // when
    tagService.save(tag);

    // then
    verify(tagRepository, never()).save(tag);
  }

  @Test
  void tagSavedIfTagNameNotTaken() {
    // given
    given(tagRepository.findByName(anyString())).willReturn(Optional.empty());
    Tag tag = new Tag("test");

    // when
    tagService.save(tag);

    // then
    verify(tagRepository).save(tag);
  }

  @Test
  void canCount() {
    tagService.count();
    verify(tagRepository).count();
  }

  @Test
  void delete_throwsNullPointerException_ifTagIsNull() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> tagService.delete(null));
    verify(tagRepository, never()).save(any(Tag.class));
  }

  @Test
  void canDeleteNonNullTag() {
    // given
    Tag tag = new Tag("test");

    // when
    tagService.delete(tag);

    // then
    verify(tagRepository).delete(tag);
  }

  @Test
  void canDeleteAll() {
    tagService.deleteAll();
    verify(tagRepository).deleteAll();
  }
}

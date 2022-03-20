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

package com.karankumar.bookproject.book.service;

import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
  @Mock private AuthorRepository authorRepository;
  private AuthorService authorService;

  @BeforeEach
  void setUp() {
    authorService = new AuthorService(authorRepository);
  }

  @Test
  void findById_throwsException_ifIdIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> authorService.findById(null));
    verify(authorRepository, never()).findById(anyLong());
  }

  @Test
  void canFindByNonNullId() {
    authorService.findById(1L);
    verify(authorRepository).findById(anyLong());
  }

  @Test
  void canFindAll() {
    authorService.findAll();
    verify(authorRepository).findAll();
  }

  @Test
  void save_throwsException_ifAuthorIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> authorService.save(null));
    verify(authorRepository, never()).save(any(Author.class));
  }

  @Test
  void authorSavedIfNotNull() {
    // given
    Author author = new Author("full name");

    // when
    authorService.save(author);

    // then
    ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);

    verify(authorRepository).save(authorArgumentCaptor.capture());

    Author capturedAuthor = authorArgumentCaptor.getValue();
    assertThat(capturedAuthor).isEqualTo(author);
  }

  @Test
  void delete_throwsException_ifAuthorIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> authorService.delete(null));
    verify(authorRepository, never()).delete(any(Author.class));
  }

  @Test
  void authorDeletedIfNotNull() {
    // given
    Author author = new Author("full name");

    // when
    authorService.delete(author);

    // then
    ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);

    verify(authorRepository).delete(authorArgumentCaptor.capture());

    Author capturedAuthor = authorArgumentCaptor.getValue();
    assertThat(capturedAuthor).isEqualTo(author);
  }

  @Test
  void canDeleteAll() {
    authorService.deleteAll();
    verify(authorRepository).deleteAll();
  }

  @Test
  void canCount() {
    authorService.count();
    verify(authorRepository).count();
  }
}

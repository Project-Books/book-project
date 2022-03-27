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

import com.karankumar.bookproject.book.model.Publisher;
import com.karankumar.bookproject.book.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {
  private PublisherService underTest;
  private PublisherRepository publisherRepository;

  @BeforeEach
  void setUp() {
    publisherRepository = mock(PublisherRepository.class);
    underTest = new PublisherService(publisherRepository);
  }

  @Test
  void findById_throwsNullPointerException_ifIdIsNull() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> underTest.findById(null));
    verify(publisherRepository, never()).findById(anyLong());
  }

  @Test
  void canFindByNonNullId() {
    // given
    Long id = 1L;

    // when
    underTest.findById(id);

    // then
    verify(publisherRepository).findById(id);
  }

  @Test
  void save_throwsNullPointerException_ifPublisherIsNull() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> underTest.save(null));
    verify(publisherRepository, never()).save(any(Publisher.class));
  }

  @Test
  void canSaveValidPublisher() {
    // given
    Publisher publisher = new Publisher("test");

    // when
    underTest.save(publisher);

    // then
    verify(publisherRepository).save(publisher);
  }

  @Test
  void canFindAll() {
    underTest.findAll();
    verify(publisherRepository).findAll();
  }

  @Test
  void delete_throwsNullPointerException_ifPublisherIsNull() {
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> underTest.delete(null));
    verify(publisherRepository, never()).delete(any(Publisher.class));
  }

  @Test
  void canDeleteValidPublisher() {
    // given
    Publisher publisher = new Publisher("test");

    // when
    underTest.delete(publisher);

    // then
    verify(publisherRepository).delete(publisher);
  }

  @Test
  void canDeleteAll() {
    underTest.deleteAll();
    verify(publisherRepository).deleteAll();
  }

  @Test
  void canCount() {
    underTest.count();
    verify(publisherRepository).count();
  }
}

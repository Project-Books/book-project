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

package com.karankumar.bookproject.book.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.book.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaIntegrationTest
class AuthorRepositoryTest {
  @Autowired private AuthorRepository underTest;

  @BeforeEach
  void setUp() {
    underTest.deleteAll();
  }

  @Test
  void canFindAll() {
    // given
    Author author1 = new Author("author1");
    Author author2 = new Author("author2");
    underTest.saveAll(List.of(author1, author2));

    // when
    List<Author> authors = underTest.findAll();

    // then
    assertThat(authors).hasSize(2);
  }

  @Test
  void canDeleteIfExists() {
    // given
    Author author = new Author("author1");
    underTest.save(author);
    Long id = author.getId();

    // when
    underTest.delete(author);

    // then
    assertThat(underTest.findById(id)).isEmpty();
  }

  @Test
  void canDeleteAll() {
    // given
    Author author1 = new Author("author1");
    Author author2 = new Author("author2");
    underTest.saveAll(List.of(author1, author2));

    // when
    underTest.deleteAll();
    List<Author> authors = underTest.findAll();

    // then
    assertThat(authors).isEmpty();
  }
}

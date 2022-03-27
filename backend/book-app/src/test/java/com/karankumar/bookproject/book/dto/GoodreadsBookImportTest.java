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

package com.karankumar.bookproject.book.dto;

import com.karankumar.bookproject.bookimport.GoodreadsBookImport;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GoodreadsBookImport should")
class GoodreadsBookImportTest {
  @ParameterizedTest
  @ValueSource(strings = {"to-read", "currently-reading", "read"})
  void returnPredefinedShelfOnMatch(String shelfName) {
    // when
    Optional<PredefinedShelf.ShelfName> actual =
        GoodreadsBookImport.toPredefinedShelfName(shelfName);

    // then
    assertThat(actual).isNotEmpty();
  }

  @ParameterizedTest
  @MethodSource("generateInvalidShelfNames")
  void returnEmptyForInvalidShelfName() {
    // given
    String invalidShelfName = "shelf";

    // when
    Optional<PredefinedShelf.ShelfName> actual =
        GoodreadsBookImport.toPredefinedShelfName(invalidShelfName);

    // then
    assertThat(actual).isEmpty();
  }

  private static Stream<String> generateInvalidShelfNames() {
    return Stream.of("", " ", "shelf", null);
  }
}

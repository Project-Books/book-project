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

package com.karankumar.bookproject.book.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

@DisplayName("LocalDateSerializer should")
class LocalDateSerializerTest {
  @Test
  void serializeLocalDateCorrectly() throws IOException {
    // given
    LocalDateSerializer serializer = new LocalDateSerializer();
    LocalDate date = LocalDate.of(2020, 9, 5);
    String expectedJsonString = "\"2020-09-05\"";

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SimpleModule().addSerializer(serializer));

    // when
    String actualJsonString = mapper.writeValueAsString(date);

    // then
    assertThat(actualJsonString).isEqualTo(expectedJsonString);
  }
}

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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
  protected LocalDateSerializer() {
    super(LocalDate.class);
  }

  @Override
  public void serialize(
      LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    String dateString =
        localDate.getYear()
            + "-"
            + String.format("%02d", localDate.getMonthValue())
            + "-"
            + String.format("%02d", localDate.getDayOfMonth());
    jsonGenerator.writeString(dateString);
  }
}

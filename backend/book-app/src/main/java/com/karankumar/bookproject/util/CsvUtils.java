/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public final class CsvUtils {
  public static final String TEXT_CSV = "text/csv";
  private static final CsvMapper csvMapper;

  static {
    csvMapper = new CsvMapper();
    csvMapper.registerModule(new JavaTimeModule());
  }

  private CsvUtils() {}

  public static <T> List<T> read(InputStream inputStream, Class<T> classType) throws IOException {
    CsvSchema schema = CsvSchema.emptySchema().withHeader();
    MappingIterator<T> iterator =
        csvMapper.readerFor(classType).with(schema).readValues(inputStream);
    return iterator.readAll();
  }
}

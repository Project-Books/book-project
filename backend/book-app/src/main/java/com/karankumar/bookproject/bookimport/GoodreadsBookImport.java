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

package com.karankumar.bookproject.bookimport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoodreadsBookImport {

  @JsonProperty("Title")
  private String title;

  @JsonProperty("Author")
  private String author;

  @JsonProperty("My Rating")
  private Double rating;

  @JsonProperty("Date Read")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
  private LocalDate dateRead;

  @JsonProperty("Bookshelves")
  private String bookshelves;

  public static Optional<PredefinedShelf.ShelfName> toPredefinedShelfName(String shelfName) {
    if (StringUtils.isBlank(shelfName)) {
      return Optional.empty();
    }
    switch (shelfName.trim().toLowerCase().replace(",", "")) {
      case "to-read":
        return Optional.of(PredefinedShelf.ShelfName.TO_READ);
      case "currently-reading":
        return Optional.of(PredefinedShelf.ShelfName.READING);
      case "read":
        return Optional.of(PredefinedShelf.ShelfName.READ);
      default:
        return Optional.empty();
    }
  }
}

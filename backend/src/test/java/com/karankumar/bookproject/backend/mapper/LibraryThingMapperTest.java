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

package com.karankumar.bookproject.backend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.bookImport.libraryThing.LibraryThingBookImport;
import org.junit.jupiter.api.Test;

class LibraryThingMapperTest {

  private final LibraryThingMapper libraryThingMapper = new LibraryThingMapper();

  @Test
  void shouldMapImportDataToBook() {
    // given
    final LibraryThingBookImport bookImport = new LibraryThingBookImport("Title", "Author", 1999, "isbn");

    // when
    final Book book = libraryThingMapper.toBook(bookImport);

    // then
    assertEquals(book.getTitle(), bookImport.getTitle());
    assertEquals(book.getAuthor().getFullName(), bookImport.getAuthor());
    assertEquals(book.getYearOfPublication(), bookImport.getPublicationYear());
    assertEquals(book.getIsbn(), bookImport.getIsbn());

  }

}
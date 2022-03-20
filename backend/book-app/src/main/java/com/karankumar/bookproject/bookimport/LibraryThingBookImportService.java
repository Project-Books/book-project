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

package com.karankumar.bookproject.bookimport;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.karankumar.bookproject.bookimport.mapper.LibraryThingMapper;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.bookimport.librarything.LibraryThingBookImport;
import java.util.List;
import java.util.Optional;

import com.karankumar.bookproject.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class LibraryThingBookImportService {

  private final BookService bookService;
  private final LibraryThingMapper libraryThingMapper;

  public List<Book> importBooksFrom(List<LibraryThingBookImport> booksToImport) {
    if (booksToImport == null || booksToImport.isEmpty()) {
      LOGGER.info("No books to import from LibraryThing");
      return emptyList();
    }

    return booksToImport.stream()
        .map(libraryThingMapper::toBook)
        .map(bookService::save)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());
  }
}

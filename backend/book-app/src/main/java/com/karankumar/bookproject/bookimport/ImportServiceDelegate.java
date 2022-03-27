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

import com.karankumar.bookproject.bookimport.librarything.LibraryThingBookDataImport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImportServiceDelegate {

  private final LibraryThingBookImportService libraryThingBookImportService;
  private final ImportService goodreadsBookImportService;

  public void importFromLibraryThing(LibraryThingBookDataImport bookDataImport) {
    libraryThingBookImportService.importBooksFrom(bookDataImport.asList());
  }

  public void importFromGoodreads() {
    // TODO: Pass data from endpoint
    goodreadsBookImportService.importGoodreadsBooks(emptyList());
  }
}

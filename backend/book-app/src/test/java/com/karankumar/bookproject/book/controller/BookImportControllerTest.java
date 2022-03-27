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

package com.karankumar.bookproject.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.karankumar.bookproject.bookimport.BookImportController;
import com.karankumar.bookproject.bookimport.ImportServiceDelegate;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class BookImportControllerTest {

  private final ImportServiceDelegate importServiceDelegate = mock(ImportServiceDelegate.class);
  private final BookImportController bookImportController =
      new BookImportController(importServiceDelegate);

  @Test
  void shouldInvokeProperImportHandler() {
    // given
    final MultipartFile testFile = getTestFile();

    // when
    bookImportController.importFromLibraryThing(testFile);

    // then
    verify(importServiceDelegate, times(1)).importFromLibraryThing(any());
    verify(importServiceDelegate, never()).importFromGoodreads();
  }

  private MockMultipartFile getTestFile() {
    try {
      final InputStream testInputStream =
          getClass().getResourceAsStream("/libraryThingImportSample.json");
      return new MockMultipartFile("file.json", testInputStream);
    } catch (IOException exception) {
      throw new IllegalStateException();
    }
  }
}

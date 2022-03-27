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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karankumar.bookproject.bookimport.librarything.LibraryThingBookDataImport;
import com.karankumar.bookproject.bookimport.librarything.LibraryThingBookImport;
import java.io.IOException;
import java.util.HashMap;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class BookImportController {

  private final ImportServiceDelegate importServiceDelegate;

  @PostMapping(path = "/library-thing", consumes = "multipart/form-data")
  public void importFromLibraryThing(@RequestParam("file") @NotNull MultipartFile multipartForm) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();

      final TypeReference<HashMap<String, LibraryThingBookImport>> typeRef =
          new TypeReference<>() {};
      final HashMap<String, LibraryThingBookImport> libraryThingBookDataImport =
          objectMapper.readValue(multipartForm.getInputStream(), typeRef);
      final LibraryThingBookDataImport dataImport =
          new LibraryThingBookDataImport(libraryThingBookDataImport);

      importServiceDelegate.importFromLibraryThing(dataImport);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }
}

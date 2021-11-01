package com.karankumar.bookproject.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karankumar.bookproject.backend.service.ImportServiceDelegate;
import com.karankumar.bookproject.backend.model.bookImport.libraryThing.LibraryThingBookDataImport;
import com.karankumar.bookproject.backend.model.bookImport.libraryThing.LibraryThingBookImport;
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

      final TypeReference<HashMap<String, LibraryThingBookImport>> typeRef = new TypeReference<>() {
      };
      final HashMap<String, LibraryThingBookImport> libraryThingBookDataImport = objectMapper.readValue(
          multipartForm.getInputStream(),
          typeRef
      );
      final LibraryThingBookDataImport dataImport = new LibraryThingBookDataImport(libraryThingBookDataImport);

      importServiceDelegate.importFromLibraryThing(dataImport);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

}

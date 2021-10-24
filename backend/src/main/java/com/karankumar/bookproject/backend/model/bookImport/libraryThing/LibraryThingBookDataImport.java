package com.karankumar.bookproject.backend.model.bookImport.libraryThing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LibraryThingBookDataImport {

  private final Map<String, LibraryThingBookImport> books;

  public List<LibraryThingBookImport> asList() {
    return new ArrayList<>(books.values());
  }

}

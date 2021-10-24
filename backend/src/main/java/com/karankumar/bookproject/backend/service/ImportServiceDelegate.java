package com.karankumar.bookproject.backend.service;

import static java.util.Collections.emptyList;

import com.karankumar.bookproject.backend.model.bookImport.libraryThing.LibraryThingBookDataImport;
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

package com.karankumar.bookproject.backend.service;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.karankumar.bookproject.backend.mapper.LibraryThingMapper;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.bookImport.libraryThing.LibraryThingBookImport;
import java.util.List;
import java.util.Optional;
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
      LOGGER.info("No books to import from LibraryThings");
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

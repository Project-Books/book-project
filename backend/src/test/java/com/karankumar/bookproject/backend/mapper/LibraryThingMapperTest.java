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
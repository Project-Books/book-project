package com.karankumar.bookproject.backend.mapper;


import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.bookImport.libraryThing.LibraryThingBookImport;
import org.springframework.stereotype.Component;

@Component
public class LibraryThingMapper {

  public Book toBook(LibraryThingBookImport importBook) {
    final Book book = new Book();
    book.setTitle(importBook.getTitle());
    book.setAuthor(new Author(importBook.getAuthor()));
    book.setYearOfPublication(importBook.getPublicationYear());
    book.setIsbn(importBook.getIsbn());
    return book;
  }

}

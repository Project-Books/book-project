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

import com.karankumar.bookproject.book.dto.BookDto;
import com.karankumar.bookproject.book.dto.BookPatchDto;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.model.BookFormat;
import com.karankumar.bookproject.book.model.BookGenre;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookService bookService;
  private final PredefinedShelfService predefinedShelfService;
  private final ModelMapper modelMapper;

  private static final String BOOK_NOT_FOUND_ERROR_MESSAGE = "Could not find book with ID %d";
  public static final String NEGATIVE_PAGE_ERROR_MESSAGE = "Could not retrieve page with number %d";

  @Autowired
  public BookController(
      BookService bookService,
      PredefinedShelfService predefinedShelfService,
      ModelMapper modelMapper) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
    this.modelMapper = modelMapper;

    this.modelMapper.addConverter(predefinedShelfConverter);
    this.modelMapper.addConverter(bookGenreConverter);
    this.modelMapper.addConverter(bookFormatConverter);
  }

  Converter<String, PredefinedShelf> predefinedShelfConverter =
      new AbstractConverter<>() {
        @Override
        public PredefinedShelf convert(String predefinedShelfName) {
          Optional<PredefinedShelf.ShelfName> optionalShelfName =
              PredefinedShelfService.getPredefinedShelfName(predefinedShelfName);

          if (optionalShelfName.isEmpty()) {
            String errorMessage =
                String.format("%s does not match a predefined shelf", predefinedShelfName);
            throw new IllegalStateException(errorMessage);
          }

          Optional<PredefinedShelf> optionalPredefinedShelf =
              predefinedShelfService.getPredefinedShelfByPredefinedShelfName(
                  optionalShelfName.get());

          if (optionalPredefinedShelf.isEmpty()) {
            // TODO: throw custom exception
            throw new IllegalStateException();
          }

          return optionalPredefinedShelf.get();
        }
      };

  Converter<String, BookGenre> bookGenreConverter =
      new AbstractConverter<>() {
        public BookGenre convert(String bookGenreString) {
          return BookGenre.valueOf(bookGenreString);
        }
      };

  Converter<String, BookFormat> bookFormatConverter =
      new AbstractConverter<>() {
        public BookFormat convert(String bookFormatString) {
          return BookFormat.valueOf(bookFormatString);
        }
      };

  @GetMapping()
  // TODO: only retrieve books that belong to the logged in user
  public List<Book> all(@RequestParam(required = false) Integer page) {
    if (page == null || page >= 0) {
      return bookService.findAll(page);
    } else {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, String.format(NEGATIVE_PAGE_ERROR_MESSAGE, page));
    }
  }

  @GetMapping("/genres")
  public BookGenre[] getGenres() {
    return BookGenre.values();
  }

  @GetMapping("/{id}")
  // TODO: only retrieve books that belong to the logged in user
  public Book findById(@PathVariable Long id) {
    return bookService
        .findById(id)
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format(BOOK_NOT_FOUND_ERROR_MESSAGE, id)));
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Optional<Book> addBook(@RequestBody BookDto bookDto) {
    Book bookToAdd = convertToBook(bookDto);
    // TODO: check whether the book to save has a title, an author and a predefined shelf. If not,
    // throw a 400-level exception
    return bookService.save(bookToAdd);
  }

  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Book update(@PathVariable Long id, @RequestBody BookPatchDto bookPatchDto) {
    final Book bookToUpdate =
        bookService
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format(BOOK_NOT_FOUND_ERROR_MESSAGE, id)));
    return bookService.updateBook(bookToUpdate, bookPatchDto);
  }

  private Book convertToBook(BookDto bookDto) {
    return modelMapper.map(bookDto, Book.class);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<String> delete(@PathVariable Long id) {
    Optional<Book> bookToDeleteOp = bookService.findById(id);
    if (bookToDeleteOp.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(String.format(BOOK_NOT_FOUND_ERROR_MESSAGE, id));
    }

    bookService.delete(bookToDeleteOp.get());
    return ResponseEntity.status(HttpStatus.OK).body("Deleted");
  }
}

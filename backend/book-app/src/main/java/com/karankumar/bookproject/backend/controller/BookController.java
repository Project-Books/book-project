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

package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.dto.BookDto;
import com.karankumar.bookproject.backend.dto.BookPatchDto;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.BookFormat;
import com.karankumar.bookproject.backend.model.BookGenre;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

  @Autowired
  public BookController(BookService bookService, PredefinedShelfService predefinedShelfService,
      ModelMapper modelMapper) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
    this.modelMapper = modelMapper;

    this.modelMapper.addConverter(predefinedShelfConverter);
    this.modelMapper.addConverter(bookGenreConverter);
    this.modelMapper.addConverter(bookFormatConverter);
  }

  Converter<String, PredefinedShelf> predefinedShelfConverter = new AbstractConverter<>() {
    @Override
    public PredefinedShelf convert(String predefinedShelfName) {
      Optional<PredefinedShelf.ShelfName> optionalShelfName =
          PredefinedShelfService.getPredefinedShelfName(predefinedShelfName);

      if (optionalShelfName.isEmpty()) {
        String errorMessage = String.format(
            "%s does not match a predefined shelf",
            predefinedShelfName
        );
        throw new IllegalStateException(errorMessage);
      }

      Optional<PredefinedShelf> optionalPredefinedShelf = predefinedShelfService
          .getPredefinedShelfByPredefinedShelfName(optionalShelfName.get());

      if (optionalPredefinedShelf.isEmpty()) {
        // TODO: throw custom exception
        throw new IllegalStateException();
      }

      return optionalPredefinedShelf.get();
    }
  };

  Converter<String, BookGenre> bookGenreConverter = new AbstractConverter<>() {
    public BookGenre convert(String bookGenreString) {
      return BookGenre.valueOf(bookGenreString);
    }
  };

  Converter<String, BookFormat> bookFormatConverter = new AbstractConverter<>() {
    public BookFormat convert(String bookFormatString) {
      return BookFormat.valueOf(bookFormatString);
    }
  };

  @GetMapping()
  // TODO: only retrieve books that belong to the logged in user
  public List<Book> all() {
    return bookService.findAll();
  }

  @GetMapping("/{id}")
  // TODO: only retrieve books that belong to the logged in user
  public Book findById(@PathVariable Long id) {
    return bookService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format(BOOK_NOT_FOUND_ERROR_MESSAGE, id))
        );
  }


  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Optional<Book> addBook(@RequestBody BookDto bookDto) {
    Book bookToAdd = convertToBook(bookDto);
    // TODO: check whether the book to save has a title, an author and a predefined shelf. If not, throw a 400-level exception
    return bookService.save(bookToAdd);
  }

  @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Book update(@PathVariable Long id, @RequestBody BookPatchDto bookPatchDto) {
    final var bookToUpdate = bookService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format(BOOK_NOT_FOUND_ERROR_MESSAGE, id)
            )
        );
    return bookService.updateBook(bookToUpdate, bookPatchDto);
  }

  private Book convertToBook(BookDto bookDto) {
    return modelMapper.map(bookDto, Book.class);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    Book bookToDelete = bookService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format(BOOK_NOT_FOUND_ERROR_MESSAGE, id))
        );
    bookService.delete(bookToDelete);
  }
}

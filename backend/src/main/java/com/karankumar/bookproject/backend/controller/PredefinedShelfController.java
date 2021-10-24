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

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName;
import com.karankumar.bookproject.backend.service.BookService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shelf/books")
public class PredefinedShelfController {

  private final BookService bookService;

  @Autowired
  public PredefinedShelfController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping(path = "/to-read")
  // TODO: only retrieve books that belong to the logged in user
  public List<Book> getAllToReadBooks() {
    return bookService.findAllBooksByPredefinedShelfName(ShelfName.TO_READ);
  }

  @GetMapping(path = "/reading")
  // TODO: only retrieve books that belong to the logged in user
  public List<Book> getAllReadingBooks() {
    return bookService.findAllBooksByPredefinedShelfName(ShelfName.READING);
  }

  @GetMapping(path = "/read")
  // TODO: only retrieve books that belong to the logged in user
  public List<Book> getAllReadBooks() {
    return bookService.findAllBooksByPredefinedShelfName(ShelfName.READ);
  }

  @GetMapping(path = "/did-not-finish")
  // TODO: only retrieve books that belong to the logged in user
  public List<Book> getAllDidNotFinishBooks() {
    return bookService.findAllBooksByPredefinedShelfName(ShelfName.DID_NOT_FINISH);
  }
}

/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2022  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.bookexport;

import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export-books-data")
public class BookExportController {

  private final UserService userService;
  private final BookExportService bookExportService;

  public BookExportController(UserService userService, BookExportService bookExportService) {
    this.bookExportService = bookExportService;
    this.userService = userService;
  }

  @GetMapping
  public ExportDto exportBooksData() {
    User currentUser = userService.getCurrentUser();
    return bookExportService.exportBooksData(currentUser);
  }
}

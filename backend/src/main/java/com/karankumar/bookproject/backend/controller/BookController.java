/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
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
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.JsonToDbService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/my-books")
public class BookController {
    private final BookService bookService;

    @Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

    @GetMapping()
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

//    @GetMapping("/find-by-id/{id}")
//    public List<Book> findById(@PathVariable("id") UUID id) {
////        return bookService.findById();
//    }

    @PostMapping("/add-book")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@NonNull @RequestBody @Valid Book book){
//        return bookService.save(book);
    }

    @GetMapping("/import-json")
    @ResponseStatus(HttpStatus.CREATED)
    public void importJson(@NonNull @Valid String file){
        System.out.println("ran import-json");
//        return JsonToDbService.importJson("/Users/zacharyhollander/Documents/Development/FOSS/book-project/backend/src/main/java/com/karankumar/bookproject/backend/json/bookExport-2.json");
    }

}

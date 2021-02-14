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

import com.karankumar.bookproject.backend.model.account.Book;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my-books")
public class BookController {
	
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping() //get all books
    public List<Book> all() {
    	return bookService.findAll();
    }
    
    @GetMapping("/{id}") 	
    public Book findById(@PathVariable Long id) { 
    	return bookService.findById(id)		//get book by id
    		.orElseThrow(() -> new BookNotFoundException(id));
    }
    
    @GetMapping("/{id}") 	
    public Book findByShelf(@PathVariable Long id, 
    		@RequestParam Shelf shelf, 
    		@RequestParam(required=false) String title, 
    		@RequestParam(required=false) String authorsName) { 
    	return bookService.findByShelfAndTitleOrAuthor(shelf, title, authorsName)	//get book by shelf and title/author
    		.orElseThrow(() -> new BookNotFoundException(id));
    }
    		
    @GetMapping("/{id}") 	
    public Book findByAuthor(@PathVariable Long id, 
    		@RequestParam(required=false) String title, 
    		@RequestParam String authorsName) {   
    	return bookService.findByTitleOrAuthor(title, authorsName)	//get book by title/author
    		.orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping()	//add new book
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Book> addBook(@RequestBody Book newBook) {
        return bookService.save(newBook);                                   
    }
    
    @PutMapping("/{id}")	//update an existing book
    public void update(@PathVariable Long id) {
    	
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	Book bookToDelete = bookService.findById(id)
    		.orElseThrow(() -> new BookNotFoundException(id));
    	bookService.delete(bookToDelete);
    }

}

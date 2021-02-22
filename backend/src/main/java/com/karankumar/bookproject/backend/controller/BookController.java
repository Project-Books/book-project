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
import com.karankumar.bookproject.backend.service.BookNotFoundException;
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

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/my-books")
public class BookController {
	
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping()
    public List<Book> all() {
    	return bookService.findAll();
    }
    
    @GetMapping("/find-by-id/{id}") 	
    public Optional<Book> findById(@PathVariable Long id) { 
    	return Optional.ofNullable(bookService.findById(id))
    		.orElseThrow(() -> new BookNotFoundException(id));
    }
    
    @GetMapping("/find-by-shelf/{shelf}") 	
    public Optional<List<Book>> findByShelf(@PathVariable Shelf shelf, 
    		//@RequestParam Shelf shelf, 
    		@RequestParam(required=false) String title, 
    		@RequestParam(required=false) String authorsName,
    		@RequestParam Long id) { 
    	return Optional.ofNullable(bookService.findByShelfAndTitleOrAuthor(shelf, title, authorsName))
    		.orElseThrow(() -> new BookNotFoundException(id));
    }
    		
    @GetMapping("/find-by-author/{author}") 	
    public Optional<List<Book>> findByAuthor(@PathVariable String authorsName, 
    		@RequestParam(required=false) String title,
    		@RequestParam Long id) {
    		//@RequestParam String authorsName) {   
    	return Optional.ofNullable(bookService.findByTitleOrAuthor(title, authorsName))
    		.orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping("/add-book")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Book> addBook(@RequestBody Book newBook) {
        return bookService.save(newBook);                                   
    }
    
    @PutMapping("/update-book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Book> update(@PathVariable Long id, @RequestBody Book updatedBook) {
    	Optional<Book> bookToUpdate = bookService.findById(id);
    	
    	if (!bookToUpdate.isPresent()) {
    		throw new BookNotFoundException(id);
    	}
    	
    	return bookService.save(updatedBook);
    }
    
    @DeleteMapping("/delete-book/{id}")
    public void delete(@PathVariable Long id) {
    	Book bookToDelete = bookService.findById(id)
    		.orElseThrow(() -> new BookNotFoundException(id));
    	bookService.delete(bookToDelete);
    }

}

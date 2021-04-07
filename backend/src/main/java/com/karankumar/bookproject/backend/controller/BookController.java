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

import org.modelmapper.Converter;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import com.karankumar.bookproject.backend.dto.BookDto;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.BookGenre;
import com.karankumar.bookproject.backend.model.BookFormat;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.List;
//import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/my-books")
public class BookController {
	
    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;
    private ModelMapper modelMapper;

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
    public Optional<List<Book>> findByAuthor(@PathVariable String author /*authorsName*/,
    		@RequestParam(required=false) String title//,
    		/*@RequestParam Long id*/) {
    		//@RequestParam String authorsName) {
        System.out.println("here");
    	return bookService.findByTitleOrAuthor(title, author /*authorsName*/);
//    		.orElseThrow(() -> new BookNotFoundException(id));
    }
    
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Book> addBook(@RequestBody BookDto bookDto) {
    	// convert DTO to entity
    	Book bookToAdd = convertToBook(bookDto);

        return bookService.save(bookToAdd);
    }
    
    @PatchMapping("/update-book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Book> update(@PathVariable Long id, @RequestBody Map<String, Object> changes) { //@RequestBody BookDto updatedBookDto) {
    	//fetch existing Book entity and ensure it exists
        Optional<Book> bookToUpdate = bookService.findById(id);
    	if (!bookToUpdate.isPresent()) {
    		throw new BookNotFoundException(id);
    	}

        //map persistant data to REST BookDto
        //BookDto bookDtoToUpdate = convertToDto(bookToUpdate.get());

        //apply the changes to the REST BookDto
        // changes.forEach(
        //     (change, value) -> {
        //         switch (change){
        //             case "title": bookDtoToUpdate.setTitle((String) value); break;
        //             case "numberOfPages": bookDtoToUpdate.setNumberOfPages((Integer) value); break;
        //             case "pagesRead": bookDtoToUpdate.setPagesRead((Integer) value); break;
        //         }
        //     }
        // );
        changes.forEach(
            (change, value) -> {
                switch (change) {
                    case "title":
                        bookToUpdate.get().setTitle((String) value);
                        break;
                    case "author":
                        bookToUpdate.get().setAuthor((Author) modelMapper.map(value, Author.class));
                        break;
                    case "predefinedShelfString":
                        bookToUpdate.get().setPredefinedShelf((PredefinedShelf) modelMapper.map(value, PredefinedShelf.class));
                        break;
                    case "numberOfPages":
                        bookToUpdate.get().setNumberOfPages((Integer) value);
                        break;
                    case "pagesRead":
                        bookToUpdate.get().setPagesRead((Integer) value);
                        break;
                    case "bookGenre":
                        bookToUpdate.get().setBookGenre((BookGenre) modelMapper.map(value, BookGenre.class));
                        break;
                    case "bookFormat":
                        bookToUpdate.get().setBookFormat((BookFormat) modelMapper.map(value, BookFormat.class));
                        break;
                    case "seriesPosition":
                        bookToUpdate.get().setSeriesPosition((Integer) value);
                        break;
                    case "edition":
                        bookToUpdate.get().setEdition((Integer) value);
                        break;
                    case "bookRecommendedBy":
                        bookToUpdate.get().setBookRecommendedBy((String) value);
                        break;
                    case "isbn":
                        bookToUpdate.get().setIsbn((String) value);
                    case "yearofPublication":
                        bookToUpdate.get().setPublicationYear((Integer) value);
                        break;
                }
            }
        );

        //modelMapper.map(changes, bookDtoToUpdate);
    	//Book updatedBook = convertToBook(bookDtoToUpdate);

    	//updatedBookDto.setId(id);
    	//Book updatedBook = convertToBook(updatedBookDto);
    	return bookService.save(bookToUpdate.get());
    }
    
    private BookDto convertToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }
    
    private Book convertToBook(BookDto bookDto) { //throws ParseException {
        return modelMapper.map(bookDto, Book.class);
    }
    
    @DeleteMapping("/delete-book/{id}")
    public void delete(@PathVariable Long id) {
    	Book bookToDelete = bookService.findById(id)
    		.orElseThrow(() -> new BookNotFoundException(id));
    	bookService.delete(bookToDelete);
    }
    
    Converter<String, PredefinedShelf> predefinedShelfConverter = new AbstractConverter<>() {
        //public PredefinedShelf convert(MappingContext<String, PredefinedShelf> context) {
        public PredefinedShelf convert(String predefinedShelfString) {
            PredefinedShelf predefinedShelf = null;
            predefinedShelf =
                    predefinedShelfService.getPredefinedShelfByNameAsString(predefinedShelfString);
            return predefinedShelf;
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

}

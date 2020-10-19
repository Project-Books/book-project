/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Publisher;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PublisherRepository;

import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@Service
@Log
public class BookService {
    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final PublisherService publisherService;

    public BookService(BookRepository bookRepository, AuthorService authorService, PublisherService publisherService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    public Book findById(Long id) {
        return bookRepository.getOne(id);
    }

    public void save(Book book) {
        if (bookHasAuthorAndPredefinedShelf(book)) {
        	addBookToPublisher(book);
        	savePublisher(book.getPublisher());
            addBookToAuthor(book);
            authorService.save(book.getAuthor());
            bookRepository.save(book);
        }
    }
    

    private boolean bookHasAuthorAndPredefinedShelf(Book book) {
        return book != null && book.getAuthor() != null && book.getPredefinedShelf() != null;
    }
    
    private boolean bookHasPublisher(Book book) {
    	return isValidPublisher(book.getPublisher());
    }

    private void addBookToAuthor(Book book) {
        Author author = book.getAuthor();
        Set<Book> authorBooks = author.getBooks();
        authorBooks.add(book);
        author.setBooks(authorBooks);
    }
    
    private void addBookToPublisher(Book book) {
        Publisher publisher = book.getPublisher();
        if(isValidPublisher(publisher)) {
        	Set<Book> publisherBooks = publisher.getBooks();
            publisherBooks.add(book);
            publisher.setBooks(publisherBooks);
        }
    }
    
    private void savePublisher(Publisher publisher) {
    	if(isValidPublisher(publisher)) {
    		publisherService.save(publisher);
    	}
    }
    
    private boolean isValidPublisher(Publisher publisher) {
    	return publisher != null && !StringUtils.isEmpty(publisher.getName());
    }

    public Long count() {
        return bookRepository.count();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCase(filterText);
    }

    public void delete(Book book) {
        if (book == null) {
            LOGGER.log(Level.SEVERE, "Cannot delete an null book.");
            return;
        }

        LOGGER.log(Level.INFO, "Deleting book. Book repository size = " + bookRepository.count());
        bookRepository.delete(book);

        List<Book> books = bookRepository.findAll();
        if (books.contains(book)) {
            LOGGER.log(Level.SEVERE, book.getTitle() + " not deleted");
        } else {
            LOGGER.log(Level.INFO, book.getTitle() + " deleted. Book repository size = " +
                    bookRepository.count());
        }
    }

    public void deleteAll() {
        if (bookRepository.count() == 0) {
            LOGGER.log(Level.INFO, "All books already deleted");
            return;
        }
        LOGGER.log(Level.INFO, "Deleting all in books & authors. Book repository size = " +
                bookRepository.count());
        bookRepository.deleteAll();
        authorService.deleteAll();
        LOGGER.log(Level.INFO, "Deleted all books in books & authors. Book repository size = " +
                bookRepository.count());
    }

    public String getJsonRepresentationForBooksAsString() throws JsonProcessingException {
        List<Book> books = bookRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter jsonWriter = mapper.writer().withRootName("AllBooks");
        return jsonWriter.writeValueAsString(books);
    }
}

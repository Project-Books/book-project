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
import com.karankumar.bookproject.backend.entity.Shelf;
import com.karankumar.bookproject.backend.repository.BookRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@Service
@Log
public class BookService {
    private final AuthorService authorService;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public Book findById(Long id) {
        return bookRepository.getOne(id);
    }

    public void save(Book book) {
        if (bookHasAuthorAndPredefinedShelf(book)) {
            addBookToAuthor(book);
            authorService.save(book.getAuthor());
            bookRepository.save(book);
        }
    }

    private boolean bookHasAuthorAndPredefinedShelf(@NonNull Book book) {
        return book.getAuthor() != null && book.getPredefinedShelf() != null;
    }

    private void addBookToAuthor(Book book) {
        Author author = book.getAuthor();
        Set<Book> authorBooks = author.getBooks();
        authorBooks.add(book);
        author.setBooks(authorBooks);
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

    public void delete(@NonNull Book book) {
        LOGGER.log(Level.INFO, "Deleting book. Book repository size = " + bookRepository.count());
        bookRepository.delete(book);

        List<Book> books = bookRepository.findAll();
        if (books.contains(book)) {

            LOGGER.log(Level.SEVERE, book.getTitle() + " not deleted");
        } else {
            LOGGER.log(Level.INFO, book.getTitle() + " deleted. Book repository size = " +
                    bookRepository.count());

            Author author = book.getAuthor();
            removeBookFromAuthor(book, author);
            removeAuthorWithoutBooks(author);
        }
    }

    private void removeBookFromAuthor(Book book, Author author) {
        author.removeBook(book);
    }

    private void removeAuthorWithoutBooks(Author author) {
        if (author.getBooks().isEmpty()) {
            authorService.delete(author);
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

    public List<Book> findByShelfAndTitleOrAuthor(Shelf shelf, String title, String authorsName){
        return bookRepository.findByShelfAndTitleOrAuthor(shelf, title, authorsName);
    }

    public List<Book> findByTitleOrAuthor(String title, String authorsName){
        return bookRepository.findByTitleOrAuthor(title, authorsName);
    }
}

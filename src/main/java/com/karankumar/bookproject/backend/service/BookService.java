/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.repository.BookRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;

@Service
@Log
public class BookService extends BaseService<Book, Long> {

    private final AuthorService authorService;
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.getOne(id);
    }

    @Override
    public void save(Book book) {
        if (book == null) {
            LOGGER.log(Level.SEVERE, "Cannot save the null book");
            return;
        }
        if (book.getAuthor() == null) {
            LOGGER.log(Level.SEVERE, "Without author book cannot be saved.");
            return;
        }
        if (book.getShelf() == null || book.getShelf().getShelfName() == null) {
            LOGGER.log(Level.SEVERE, "Shelf not assigned to book, cannot be saved");
            return;
        }
        authorService.save(book.getAuthor());
        bookRepository.save(book);
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
        return bookRepository.search(filterText);
    }

    @Override
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
            LOGGER.log(
                Level.INFO,
                book.getTitle() + " deleted. Book repository size = " + bookRepository.count());
        }
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
        authorService.deleteAll();
    }
}

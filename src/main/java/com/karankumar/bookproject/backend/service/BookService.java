package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

/**
 * @author karan on 08/05/2020
 */

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findById(Long id) {
        return bookRepository.getOne(id);
    }

    public void save(Book book) {
        if (book != null) {
            bookRepository.save(book);
        }
    }

    public Long count() {
        return bookRepository.count();
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

}

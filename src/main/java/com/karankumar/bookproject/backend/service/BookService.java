package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Book;

import java.util.Set;

/**
 * @author karan on 08/05/2020
 */
public interface BookService {
    Book findById(Long id);

    Book save(Book book);

    Set<Book> findAll();
}

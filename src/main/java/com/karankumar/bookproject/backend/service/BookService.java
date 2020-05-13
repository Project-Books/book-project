package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author karan on 08/05/2020
 */

@Service
public class BookService extends BaseService<Book, Long> {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.getOne(id);
    }

    @Override
    public void save(Book book) {
        if (book != null) {
            bookRepository.save(book);
        }
    }

    public Long count() {
        return bookRepository.count();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}

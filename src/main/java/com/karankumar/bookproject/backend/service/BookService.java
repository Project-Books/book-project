package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A Spring service that acts as the gateway to the {@code BookRepository} -- to use the {@code BookRepository},
 * a consumer should go via this {@code BookService}
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

    public List<Book> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.search(filterText);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}

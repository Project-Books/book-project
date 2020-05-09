package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author karan on 08/05/2020
 */
@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findById(Long id) {
        return authorRepository.getOne(id);
    }

    public void save(Author author) {
        if (author != null) {
            authorRepository.save(author);
        }
    }

    public void delete(Author author) {
        authorRepository.delete(author);
    }
}

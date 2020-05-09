package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import org.springframework.stereotype.Service;


/**
 * @author karan on 08/05/2020
 */
@Service
public class AuthorService extends BaseService<Author, Long> {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.getOne(id);
    }

    @Override
    public void save(Author author) {
        if (author != null) {
            authorRepository.save(author);
        }
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }
}

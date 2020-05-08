package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Author;

import java.util.Set;

/**
 * @author karan on 08/05/2020
 */
public interface AuthorService {

    Author findById(Long id);

    Author findByLastName(String lastName);

    Author save(Author author);

    Set<Author> findAll();
}

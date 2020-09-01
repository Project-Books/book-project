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

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.BaseEntity;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service
@Log
public class AuthorService extends BaseService<Author, Long> {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.getOne(id);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public void save(Author author) {
        if (author != null) {
            List<Long> matchingAuthors = findAll().stream()
                .map(BaseEntity::getId)
                .filter(id -> id.equals(author.getId())).collect(Collectors.toList());
            if (matchingAuthors.size() == 1) {
                // if an author with the same ID exists, set this incoming author ID to null so that
                // a new row in the table is made rather than updating the row that has the same ID
                LOGGER.log(Level.INFO, "Matching authorIds: " + matchingAuthors);
                author.removeId();
            }

            authorRepository.save(author);
        }
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

    public Long count() {
        return authorRepository.count();
    }

    @Override
    public void deleteAll() {
        authorRepository.deleteAll();
    }
}

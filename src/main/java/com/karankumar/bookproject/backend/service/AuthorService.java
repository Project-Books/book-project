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

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
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
            List<Author> matchingAuthors = isMatchingAuthorsPresent(author);
            if (!matchingAuthors.isEmpty()) {
                // if an author with the same ID exists, then create a new Author so that a new row in the
                // table is made rather than updating the row that has the same ID
                LOGGER.log(Level.INFO, "Matching authors: " + matchingAuthors);
                author = new Author(author.getFirstName(), author.getLastName());
            }

            authorRepository.save(author);
        }
    }

    public List<Author> isMatchingAuthorsPresent(@NotNull Author author) {
        return findAll().stream()
                        .filter(existingAuthor -> existingAuthor.toString().equals(author.toString()))
                        .collect(Collectors.toList());
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

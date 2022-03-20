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

package com.karankumar.bookproject.book.service;

import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.repository.AuthorRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Log
@Transactional
public class AuthorService {
  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public Optional<Author> findById(@NonNull Long id) {
    return authorRepository.findById(id);
  }

  public List<Author> findAll() {
    return authorRepository.findAll();
  }

  public void save(@NonNull Author author) {
    authorRepository.save(author);
  }

  public void delete(@NonNull Author author) {
    authorRepository.delete(author);
  }

  public void deleteAll() {
    authorRepository.deleteAll();
  }

  public Long count() {
    return authorRepository.count();
  }
}

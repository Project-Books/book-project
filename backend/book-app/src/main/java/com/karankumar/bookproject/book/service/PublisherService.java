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

import com.karankumar.bookproject.book.model.Publisher;
import com.karankumar.bookproject.book.repository.PublisherRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class PublisherService {
  private final PublisherRepository publisherRepository;

  public PublisherService(PublisherRepository publisherRepository) {
    this.publisherRepository = publisherRepository;
  }

  public Optional<Publisher> findById(@NonNull Long id) {
    return publisherRepository.findById(id);
  }

  public void save(@NonNull Publisher publisher) {
    if (StringUtils.isNotEmpty(publisher.getName())) {
      publisherRepository.save(publisher);
    }
  }

  public List<Publisher> findAll() {
    return publisherRepository.findAll();
  }

  public void delete(@NonNull Publisher publisher) {
    publisherRepository.delete(publisher);
  }

  public void deleteAll() {
    publisherRepository.deleteAll();
  }

  public Long count() {
    return publisherRepository.count();
  }
}

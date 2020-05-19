/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShelfService extends BaseService<Shelf, Long> {

    private ShelfRepository shelfRepository;

    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    @Override
    public Shelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    @Override
    public void save(Shelf shelf) {
        if (shelf != null) {
            shelfRepository.save(shelf);
        }
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    @Override
    public void delete(Shelf shelf) {
        shelfRepository.delete(shelf);
    }

    @PostConstruct
    public void populateTestData() {
        if (shelfRepository.count() == 0) {
            shelfRepository.saveAll(
                    Stream.of("Want to read", "Currently reading", "Read")
                        .map(Shelf::new).collect(Collectors.toList()));
        }
    }
}

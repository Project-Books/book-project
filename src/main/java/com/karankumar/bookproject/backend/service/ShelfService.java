package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author karan on 08/05/2020
 */
@Service
public class ShelfService {

    private ShelfRepository shelfRepository;

    public ShelfService(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    public Shelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    public void save(Shelf shelf) {
        return shelfRepository.save(shelf)
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }
}

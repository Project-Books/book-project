package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Shelf;

import java.util.Set;

/**
 * @author karan on 08/05/2020
 */
public interface ShelfService {
    Shelf findById(Long id);

    Shelf save(Shelf shelf);

    Set<Shelf> findAll();
}

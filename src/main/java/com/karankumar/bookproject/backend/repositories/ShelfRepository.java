package com.karankumar.bookproject.backend.repositories;

import com.karankumar.bookproject.backend.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author karan on 07/05/2020
 */
public interface ShelfRepository extends JpaRepository<Shelf, Long> {
}

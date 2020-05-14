package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}

package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

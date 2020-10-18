package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.entity.book.BookProgress;
import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<BookProgress, BookProgressId> {
}

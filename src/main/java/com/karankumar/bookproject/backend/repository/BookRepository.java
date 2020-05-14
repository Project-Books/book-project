package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b " +
        "where lower(b.title) like lower(concat('%', :filterText, '%'))")
    List<Book> search(@Param("filterText") String filterText);

}

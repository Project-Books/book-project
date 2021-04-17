/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b " +
            "FROM Book b " +
            "INNER JOIN FETCH b.author " +
            "INNER JOIN FETCH b.predefinedShelf " +
            "INNER JOIN FETCH b.tags " +
            "INNER JOIN FETCH b.publishers" )
    List<Book> findAllBooks();


    @Query("SELECT b " +
            "FROM Book b " +
            "INNER JOIN FETCH b.author " +
            "INNER JOIN FETCH b.predefinedShelf " +
            "INNER JOIN FETCH b.tags " +
            "INNER JOIN FETCH b.publishers " +
            "WHERE b.id = :id" )
    Optional<Book> findBookById(@Param("id") Long id);

    List<Book> findByTitleContainingIgnoreCase(String title);

    // TODO: remove this. This was for testing purposes.
    @Query("SELECT b " +
            "FROM Book b " +
            "INNER JOIN FETCH b.predefinedShelf AS p " +
            "INNER JOIN FETCH b.customShelf AS c " +
            "WHERE (LOWER(p.shelfName) = LOWER(:shelf) OR LOWER(c.shelfName) = LOWER(:shelf))")
            /* +
            "(LOWER(b.title) LIKE LOWER(CONCAT('%', :titleOrAuthor, '%')) OR " +
            "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :titleOrAuthor, '%')))")*/
    List<Book> findByShelfAndTitleOrAuthor2(@Param("shelf") String shelfName);


    @Query("SELECT b " +
            "FROM Book b " +
            "INNER JOIN FETCH b.author a " +
            "WHERE (b.predefinedShelf.shelfName = :shelf OR b.customShelf.shelfName = :shelf) AND " +
            "(LOWER(b.title) LIKE LOWER(CONCAT('%', :titleOrAuthor, '%')) OR " +
            "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :titleOrAuthor, '%')))")
    List<Book> findByShelfAndTitleOrAuthor(@Param("shelf") String shelfName,
                                           @Param("titleOrAuthor") String titleOrAuthor);

    @Query("SELECT b " +
            "FROM Book b " +
            "INNER JOIN FETCH b.author AS a " +
            "INNER JOIN FETCH b.predefinedShelf " +
            "INNER JOIN FETCH b.tags " +
            "INNER JOIN FETCH b.publishers " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :titleOrAuthor, '%')) OR " +
                "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :titleOrAuthor, '%'))")
    List<Book> findByTitleOrAuthor(@Param("titleOrAuthor") String titleOrAuthor);
}

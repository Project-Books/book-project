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

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT b " +
            "FROM Book b " +
            "LEFT JOIN b.author a " +
            "WHERE (b.predefinedShelf = :shelf OR b.customShelf = :shelf) AND " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND " +
            "(LOWER(a.firstName) LIKE LOWER(CONCAT('%', :authorsName, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :authorsName, '%')))")
    List<Book> findByShelfAndTitleOrAuthor(@Param("shelf") Shelf shelf,
                                           @Param("title") String title,
                                           @Param("authorsName") String authorsName);

    @Query("SELECT b " +
            "FROM Book b " +
            "LEFT JOIN b.author AS a " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND " +
            "(LOWER(a.firstName) LIKE LOWER(CONCAT('%', :authorsName, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :authorsName, '%')))")
    List<Book> findByTitleOrAuthor(@Param("title") String title,
                                   @Param("authorsName") String authorsName);

}

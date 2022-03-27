/*
   The book project lets a user keep track of different books they would like to read, are currently
   reading, have read or did not finish.
   Copyright (C) 2020  Karan Kumar

   This program is free software: you can redistribute it and/or modify it under the terms of the
   GNU General Public License as published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful, but WITHOUT ANY
   WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
   PURPOSE.  See the GNU General Public License for more details.

   You should have received a copy of the GNU General Public License along with this program.
   If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.shelf.repository;

import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.account.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCreatedShelfRepository extends JpaRepository<UserCreatedShelf, Long> {

  @EntityGraph(value = "CustomShelf.books")
  List<UserCreatedShelf> findAllByUser(User user);

  @EntityGraph(value = "CustomShelf.books")
  Optional<UserCreatedShelf> findByShelfNameAndUser(String shelfName, User user);

  @EntityGraph(value = "CustomShelf.books")
  List<UserCreatedShelf> findByShelfName(String shelfName);

  @EntityGraph(value = "CustomShelf.books")
  Optional<UserCreatedShelf> findById(Long id);

  @EntityGraph(value = "CustomShelf.books")
  List<UserCreatedShelf> findAll();

  @Query(
      "SELECT "
          + "CASE WHEN COUNT(s) > 0 THEN TRUE "
          + "ELSE FALSE END "
          + "FROM UserCreatedShelf s "
          + "WHERE LOWER(TRIM(s.shelfName)) LIKE LOWER(TRIM(:shelfName))")
  boolean shelfNameExists(@Param("shelfName") String shelfName);
}

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

import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.account.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PredefinedShelfRepository extends JpaRepository<PredefinedShelf, Long> {
  @EntityGraph(value = "PredefinedShelf.books")
  List<PredefinedShelf> findAllByUser(User user);

  @EntityGraph(value = "PredefinedShelf.books")
  Optional<PredefinedShelf> findByPredefinedShelfNameAndUser(
      PredefinedShelf.ShelfName shelfName, User user);

  int countAllByUser(User user);

  @EntityGraph(value = "PredefinedShelf.books")
  Optional<PredefinedShelf> findById(Long id);

  @EntityGraph(value = "PredefinedShelf.books")
  List<PredefinedShelf> findAll();

  @Query(
      "SELECT p "
          + "FROM PredefinedShelf p "
          + "LEFT JOIN p.books AS b "
          + "WHERE p.predefinedShelfName = com.karankumar.bookproject.book.model.PredefinedShelfName.READ")
  List<PredefinedShelf> findReadShelf2();
}

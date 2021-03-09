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


package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredefinedShelfRepository extends JpaRepository<PredefinedShelf, Long> {
    List<PredefinedShelf> findAllByUser(User user);
    Optional<PredefinedShelf> findByPredefinedShelfNameAndUser(PredefinedShelf.ShelfName shelfName,
    		User user);
    int countAllByUser(User user);
}

/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converts the last name field (a String) into an {@code Author}'s last name field and visa versa
 */
public class StringToAuthorLastNameConverter implements Converter<String, Author> {
  private Logger logger = Logger.getLogger(StringToAuthorLastNameConverter.class.getName());

  @Override
  public Result<Author> convertToModel(String lastName, ValueContext valueContext) {
    if (lastName == null || lastName.isEmpty()) {
      return Result.error("Please enter a last name");
    }
    Author author = new Author();
    author.setLastName(lastName);
    return Result.ok(author);
  }

  @Override
  public String convertToPresentation(Author author, ValueContext valueContext) {

    if (author != null && author.getLastName() != null) {
        return author.getLastName();
    } else if (author == null) {
      logger.log(Level.FINER, "Author is null");
    } else {
      logger.log(Level.FINER, "Author's last name is null");
    }
    return "Invalid"; // TODO Change
  }
}

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
 * Converts the first name field (a String) into an {@code Author}'s first name field and visa versa
 */
public class StringToAuthorFirstNameConverter implements Converter<String, Author> {
    private static Logger logger = Logger.getLogger(StringToAuthorFirstNameConverter.class.getName());

    @Override
    public Result<Author> convertToModel(String firstName, ValueContext valueContext) {
        if (firstName == null || firstName.isEmpty()) {
            return Result.error("Please enter a first name");
        }
        Author author = new Author();
        author.setFirstName(firstName);
        return Result.ok(author);
    }

    @Override
    public String convertToPresentation(Author author, ValueContext valueContext) {
        if (author == null || author.getFirstName() == null) {
            if (author == null) {
                logger.log(Level.FINE, "Author is null");
            } else if (author.getFirstName() == null) {
                logger.log(Level.FINE, "Author's first name is null");
            }
            return "Error";
        }
        return author.getFirstName();
    }
}

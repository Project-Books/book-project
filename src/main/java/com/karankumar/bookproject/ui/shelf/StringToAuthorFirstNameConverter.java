package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Converts the first name field (a String) into an {@code Author}'s first name field and visa versa
 */
public class StringToAuthorFirstNameConverter implements Converter<String, Author> {
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
        return author.getFirstName();
    }
}

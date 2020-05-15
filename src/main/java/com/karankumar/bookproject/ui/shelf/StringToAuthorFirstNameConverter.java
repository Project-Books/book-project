package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToAuthorFirstNameConverter implements Converter<String, Author> {
    @Override
    public Result<Author> convertToModel(String firstName, ValueContext valueContext) {
        if (firstName == null || firstName.isEmpty()) {
            Result.error("Name not provided");
        } else if (firstName.contains(" ")) {
            String[] split = firstName.split(" ");
            Author author = new Author();
            author.setFirstName(split[0]);
            author.setLastName(split[1]);
            return Result.ok(author);
        }
        return Result.error("Please enter a first name");
    }

    @Override
    public String convertToPresentation(Author author, ValueContext valueContext) {
        return author.getFirstName();
    }
}

package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToAuthorLastNameConverter implements Converter<String, Author> {
    @Override
    public Result<Author> convertToModel(String lastName, ValueContext valueContext) {
        if (lastName == null || lastName.isEmpty()) {
            Result.error("Name not provided");
        } else if (lastName.contains(" ")) {
            String[] split = lastName.split(" ");
            Author author = new Author();
            author.setFirstName(split[0]);
            author.setLastName(split[1]);
            return Result.ok(author);
        }
        return Result.error("Please enter a first name");
    }

    @Override
    public String convertToPresentation(Author author, ValueContext valueContext) {
        return author.getLastName();
    }
}

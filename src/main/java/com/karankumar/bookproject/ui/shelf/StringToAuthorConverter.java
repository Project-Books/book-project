package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * @author karan on 15/05/2020
 */
public class StringToAuthorConverter implements Converter<String, Author> {
    @Override
    public Result<Author> convertToModel(String result, ValueContext valueContext) {
        if (result == null || result.isEmpty()) {
            Result.error("Name not provided");
        } else if (result.contains(" ")) {
            String[] split = result.split(" ");
            Author author = new Author();
            author.setFirstName(split[0]);
            author.setLastName(split[1]);
            return Result.ok(author);
        }
        return Result.error("First name or last name not provided");
    }

    @Override
    public String convertToPresentation(Author author, ValueContext valueContext) {
        return author.toString();
    }
}

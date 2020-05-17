package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Author;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Converts the last name field (a String) into an {@code Author}'s last name field and visa versa
 */
public class StringToAuthorLastNameConverter implements Converter<String, Author> {
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
    return author.getLastName();
  }
}

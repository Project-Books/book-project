package com.karankumar.bookproject.backend.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a shelf (or a list) of books (e.g. books in a 'to read' shelf)
 */

@Entity
public class Shelf extends BaseEntity {
    @NotNull
    @NotEmpty
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Book> books;

    public Shelf() {
    }

    public Shelf(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
